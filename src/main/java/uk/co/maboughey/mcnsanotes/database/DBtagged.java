package uk.co.maboughey.mcnsanotes.database;

import uk.co.maboughey.mcnsanotes.type.Tag;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBtagged {

    public static Tag getTag(String uuid) {

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM taggedplayers WHERE uuid=? ORDER BY id DESC LIMIT 1");
            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                Tag tag = new Tag();
                tag.id = results.getInt("id");
                tag.taggedUUID = results.getString("uuid");
                tag.taggerUUID = results.getString("noter_uuid");
                tag.reason = results.getString("reason");
                return tag;
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            Log.error("Error fetching tag: "+e.getMessage());
        }
        return null;
    }

    public static Boolean addTag(Tag tag){
        try {
            // load the database
            Connection connect = DatabaseManager.getConnection();

            // write the statement
            PreparedStatement preparedStatement = connect.prepareStatement("insert into taggedplayers (uuid, noter_uuid, reason) values (?, ?, ?)");
            preparedStatement.setString(1, tag.taggedUUID);
            preparedStatement.setString(2, tag.taggerUUID);
            preparedStatement.setString(3, tag.reason);


            // and execute it!
            preparedStatement.executeUpdate();

            return true;

        }
        catch(Exception e) {
            Log.error("Database error adding tag: "+e.getMessage());
            return false;
        }
    }

    public static Boolean deleteTags(String uuid) {
        try {
            Connection connect = DatabaseManager.getConnection();
            PreparedStatement statement = connect.prepareStatement("DELETE FROM taggedplayers WHERE uuid=?");
            statement.setString(1, uuid);

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            Log.error("Database Error when deleting tags: " + e.getLocalizedMessage());
            return false;
        }
    }

    public static List<Tag> getTags(int page) {
        List<Tag> output = new ArrayList<Tag>();

        //Work out our limits
        if (page < 1)
            page = 1;

        int offset = (page - 1) * 5;

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM taggedplayers ORDER BY id DESC LIMIT ?, 5");
            statement.setInt(1, offset);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                Tag tag = new Tag();
                tag.id = results.getInt("id");
                tag.taggedUUID = results.getString("uuid");
                tag.taggerUUID = results.getString("noter_uuid");
                tag.reason = results.getString("reason");
                output.add(tag);
            }
        }
        catch (SQLException e) {
            Log.error("Error fetching tag: "+e.getMessage());
        }
        return output;
    }

    public static int getTaggedCount() {
        int count = 0;
        try {
            //Get connection
            Connection connect = DatabaseManager.getConnection();

            //Build query
            PreparedStatement statement = connect.prepareStatement("SELECT COUNT(id) FROM taggedplayers");

            ResultSet results = statement.executeQuery();
            if (results.next())
                count = results.getInt("COUNT(id)");
        }
        catch (SQLException e) {
            Log.error("Database Error fetching flagged count: "+e.getLocalizedMessage());
        }
        return count;
    }
}
