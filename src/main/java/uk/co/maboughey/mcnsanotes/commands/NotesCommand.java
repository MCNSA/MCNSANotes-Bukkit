package uk.co.maboughey.mcnsanotes.commands;

import com.google.common.math.IntMath;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.maboughey.mcnsanotes.database.DBnote;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.type.Note;
import uk.co.maboughey.mcnsanotes.utils.Colour;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class NotesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        int page = 1;

        //Have we got permission
        if (!commandSender.hasPermission("mcnsanotes.mod")) {
            commandSender.sendMessage(Colour.colour("&4You do not have the required permissions"));
            return true;
        }

        //Have we got enough arguments?
        if (args.length < 1)
            return false;

        //Get the TargetPlayer uuid
        UUID target = DBuuid.getUuid(args[0]);
        //Sanity check
        if (target == null) {
            commandSender.sendMessage(Colour.colour("&4Could not find player \""+args[0]+"\""));
            return true;
        }

        //Do we have a page?
        if (args.length > 1) {
            if (args[1].matches("\\d+"))
                page = Integer.parseInt(args[1]);
        }

        //Previous usernames
        List<String> usernames = DBuuid.previousUsernames(target);
        Messages.sendMessage(commandSender, "&6-----Previous usernames-----");

        StringBuilder sbOutput = new StringBuilder();
        if (usernames.size() > 0){
            for (String username: usernames) {
                sbOutput.append(username+", ");
            }
        }
        Messages.sendMessage(commandSender, "&f"+sbOutput.toString());

        LinkedList<Note> notes = DBnote.getNotes(target, page);

        //Display note
        Messages.sendMessage(commandSender,
                "&6Viewing page &F" + page + "/"+ getNumPages(target.toString()) +"&6 of &F" + DBuuid.getUsername(target) + "'s&6 notes");

        //Check if there are notes
        if (notes.size() > 0){
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                Messages.sendMessage(commandSender, "&6ID: &F"+note.id + " &6Server: &F"+note.server +" &6By: &F"+note.getNoteTaker()+" &6Date: &F"+note.noteDate);
                Messages.sendMessage(commandSender, "&6Note: &F"+note.note);
            }

        }
        else {
            Messages.sendMessage(commandSender, "There are no notes");
        }
        return true;
    }
    public int getNumPages(String uuid) {
        int pages = 1;
        //Get amount of notes
        int count = DBnote.getNotesCount(uuid);
        //See how many pages that is
        pages = IntMath.divide(count, 5, RoundingMode.CEILING);

        return pages;
    }
}
