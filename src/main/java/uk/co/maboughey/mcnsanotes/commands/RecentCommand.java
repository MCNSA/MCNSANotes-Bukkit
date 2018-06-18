package uk.co.maboughey.mcnsanotes.commands;

import com.google.common.math.IntMath;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.maboughey.mcnsanotes.database.DBnote;
import uk.co.maboughey.mcnsanotes.type.Note;
import uk.co.maboughey.mcnsanotes.utils.Colour;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.math.RoundingMode;
import java.util.LinkedList;

public class RecentCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        int page = 1;

        //Have we got permission
        if (!commandSender.hasPermission("mcnsanotes.mod")) {
            commandSender.sendMessage(Colour.colour("&4You do not have the required permissions"));
            return true;
        }

        //Do we have a page?
        if (args.length > 0) {
            if (args[0].matches("\\d+"))
                page = Integer.parseInt(args[0]);
        }

        LinkedList<Note> notes = DBnote.getRecentNotes(page);

        //Display note
        Messages.sendMessage(commandSender,
                "&6Viewing page &F" + page + "/"+ getNumPages() +"&6 of recent notes");

        //Check if there are notes
        if (notes.size() > 0){
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                Messages.sendMessage(commandSender, "&6ID: &F"+note.id +" &6On: &F"+note.getNotee()+" &6By: &F"+note.getNoteTaker());
                Messages.sendMessage(commandSender, "&6Server: &F"+note.server+" &6Date: &F"+note.noteDate);
                Messages.sendMessage(commandSender, "&6Note: &F"+note.note);
            }

        }
        else {
            Messages.sendMessage(commandSender, "There are no notes");
        }

        return true;
    }
    public int getNumPages() {
        int pages = 1;
        //Get amount of notes
        int count = DBnote.getNotesCount();
        //See how many pages that is
        pages = IntMath.divide(count, 5, RoundingMode.CEILING);

        return pages;
    }

}
