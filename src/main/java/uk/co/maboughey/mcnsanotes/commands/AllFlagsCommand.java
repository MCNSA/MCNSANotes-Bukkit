package uk.co.maboughey.mcnsanotes.commands;

import com.google.common.math.IntMath;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.maboughey.mcnsanotes.database.DBtagged;
import uk.co.maboughey.mcnsanotes.type.Tag;
import uk.co.maboughey.mcnsanotes.utils.Colour;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public class AllFlagsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int page = 1;

        //Have we got permission
        if (!sender.hasPermission("mcnsanotes.mod")) {
            sender.sendMessage(Colour.colour("&4You do not have the required permissions"));
            return true;
        }

        //Do we have a page?
        if (args.length > 0) {
            if (args[0].matches("\\d+"))
                page = Integer.parseInt(args[0]);
        }

        List<Tag> tags = DBtagged.getTags(page);

        //Display note
        Messages.sendMessage(sender,
                "&6Viewing page &F" + page + "/"+ getNumPages() +"&6 of recent flags");

        //Check if there are notes
        if (tags.size() > 0){
            for (int i = 0; i < tags.size(); i++) {
                Tag tag = tags.get(i);
                Messages.sendMessage(sender, "&6ID: &F"+tag.id +" &6On: &F"+tag.getTagged()+" &6By: &F"+tag.getTagger());
                Messages.sendMessage(sender, "&6Note: &F"+tag.reason);
            }

        }
        else {
            Messages.sendMessage(sender, "There are no flags");
        }

        return true;
    }
    public int getNumPages() {
        int pages = 1;
        //Get amount of notes
        int count = DBtagged.getTaggedCount();
        //See how many pages that is
        pages = IntMath.divide(count, 5, RoundingMode.CEILING);

        return pages;
    }
}
