package uk.co.maboughey.mcnsanotes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBnote;
import uk.co.maboughey.mcnsanotes.database.DBtagged;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.utils.Colour;
import uk.co.maboughey.mcnsanotes.utils.Log;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.util.UUID;

public class DeleteFlagsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!McnsaNotes.isEnabled) {
            //It isnt, tell the user
            Messages.sendMessage(sender,"&4Notes plugin is currently disabled. Please check config file and reload");
            Log.warn("Notes is currently disabled. Please check config file and reload");
            return true;
        }
        else {
            //Run the command

            //Check if player is specified
            if (args.length < 1)
                return false;

            //Get the TargetPlayer uuid
            UUID target = DBuuid.getUuid(args[0]);
            //Sanity check
            if (target == null) {
                sender.sendMessage(Colour.colour("&4Could not find player \""+args[0]+"\""));
                return true;
            }

            if (DBtagged.deleteTags(target.toString())) {
                Messages.sendMessage(sender, "&6Flags Deleted");
            }
            else {
                Messages.sendMessage(sender, "&4Error deleting flag (Are you sure the player was flagged?)");
            }
        }

        return true;
    }
}
