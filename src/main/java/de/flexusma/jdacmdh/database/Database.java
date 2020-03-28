/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * CC0 1.0 Universal
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh.database;


import de.flexusma.jdacmdh.CommandPreferences;
import de.flexusma.jdacmdh.debug.LogType;
import de.flexusma.jdacmdh.debug.Logger;
import net.dv8tion.jda.api.JDA;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.graalvm.compiler.lir.amd64.vector.AMD64VectorBinary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.sql.*;

public class Database {
    //Database setup + statements;
    private final static String tablename = "wavvy_preferences";

    private static final String SQL_SERIALIZE_OBJECT = "INSERT INTO " + tablename + " ( id,s_pref) VALUES (?,  ?)";
    private static final String SQL_SERIALIZE_UOBJECT = "UPDATE " + tablename + "  SET s_pref = ? WHERE id = ?";
    private static final String SQL_DESERIALIZE_OBJECT = "SELECT s_pref FROM " + tablename + "  WHERE id = ?";
    private static final String SQL_CHECKCREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String SQL_CHECKCREATE_COLUMN = "ALTER TABLE "+tablename+" ADD COLUMN IF NOT EXISTS ? ?;";

    private static String url;
    private static String user;
    private static String password;
    private String purl;
    private String puser;
    private String ppassword;
    private static boolean isInit = false;
    private static Connection con = null;


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

    public boolean initDB() {

        Logger.log(LogType.INFO, "Starting up Database handler...");
        return checkDatabase();

    }

    //check database, table and column setup;
    private boolean checkDatabase() {

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

        if(!checkTableExists(tablename)) return false;
        Logger.log(LogType.INFO, "Checking Database setup: Columns...");
        for (Field f : CommandPreferences.class.getFields()) {
            if(!checkColumnExists(f.getName(),f)) return false;
        }
        Logger.log(LogType.INFO, "Database check complete! starting...");
        return true;
    }



    private boolean checkTableExists(String tableName){
        boolean res =false;
        try {

            PreparedStatement pstmt = getCon().prepareStatement(SQL_CHECKCREATE_TABLE+tableName);
            res = pstmt.execute();
            pstmt.close();

        }catch (SQLException e){
            Logger.log(LogType.ERROR, "Error while checking Table: "+e.getErrorCode()+e.getMessage());
            return false;
        }
        if(res) {
            Logger.log(LogType.INFO, "Checked / Created Table.");
        }
        else {
            Logger.log(LogType.ERROR, "Unknown error while checking Table.");
        }
        return res;
    }

    private boolean checkColumnExists(String colummnName, Field field){
        boolean res =false;
        try {

            PreparedStatement pstmt = getCon().prepareStatement(SQL_CHECKCREATE_COLUMN);
            pstmt.setString(0,tablename);
            pstmt.setString(1,JDBCType.valueOf(field.getType().getTypeName()).getName());
            res = pstmt.execute();
            pstmt.close();

        }catch (SQLException e){
            Logger.log(LogType.ERROR, "Error while checking Column["+colummnName+"]: "+e.getErrorCode()+e.getMessage());
            return false;
        }
        if(res) {
            Logger.log(LogType.INFO, "Checked / Created Column["+colummnName+"]. ");
        }
        else {
            Logger.log(LogType.ERROR, "Unknown error while checking Column["+colummnName+"]. ");
        }
        return res;
    }




    public static void prefToDB(Connection connection, String id,
                                Object objectToSerialize) throws SQLException {

        PreparedStatement pstmt = connection
                .prepareStatement(SQL_SERIALIZE_OBJECT);

        // just setting the class name
        pstmt.setString(1, id);
        //pstmt.setString(2, objectToSerialize.getClass().getName());
        pstmt.setObject(2, objectToSerialize);
        pstmt.executeUpdate();
        pstmt.close();
        Logger.log(LogType.INFO, "Java object serialized to database. Object: "
                + objectToSerialize);
    }
    

    public static void uprefToDB(Connection connection, String id,
                                 Object objectToSerialize) throws SQLException {

        PreparedStatement pstmt = connection
                .prepareStatement(SQL_SERIALIZE_UOBJECT);

        // just setting the class name
        pstmt.setString(2, id);
        //pstmt.setString(2, objectToSerialize.getClass().getName());
        pstmt.setObject(1, objectToSerialize);
        pstmt.executeUpdate();
        pstmt.close();
        Logger.log(LogType.INFO, "Java object Update-serialized to database. Object: "
                + objectToSerialize);
    }


    public static CommandPreferences prefFromDB(Connection connection, String id) throws SQLException, IOException,
            ClassNotFoundException {
        PreparedStatement pstmt = connection
                .prepareStatement(SQL_DESERIALIZE_OBJECT);
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();

        byte[] buf = rs.getBytes(1);
        ObjectInputStream objectIn = null;
        if (buf != null)
            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));

        CommandPreferences deSerializedObject = (CommandPreferences) objectIn.readObject();

        rs.close();
        pstmt.close();

        Logger.log(LogType.INFO, "Java object de-serialized from database. Object: "
                + deSerializedObject + " Classname: "
                + deSerializedObject.getClass().getName());
        connection.close();
        return deSerializedObject;
    }


    public static CommandPreferences initPref(JDA jda, String id) {





       /* try {
            return Database.prefFromDB(Database.getCon(), id);
        } catch (SQLException e) {
            Logger.log(LogType.WARN, "SQL Error: " + e.getErrorCode() + " " + e.getMessage());

        } catch (Exception e) {
            try {
                prefToDB(Database.getCon(), id, new CommandPreferences());
            } catch (SQLException ex) {
                Logger.log(LogType.WARN, ex.getMessage());
            }
        }*/
        return new CommandPreferences();
    }

    public static void savePref(JDA jda, String id, CommandPreferences pref) {

        try {
            prefToDB(Database.getCon(), id, pref);
        } catch (SQLException e) {
            Logger.log(LogType.WARN, "SQL Error: " + e.getErrorCode() + " " + e.getMessage());
            try {
                uprefToDB(Database.getCon(), id, pref);
            } catch (SQLException e1) {
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
