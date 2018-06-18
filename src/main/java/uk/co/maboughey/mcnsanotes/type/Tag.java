package uk.co.maboughey.mcnsanotes.type;

import uk.co.maboughey.mcnsanotes.database.DBuuid;

import java.util.UUID;

public class Tag {
    public int id;
    public String taggedUUID;
    public String taggerUUID;
    public String reason;

    public String getTagger() {
        if (taggerUUID == null || taggerUUID.toLowerCase().startsWith("console") )
            return "Console";
        else if (taggerUUID.startsWith("Command Block"))
            return "Command Block";

        //Catch any unexpected errors
        try {
            UUID uuid = UUID.fromString(taggerUUID);
            return DBuuid.getUsername(UUID.fromString(taggerUUID));
        }
        catch (IllegalArgumentException e) {
            return taggerUUID;
        }
    }

    public String getTagged() {
        try {
            UUID uuid = UUID.fromString(taggedUUID);
            return DBuuid.getUsername(UUID.fromString(taggedUUID));
        }
        catch (IllegalArgumentException e) {
            return taggedUUID;
        }
    }
}
