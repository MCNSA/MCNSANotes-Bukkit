package uk.co.maboughey.mcnsanotes.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import uk.co.maboughey.mcnsanotes.database.DBnote;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.type.Note;
import uk.co.maboughey.mcnsanotes.utils.Colour;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Log;

import java.util.UUID;

public class NoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        //Have we got permission
        if (!commandSender.hasPermission("mcnsanotes.mod")) {
            commandSender.sendMessage(Colour.colour("&4You do not have the required permissions"));
            return true;
        }

        //Have we got enough arguments?
        if (args.length < 2)
            return false;

        //Get the TargetPlayer uuid
        UUID target = DBuuid.getUuid(args[0]);
        //Sanity check
        if (target == null) {
            commandSender.sendMessage(Colour.colour("&4Could not find player \""+args[0]+"\""));
            return true;
        }

        //get the uuid for the command sender
        String NoterUUID = null;

        //If player
        if (commandSender instanceof Player)
            NoterUUID = ((Player) commandSender).getUniqueId().toString();

        //If console
        if (commandSender instanceof ConsoleCommandSender)
            NoterUUID = "Console";

        //If Command block
        else if (commandSender instanceof BlockCommandSender)
            NoterUUID = "Command Block";

        //Get the note
        StringBuilder SBnote = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            SBnote.append(args[i]+" ");
        }
        String NoteString = SBnote.toString();

        //Lets create the note
        Note note = new Note();

        //set uuid of the person setting the note
        note.noteTaker = NoterUUID;
        //Set notee uuid
        note.notee = target.toString();
        //Server name
        note.server = Configuration.ServerName;
        //Set note
        note.note = NoteString;

        //Save the note
        DBnote.writeNote(note);

        //Tell the player
        if (commandSender instanceof Player)
            commandSender.sendMessage(Colour.colour("&ANote for "+DBuuid.getUsername(UUID.fromString(note.notee))+" has been recorded"));

        //If Console or command block, send output to console
        if (commandSender instanceof ConsoleCommandSender || commandSender instanceof BlockCommandSender)
            Log.info(Colour.colour("&ANote for "+DBuuid.getUsername(UUID.fromString(note.notee))+" has been recorded"));

        return true;
    }
}
