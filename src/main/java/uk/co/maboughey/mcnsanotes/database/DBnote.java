package uk.co.maboughey.mcnsanotes.database;

import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.type.Note;
import uk.co.maboughey.mcnsanotes.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.UUID;

public class DBnote {

    public static boolean writeNote(Note note) {
        boolean ret = true;
        try {
            // load the database
            Connection connect = DatabaseManager.getConnection();

            // write the statement
            PreparedStatement preparedStatement = connect.prepareStatement("insert into notes (id, note, server, date, uuid, noter_uuid) values (NULL, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, note.note);
            preparedStatement.setString(2, note.server);
            preparedStatement.setDate(3, new java.sql.Date((new java.util.Date()).getTime()));
            preparedStatement.setString(4, note.notee);
            preparedStatement.setString(5, note.noteTaker);


            // and execute it!
            preparedStatement.executeUpdate();

        }
        catch(Exception e) {
            Log.error("Database error writing note: "+e.getMessage());
            ret = false;
        }
        return ret;
    }
    public static LinkedList<Note> getNotes(UUID target, int page) {
        // begin storing the results
        LinkedList<Note> notes = new LinkedList<Note>();

        //Work out our limits
        if (page < 1)
            page = 1;

        int offset = (page - 1) * 5;
        try {
            // load the database
            Connection connect = DatabaseManager.getConnection();

            // get the results
            PreparedStatement statement = connect.prepareStatement("SELECT * FROM notes WHERE uuid=? ORDER BY id DESC LIMIT ?, 5");
            statement.setString(1, target.toString());
            statement.setInt(2, offset);
            ResultSet results = statement.executeQuery();

            //Now we have the results, lets loop through and create notes
            while (results.next()) {
                Note note = new Note();
                note.id = results.getInt("id");
                note.note = results.getString("note");
                note.server = results.getString("server");
                note.noteDate = results.getDate("date");

                //Check if uuids are set
                if (results.getString("uuid") != null)
                    note.notee = results.getString("uuid");
                if (results.getString("noter_uuid") != null)
                    note.noteTaker = results.getString("noter_uuid");

                notes.add(note);
            }
        }
        catch(Exception e) {
            Log.error("Database Error getting notes: "+e.getLocalizedMessage());
        }
        return notes;
    }
    public static boolean deleteNote(int id) {
        try {
            Connection connect = DatabaseManager.getConnection();
            PreparedStatement statement = connect.prepareStatement("DELETE FROM notes WHERE id=?");
            statement.setInt(1, id);

            if (statement.executeUpdate() > 0)
                return true;
            else
                return false;
        }
        catch (SQLException e) {
            Log.error("Database Error when deleting note: " + e.getLocalizedMessage());
            return false;
        }
    }
    public static int getNotesCount(String uuid) {
        int count = 0;
        try {
            //Get connection
            Connection connect = DatabaseManager.getConnection();

            //Build query
            PreparedStatement statement = connect.prepareStatement("SELECT COUNT(id) FROM notes WHERE uuid=?");
            statement.setString(1, uuid);

            ResultSet results = statement.executeQuery();
            if (results.next())
                count = results.getInt("COUNT(id)");
        }
        catch (SQLException e) {
            Log.error("Database Error fetching notes count: "+e.getLocalizedMessage());
        }
        return count;
    }
    public static LinkedList<Note> getRecentNotes(int page) {
        // begin storing the results
        LinkedList<Note> notes = new LinkedList<Note>();

        //Work out our limits
        if (page < 1)
            page = 1;

        int offset = (page - 1) * 5;
        try {
            // load the database
            Connection connect = DatabaseManager.getConnection();

            // get the results
            PreparedStatement statement = connect.prepareStatement("SELECT * FROM notes ORDER BY id DESC LIMIT ?, 5");
            statement.setInt(1, offset);
            ResultSet results = statement.executeQuery();

            //Now we have the results, lets loop through and create notes
            while (results.next()) {
                Note note = new Note();
                note.id = results.getInt("id");
                note.note = results.getString("note");
                note.server = results.getString("server");
                note.noteDate = results.getDate("date");

                //Check if uuids are set
                if (results.getString("uuid") != null)
                    note.notee = results.getString("uuid");
                if (results.getString("noter_uuid") != null)
                    note.noteTaker = results.getString("noter_uuid");

                notes.add(note);
            }
        }
        catch(Exception e) {
            Log.error("Database Error getting notes: "+e.getLocalizedMessage());
        }
        return notes;
    }
    public static int getNotesCount() {
        int count = 0;
        try {
            //Get connection
            Connection connect = DatabaseManager.getConnection();

            //Build query
            PreparedStatement statement = connect.prepareStatement("SELECT COUNT(id) FROM notes");

            ResultSet results = statement.executeQuery();
            if (results.next())
                count = results.getInt("COUNT(id)");
        }
        catch (SQLException e) {
            Log.error("Database Error fetching notes count: "+e.getLocalizedMessage());
        }
        return count;
    }
}
