/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh.database;


import de.flexusma.jdacmdh.CommandPreferences;
import de.flexusma.jdacmdh.debug.LogType;
import de.flexusma.jdacmdh.debug.Logger;
import de.flexusma.jdacmdh.utils.JAVAToSQLDatatype;
import net.dv8tion.jda.api.JDA;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    //Database setup + statements;
    private final static String tablename = "preferences";

    private static final String SQL_SERIALIZE_OBJECT = "INSERT INTO " + tablename + " (id,<names>) VALUES (<id>, <values>)";
    private static final String SQL_SERIALIZE_UOBJECT = "UPDATE " + tablename + " SET <data> WHERE id = ?";
    private static final String SQL_DESERIALIZE_OBJECT = "SELECT * FROM " + tablename + " WHERE id = ?";
    private static final String SQL_CHECKCREATE_TABLE = "CREATE TABLE IF NOT EXISTS ? ( `id` VARCHAR(150) NOT NULL PRIMARY KEY ) ENGINE = MyISAM";
    private static final String SQL_CHECKCREATE_COLUMN = "ALTER TABLE\n " + tablename + " ADD COLUMN\n IF NOT EXISTS\n <value>\n ?;";

    private static String url;
    private static String user;
    private static String password;
    private String purl;
    private String puser;
    private String ppassword;
    private static boolean isInit = false;
    private static Connection con = null;
    //class data
    private static CommandPreferences prefStructure;
    private static Class<?> prefClass;

    public static Connection getCon() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            Logger.log(LogType.WARN, e.getMessage());
            return null;
        }
    }

    public Database(String dbUrl, String username, String password) {
        this.purl = dbUrl;
        this.puser = username;
        this.ppassword = password;
    }

    public boolean initDB(CommandPreferences preferences) {

        Logger.log(LogType.INFO, "Starting up Database handler...");
        return checkDatabase(preferences);

    }

    //check database, table and column setup;
    private boolean checkDatabase(CommandPreferences preferences) {

        //checking class type of Preferences
        Logger.log(LogType.DEBUG, "Class name of Preferences: " + preferences.getClass().getName());
        prefClass = preferences.getClass();


        Logger.log(LogType.INFO, "Checking Database connection...");
        try (Connection connection = DriverManager.getConnection(purl, puser, ppassword)) {
            Logger.log(LogType.INFO, "Database connected!");
            isInit = true;
            con = connection;

            url = purl;
            user = puser;
            password = ppassword;

        } catch (SQLException e) {
            Logger.log(LogType.ERROR, "Could not connect to Databse! " + e);
            return false;
        }
        Logger.log(LogType.INFO, "Database connection established.");
        Logger.log(LogType.INFO, "Checking Database setup: Tables...");

        if (!checkTableExists(tablename)) return false;
        Logger.log(LogType.INFO, "Checking Database setup: Columns...");
        Logger.log(LogType.DEBUG, preferences.getClass().getFields().length + "");
        for (Field f : preferences.getClass().getFields()) {
            Logger.log(LogType.DEBUG, "Field[" + f.getName() + "] " + f.getType());
            if (!checkColumnExists(f.getName(), f)) return false;
        }
        Logger.log(LogType.INFO, "Database check complete! starting...");
        prefStructure = preferences;
        return true;
    }


    private boolean checkTableExists(String tableName) {
        try {
            String query = SQL_CHECKCREATE_TABLE.replace("?", "`" + tableName + "`");
            PreparedStatement pstmt = getCon().prepareStatement(query);
            pstmt.execute();
            pstmt.close();

        } catch (SQLException e) {
            Logger.log(LogType.ERROR, "Error while checking Table: " + e.getErrorCode() + "|" + e.getMessage());
            return false;
        }
        Logger.log(LogType.INFO, "Checked / Created Table.");
        return true;
    }

    private boolean checkColumnExists(String colummnName, Field field) {
        try {
            String s = SQL_CHECKCREATE_COLUMN.replace("<value>", "`" + colummnName + "`");
            JDBCType type = JAVAToSQLDatatype.convertType(field.getType());
            if (type != null) {
                Logger.log(LogType.DEBUG, type.getName());
                if (type.equals(JDBCType.LONGVARCHAR))
                    s = s.replace("?", "LONGTEXT NOT NULL");
                else
                    s = s.replace("?", type.getName() + " NOT NULL");
            } else {
                Logger.log(LogType.ERROR, "Error while checking/creating Column[" + colummnName + "].  Please make sure that your variable is of the supported Datatypes! Please only make sure only Database Variables are public!");
                return false;
            }
            Logger.log(LogType.DEBUG, s);
            PreparedStatement pstmt = getCon().prepareStatement(s);


            pstmt.execute();
            pstmt.close();

        } catch (SQLException e) {
            Logger.log(LogType.ERROR, "Error while checking Column[" + colummnName + "]: " + e.getErrorCode() + "|" + e.getMessage());
            return false;
        }
        Logger.log(LogType.INFO, "Checked / Created Column[" + colummnName + "]. ");
        return true;
    }


    public static void prefToDB(Connection connection, String id, CommandPreferences objectToSerialize) throws SQLException, IllegalAccessException {
        Field[] fields = prefStructure.getClass().getFields();
        Logger.log(LogType.DEBUG, fields.length + "");
        StringBuilder names = new StringBuilder();
        int count = 0;
        StringBuilder values = new StringBuilder();
        for (Field f : fields) {
            if (count == 0) {
                names.append("`" + f.getName() + "`");
                values.append("?");
            } else {
                names.append(",`" + f.getName() + "`");
                values.append(",?");
            }

            count++;
        }


        String query = SQL_SERIALIZE_OBJECT.replace("<names>", names.toString());
        query = query.replace("<id>", id);
        query = query.replace("<values>", values.toString());


        PreparedStatement pstmt = connection.prepareStatement(query);
        int counter = 1;
        for (Field f : fields) {
            setPreparedData(f, objectToSerialize, pstmt, counter);
            counter++;
        }
        Logger.log(LogType.DEBUG, "prefDB: " + query);
        pstmt.executeUpdate();
        pstmt.close();
        Logger.log(LogType.INFO, "Java object serialized to database. Object: "
                + objectToSerialize);
    }


    public static void uprefToDB(Connection connection, String id, CommandPreferences objectToSerialize) throws SQLException, IllegalAccessException {


        Field[] fields = objectToSerialize.getClass().getFields();
        List<String> data = new ArrayList<>();

        for (Field f : fields) {
            data.add(createSQLData(f, objectToSerialize));
        }
        StringBuilder updateData = new StringBuilder();
        int count = 0;
        for (String s : data) {
            if (count == 0)
                updateData.append(s);
            else updateData.append("," + s);
            count++;
        }

        String query = SQL_SERIALIZE_UOBJECT.replace("<data>", updateData);


        PreparedStatement pstmt = connection.prepareStatement(query);


        // just setting the class name


        int counter = 1;
        for (Field f : fields) {
            setPreparedData(f, objectToSerialize, pstmt, counter);
            counter++;
        }
        pstmt.setString(counter, id);
        Logger.log(LogType.DEBUG, pstmt.toString());
        pstmt.executeUpdate();
        pstmt.close();
        Logger.log(LogType.INFO, "Updated preferences succesfully!");
    }


    public static CommandPreferences prefFromDB(Connection connection, String id) throws SQLException {
        PreparedStatement pstmt = connection
                .prepareStatement(SQL_DESERIALIZE_OBJECT);
        pstmt.setString(1, id);
        Logger.log(LogType.DEBUG,"Pref from DB query: "+pstmt.toString());
        ResultSet rs = pstmt.executeQuery();
        rs.next();


        Object[] args = new Object[prefStructure.getClass().getFields().length];

        int col = rs.getMetaData().getColumnCount();



        for (Constructor ctor : prefClass.getConstructors()) {
            Class<?>[] paramTypes = ctor.getParameterTypes();

            // If the arity matches, let's use it.
            Logger.log(LogType.DEBUG, args.length + " | " + paramTypes.length);
            //  CommandPreferences preferences = prefStructure;
            if (args.length == paramTypes.length) {

                for(int i =1; i<col;i++){
                    args[i-1]=getDataFromRS(prefStructure.getClass().getFields()[i-1].getType(),rs,i+1);
                  //  preferences.getClass().getMethod("set"+prefStructure.getClass().getFields()[i-1].getName(),prefStructure.getClass().getFields()[i-1].getType()).invoke(preferences,args[i-1]);
                }
                // Instantiate the object with the converted arguments.
                Logger.log(LogType.INFO, "Downloaded + parsed Data from Database");
                rs.close();
                pstmt.close();
                connection.close();


                try {
                    return ((CommandPreferences) ctor.newInstance(args)).returnCastedInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    Logger.log(LogType.DEBUG,"Error at instantiating class instance");
                }
            }
        }


        return null;
    }


    private static Object getDataFromRS(Type target, ResultSet rs, int index) throws SQLException {
        Logger.log(LogType.DEBUG,"Trying to parse type: "+target.getTypeName()+" for "+rs.getObject(index).toString());
        if (target == Object.class || target == String.class) {
            return rs.getObject(index);
        }
        if (target == Character.class || target == char.class) {
            return rs.getCharacterStream(index);
        }
        if (target == Byte.class || target == byte.class) {
            return rs.getByte(index);
        }
        if (target == Short.class || target == short.class) {
            return rs.getShort(index);
        }
        if (target == Integer.class || target == int.class) {
            return rs.getInt(index);
        }
        if (target == Long.class || target == long.class) {
            return rs.getLong(index);
        }
        if (target == Float.class || target == float.class) {
            return rs.getFloat(index);
        }
        if (target == Double.class || target == double.class) {
            return rs.getDouble(index);
        }
        if (target == Boolean.class || target == boolean.class) {
            return rs.getBoolean(index);
        }
        return null;
    }


    private static String createSQLData(Field field, CommandPreferences preferences) throws SQLException, IllegalAccessException {
        return "`" + field.getName() + "` = ?";
    }

    private static void setPreparedData(Field field, CommandPreferences cpreferences, PreparedStatement pr, int index) throws SQLException, IllegalAccessException {

        Logger.log(LogType.DEBUG, "Class name of newPreferences: " + cpreferences.getClass().getName());
        Logger.log(LogType.DEBUG, "Class name of storedPreferences: " + prefClass.getName());

        CommandPreferences preferences = cpreferences.returnCastedInstance();

        Logger.log(LogType.DEBUG, cpreferences.getClass().getName() + " class is to be setpreparedData");

        Type target = field.getType();
        if (target == Object.class || target == String.class) {
            pr.setObject(index, field.get(preferences));
        }
        if (target == Byte.class || target == byte.class) {
            pr.setByte(index, field.getByte(preferences));
        }
        if (target == Short.class || target == short.class) {
            pr.setShort(index, field.getShort(preferences));
        }
        if (target == Integer.class || target == int.class) {
            pr.setInt(index, field.getInt(preferences));
        }
        if (target == Long.class || target == long.class) {
            pr.setLong(index, field.getLong(preferences));
        }
        if (target == Float.class || target == float.class) {
            pr.setFloat(index, field.getFloat(preferences));
        }
        if (target == Double.class || target == double.class) {
            pr.setObject(index, field.getDouble(preferences));
        }
        if (target == Boolean.class || target == boolean.class) {
            pr.setObject(index, field.getBoolean(preferences));
        }

    }


    public static CommandPreferences initPref(JDA jda, String id) {
        try {
            return Database.prefFromDB(Database.getCon(), id);
        } catch (SQLException e) {
            Logger.log(LogType.WARN, "SQL Error1: " + " " + e.getMessage());
            e.printStackTrace();
            try {
                prefToDB(Database.getCon(), id, prefStructure);
            } catch (SQLException | IllegalAccessException ex) {
                Logger.log(LogType.WARN, ex.getMessage());
            }
        }
        return prefStructure;
    }

    public static void savePref(JDA jda, String id, CommandPreferences pref) {

        try {
            prefToDB(Database.getCon(), id, pref);
        } catch (SQLException | IllegalAccessException e) {
            Logger.log(LogType.WARN, "SQL Error: " + " " + e.getMessage());
            try {
                uprefToDB(Database.getCon(), id, pref);
            } catch (SQLException | IllegalAccessException e1) {
                Logger.log(LogType.WARN, e1.getMessage());
            }
        }

    }

  /*  // serializing java object to mysql database
    long serialized_id = serializeJavaObjectToDB(connection, obj);

    // de-serializing java object from mysql database
    Vector objFromDatabase = (Vector) deSerializeJavaObjectFromDB(
            connection, serialized_id);

        connection.close();*/
}
