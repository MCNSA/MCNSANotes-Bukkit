package uk.co.maboughey.mcnsanotes.database;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import uk.co.maboughey.mcnsanotes.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DBuuid {

    public static String getUsername(UUID uuid){
        String username = null;

        //Try local first
        username = local_getUsername(uuid);

        //Get from database
        username = database_getUsername(uuid);

        return username;
    }

    public static UUID getUuid(String name) {
        UUID uuid = null;

        //Try local first
        uuid = local_getUuid(name);

        //Try database
        uuid = database_getUuid(name);

        return uuid;
    }

    public static String local_getUsername(UUID uuid){
        //See if player is on the server
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return player.getName();
        }

        //See if player has been on the server before but offline
        OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(uuid);
        if (oPlayer != null) {
            return oPlayer.getName();
        }

        //Just return null
        return null;
    }

    public static UUID local_getUuid(String name){
        UUID uuid = null;

        //Does the name match any online player
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return player.getUniqueId();
            }
        }

        //How about offline players
        for (OfflinePlayer oplayer: Bukkit.getOfflinePlayers()) {
            if (oplayer.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return oplayer.getUniqueId();
            }
        }

        //No result found
        return null;
    }

    public static String database_getUsername(UUID uuid){
        //Try getting the username from the database
        Connection connection = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM knownUsernames WHERE uuid=? ORDER BY id DESC");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getString("name");
            }
            else {
                return null;
            }

        } catch (SQLException e) {
            Log.error("Database error fetching username by uuid: "+e.getMessage());
        }

        //Just return null
        return null;
    }

    public static UUID database_getUuid(String name){
        //Try getting the username from the database
        Connection connection = DatabaseManager.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM knownUsernames WHERE name LIKE ? ORDER BY id DESC");
            statement.setString(1, name+"%");
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return UUID.fromString(results.getString("uuid"));
            }
            else {
                return null;
            }

        } catch (SQLException e) {
            Log.error("Database error fetching uuid by username: "+e.getMessage());
        }

        //Just return null
        return null;
    }

    public static void addUUID(String uuid, String player) {
        try {
            //Get the connection
            Connection connect = DatabaseManager.getConnection();

            //Check if uuid and name relationship is in the database
            PreparedStatement statement = connect.prepareStatement("SELECT * FROM knownUsernames WHERE uuid=? AND name=?");
            statement.setString(1, uuid);
            statement.setString(2, player);
            ResultSet results = statement.executeQuery();

            if (!results.next()) {
                //Not in DB, Lets add it
                PreparedStatement put = connect.prepareStatement("INSERT INTO knownUsernames (uuid, name) VALUES (?,?)");
                put.setString(1, uuid);
                put.setString(2, player);
                put.executeUpdate();
            }
        }
        catch (SQLException e) {
            Log.error("Error adding uuid into database: "+e.getLocalizedMessage());
        }
    }

    public static List<String> previousUsernames(UUID uuid) {
        List<String> output = new ArrayList<>();

        try {
            //Get the connection
            Connection connect = DatabaseManager.getConnection();

            //Check if uuid and name relationship is in the database
            PreparedStatement statement = connect.prepareStatement("SELECT * FROM knownUsernames WHERE uuid=? ORDER BY id DESC");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                if (!output.contains(results.getString("name"))) {
                    output.add(results.getString("name"));
                }
            }
        }
        catch (SQLException e) {
            Log.error("Error getting known usernames from database: "+e.getLocalizedMessage());
        }

        return output;
    }
}
