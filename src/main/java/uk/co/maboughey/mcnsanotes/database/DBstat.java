package uk.co.maboughey.mcnsanotes.database;

import uk.co.maboughey.mcnsanotes.type.Stat;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DBstat {

    public static Stat getStat(UUID uuid) {
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+Configuration.ServerName+"_stats WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                Stat stat = new Stat();
                stat.uuid = uuid;
                stat.timeOnServer = results.getLong("timeonserver");
                stat.dateJoined = results.getTimestamp("firstjoined");
                stat.blocksBroken = results.getLong("blocksbroken");
                stat.blocksPlaced = results.getLong("blocksplaced");
                stat.numDeaths = results.getLong("numdeaths");
                stat.numKicks = results.getLong("numkicks");
                stat.modRequests = results.getLong("modreqs");
                stat.numJoins = results.getLong("logins");

                return stat;
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            Log.error("Error fetching stats: "+e.getMessage());
        }
        return null;
    }

    public static void saveStat(Stat stat) {
        try{
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE "+Configuration.ServerName+"_stats SET " +
                    "timeonserver=?, " +
                    "blocksplaced=?, " +
                    "blocksbroken=?, " +
                    "numdeaths=?, " +
                    "numkicks=?, " +
                    "modreqs=?, " +
                    "logins=? " +
                    "WHERE uuid=?");
            statement.setLong(1, stat.timeOnServer);
            statement.setLong(2, stat.blocksPlaced);
            statement.setLong(3, stat.blocksBroken);
            statement.setLong(4, stat.numDeaths);
            statement.setLong(5, stat.numKicks);
            statement.setLong(6, stat.modRequests);
            statement.setLong(7, stat.numJoins);
            statement.setString(8, stat.uuid.toString());

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            Log.error("Error saving stats: "+e.getMessage());
        }
    }

    public static void saveNewStat(Stat stat) {
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + Configuration.ServerName + "_stats (timeonserver, blocksplaced, blocksbroken, numdeaths, numkicks, modreqs, uuid, logins) VALUES (0,0,0,0,0,0,?,0)");
            statement.setString(1, stat.uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Log.error("Database error saving new stat: " + e.getMessage());
        }
    }
}
