package uk.co.maboughey.mcnsanotes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBnote;
import uk.co.maboughey.mcnsanotes.utils.Log;
import uk.co.maboughey.mcnsanotes.utils.Messages;

public class DeleteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!McnsaNotes.isEnabled) {
            //It isnt, tell the user
            Messages.sendMessage(commandSender,"&4Notes plugin is currently disabled. Please check config file and reload");
            Log.warn("Notes is currently disabled. Please check config file and reload");
            return true;
        }
        else {
            //Run the command

            //Get the id
            if (strings.length < 1)
                return false;

            int id = 0;
            if (strings[0].matches("\\d+"))
                id = Integer.parseInt(strings[0]);

            if (DBnote.deleteNote(id)){
                Messages.sendMessage(commandSender, "&6Note Deleted");
            }
            else {
                Messages.sendMessage(commandSender, "&4Error deleting note (Are you sure you put in the correct id?)");
            }
        }

        return true;
    }
}
