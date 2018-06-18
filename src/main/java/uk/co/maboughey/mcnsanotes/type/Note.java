package uk.co.maboughey.mcnsanotes.type;

import uk.co.maboughey.mcnsanotes.database.DBuuid;

import java.util.Date;
import java.util.UUID;

public class Note {
    public Integer id;
    public Date noteDate;
    public String noteTaker;
    public String notee;
    public String note;
    public String server;

    public String getNoteTaker() {
        if (noteTaker == null || noteTaker.toLowerCase().startsWith("console") )
            return "Console";
        else if (noteTaker.startsWith("Command Block"))
            return "Command Block";

        //Catch any unexpected errors
        try {
            UUID uuid = UUID.fromString(noteTaker);
            return DBuuid.getUsername(UUID.fromString(noteTaker));
        }
        catch (IllegalArgumentException e) {
            return noteTaker;
        }
    }

    public String getNotee() {
        try {
            UUID uuid = UUID.fromString(notee);
            return DBuuid.getUsername(UUID.fromString(notee));
        }
        catch (IllegalArgumentException e) {
            return notee;
        }
    }

}
