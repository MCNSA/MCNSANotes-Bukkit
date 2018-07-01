package uk.co.maboughey.mcnsanotes.database;

import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Log;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement= null;
    private static ResultSet resultSet = null;

    public static Connection getConnection() {
        if (connect == null)
            connect();
        return connect;
    }

    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties connProperties = new Properties();
            connProperties.put("user", Configuration.DBUser);
            connProperties.put("password", Configuration.DBPassword);
            connProperties.put("maxReconnects","4");
            connProperties.put("autoReconnect", "true");

            connect = DriverManager.getConnection(Configuration.getDatabaseString(), connProperties);
            Log.info("Connection to Database Established");

        }
        catch (SQLException e){
            Log.error("Database Error connecting to database: "+ e.getMessage());
            connect = null;
            McnsaNotes.isEnabled = false;
        }
        catch (ClassNotFoundException e) {
            Log.error("Could not find mysql connector");
            McnsaNotes.isEnabled = false;        }
    }

    public static void close() {
        try {
            if(resultSet != null) resultSet.close();
            if(statement != null) statement.close();
            if(preparedStatement != null) preparedStatement.close();
            if(connect != null) connect.close();
        }
        catch(Exception e) {
            Log.error("Database exception during close. Message was: "+e.getMessage());
        }
    }

    public void tablesCreate() {
        try {
            // load the database
            connect();

            // create the tables if they does not exist
            preparedStatement = connect.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS notes ( " +
                            "id int(6) NOT NULL AUTO_INCREMENT, " +
                            "date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                            "uuid varchar(50) NOT NULL, "+
                            "noter_uuid varchar(50) NOT NULL, " +
                            "server varchar(20) NOT NULL, " +
                            "note TEXT NOT NULL,"+
                            "PRIMARY KEY (id));");
            preparedStatement.executeUpdate();

            preparedStatement = connect.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS "+Configuration.ServerName+"_stats ("+
                            "uuid varchar(50) NOT NULL, "+
                            "firstjoined timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                            "timeonserver bigint NOT NULL, " +
                            "blocksplaced bigint(10) NOT NULL, " +
                            "blocksbroken bigint(10) NOT NULL, " +
                            "numdeaths bigint(10) NOT NULL, " +
                            "numkicks bigint(10) NOT NULL, " +
                            "modreqs bigint(10) NOT NULL, " +
                            "logins bigint(10) NOT NULL);");
            preparedStatement.executeUpdate();

            preparedStatement = connect.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS knownUsernames ("+
                            "id int(6) NOT NULL AUTO_INCREMENT, "+
                            "uuid varchar(50) NOT NULL, "+
                            "name varchar(20) NOT NULL, "+
                            "PRIMARY KEY (id));"
            );
            preparedStatement.executeUpdate();

            preparedStatement = connect.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS taggedplayers (" +
                            "id int(6) NOT NULL AUTO_INCREMENT, " +
                            "uuid varchar(50) NOT NULL, " +
                            "noter_uuid varchar(50), " +
                            "reason TEXT NOT NULL, " +
                            "PRIMARY KEY (id))"
            );
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            Log.error("Database Exception, Disabling plugin. Message: "+e.getMessage());
            McnsaNotes.isEnabled = false;
        }
    }





}