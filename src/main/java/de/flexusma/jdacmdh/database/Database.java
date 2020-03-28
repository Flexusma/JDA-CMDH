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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.Arrays;

public class Database {
    private static String url;
    private static String user;
    private static String password;
    private  String purl;
    private  String puser;
    private  String ppassword;
    private static boolean isInit = false;
    private static Connection con = null;


    public static Connection getCon(){
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            Logger.log(LogType.WARN,e.getMessage());
            return null;
        }
    }

    public Database(String dbUrl, String username, String password){
        this.purl=dbUrl;
        this.puser=username;
        this.ppassword =password;
    }

    public boolean initDB() {
        Logger.log(LogType.INFO,"Connecting to DB...");
        try (Connection connection = DriverManager.getConnection(purl, puser, ppassword)) {
            Logger.log(LogType.INFO,"Database connected!");
            isInit = true;
            con = connection;

            url=purl;
            user=puser;
            password=ppassword;

            return true;
        } catch (SQLException e) {
            Logger.log(LogType.ERROR,"Cannot connect the database! " + e);
            return false;
        }

    }


    private static final String SQL_SERIALIZE_OBJECT = "INSERT INTO preferences( id," +
            //"name," +
            " s_pref) VALUES (?, " +
            //"?," +
            " ?)";

    private static final String SQL_SERIALIZE_UOBJECT = "UPDATE preferences SET s_pref = ? WHERE id = ?";
    private static final String SQL_DESERIALIZE_OBJECT = "SELECT s_pref FROM preferences WHERE id = ?";


    private static final String Games_SERIALIZE_OBJECT = "INSERT INTO ?( id," +
            //"name," +
            " data) VALUES (?, " +
            //"?," +
            " ?)";

    private static final String Games_SERIALIZE_UOBJECT = "UPDATE ? SET data = ? WHERE id = ?";
    private static final String Games_DESERIALIZE_OBJECT = "SELECT data FROM ? WHERE id = ?";
    private static final String Gamess_DESERIALIZE_OBJECT = "SELECT data FROM ?";
    private static final String Games_Exists_table = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?) " +
                                                        "BEGIN " +
                                                        "CREATE TABLE ? (id BIGINT, data LONGBLOB,PRIMARY KEY(id) ) " +
                                                        "END";

    public static void prefToDB(Connection connection,String id,
                                Object objectToSerialize) throws SQLException {

        PreparedStatement pstmt = connection
                .prepareStatement(SQL_SERIALIZE_OBJECT);

        // just setting the class name
        pstmt.setString(1, id);
        //pstmt.setString(2, objectToSerialize.getClass().getName());
        pstmt.setObject(2, objectToSerialize);
        pstmt.executeUpdate();
        pstmt.close();
        Logger.log(LogType.INFO,"Java object serialized to database. Object: "
                + objectToSerialize);
    }

    public static void uprefToDB(Connection connection,String id,
                                Object objectToSerialize) throws SQLException {

        PreparedStatement pstmt = connection
                .prepareStatement(SQL_SERIALIZE_UOBJECT);

        // just setting the class name
        pstmt.setString(2, id);
        //pstmt.setString(2, objectToSerialize.getClass().getName());
        pstmt.setObject(1, objectToSerialize);
        pstmt.executeUpdate();
        pstmt.close();
        Logger.log(LogType.INFO,"Java object Update-serialized to database. Object: "
                + objectToSerialize);
    }


    public static CommandPreferences prefFromDB(Connection connection,
                                                String id) throws SQLException, IOException,
            ClassNotFoundException {
        PreparedStatement pstmt = connection
                .prepareStatement(SQL_DESERIALIZE_OBJECT);
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();

        // Object object = rs.getObject(1);

        byte[] buf = rs.getBytes(1);
        ObjectInputStream objectIn = null;
        if (buf != null)
            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));

        CommandPreferences deSerializedObject = (CommandPreferences)objectIn.readObject();

        rs.close();
        pstmt.close();

        Logger.log(LogType.INFO,"Java object de-serialized from database. Object: "
                + deSerializedObject + " Classname: "
                + deSerializedObject.getClass().getName());
        connection.close();
        return deSerializedObject;
    }


    public static CommandPreferences initPref(JDA jda, String id){
        try {
            return Database.prefFromDB(Database.getCon(), id);
        }catch (SQLException e) {
            e.printStackTrace();
            Logger.log(LogType.WARN, "SQL Error: " + e.getErrorCode() + " " + e.getMessage()+" "+ Arrays.toString(e.getStackTrace()));

        }catch (Exception e){
            try {
                prefToDB(Database.getCon(),id,new CommandPreferences());
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.log(LogType.WARN,ex.getMessage()+" "+ Arrays.toString(ex.getStackTrace()));
            }
        }
        return new CommandPreferences();
    }

    public static void savePref(JDA jda, String id, CommandPreferences pref){

        try {
            prefToDB(Database.getCon(),id,pref);
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log(LogType.WARN, "SQL Error: "+e.getErrorCode()+" "+e.getMessage()+" "+ Arrays.toString(e.getStackTrace()));
            try {
                uprefToDB(Database.getCon(),id,pref);
            } catch (SQLException e1) {
                e1.printStackTrace();
                Logger.log(LogType.WARN,e1.getMessage()+" "+ Arrays.toString(e1.getStackTrace()));
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
