package uk.co.maboughey.mcnsanotes.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import uk.co.maboughey.mcnsanotes.database.DBtagged;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.type.Tag;
import uk.co.maboughey.mcnsanotes.utils.Colour;
import uk.co.maboughey.mcnsanotes.utils.Log;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.util.UUID;

public class FlagCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Have we got enough arguments?
        if (args.length < 2)
            return false;

        //Get the TargetPlayer uuid
        UUID target = DBuuid.getUuid(args[0]);
        //Sanity check
        if (target == null) {
            sender.sendMessage(Colour.colour("&4Could not find player \""+args[0]+"\""));
            return true;
        }

        //get the uuid for the command sender
        String NoterUUID = null;

        //If player
        if (sender instanceof Player)
            NoterUUID = ((Player) sender).getUniqueId().toString();

        //If console
        if (sender instanceof ConsoleCommandSender)
            NoterUUID = "Console";

            //If Command block
        else if (sender instanceof BlockCommandSender)
            NoterUUID = "Command Block";

        //Get the tag reason
        StringBuilder SBtag = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            SBtag.append(args[i]+" ");
        }
        String tagString = SBtag.toString();

        Tag tag = new Tag();
        tag.taggerUUID = NoterUUID;
        tag.taggedUUID = target.toString();
        tag.reason = tagString;

        if (DBtagged.addTag(tag)) {
            //Tell the player
            if (sender instanceof Player)
                sender.sendMessage(Colour.colour("&ANote for "+tag.getTagged()+" has been recorded"));

            //If Console or command block, send output to console
            if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender)
                Log.info(Colour.colour("&AFlag for "+tag.getTagged()+" has been recorded"));
            return true;
        }
        else {
            //Tell the player
            if (sender instanceof Player)
                sender.sendMessage(Colour.colour("&cThere was an issue recording that flag, Check the console for info"));

            //If Console or command block, send output to console
            if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender)
                Log.warn(Colour.colour("&cThere was an issue recording that flag"));
            return true;
        }

    }
}
