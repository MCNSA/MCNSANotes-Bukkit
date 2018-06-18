package uk.co.maboughey.mcnsanotes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBstat;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.stats.StatsManager;
import uk.co.maboughey.mcnsanotes.type.Stat;
import uk.co.maboughey.mcnsanotes.utils.Colour;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.util.UUID;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        UUID target = null;

        //Are we looking at the command senders stats, or another player?
        if (args.length > 0 && sender.hasPermission("mcnsanotes.mod")) {
            //looking at another player's stats and have the permission to do so

            //Get the TargetPlayer uuid
            target = DBuuid.getUuid(args[0]);
            //Sanity check
            if (target == null) {
                sender.sendMessage(Colour.colour("&4Could not find player \""+args[0]+"\""));
                return true;
            }
        }
        else {
            if (!(sender instanceof Player)) {
                //Its console
                Messages.sendMessage(sender, "&4Please specify a player");
                return true;
            }
            target = ((Player) sender).getUniqueId();
        }

        //Lets get the stats from currently loaded stats
        Stat stat = McnsaNotes.StatsManager.getStat(target.toString());

        if (stat == null) {
            //Not loaded, load from Database
            stat = DBstat.getStat(target);

            //Do we have the stats now?
            if (stat == null) {
                //we don't
                Messages.sendMessage(sender, "&4This player does not have stats");
                return true;
            }
        }

        //We can assume we have the stats now..
        Messages.sendMessage(sender,"&6------------Stats for &F" + stat.getName() +"&6------------");
        Messages.sendMessage(sender,"&6Date first joined: &F" + stat.dateJoined + "&6  Logins: &F" + stat.numJoins);
        Messages.sendMessage(sender,"&6Time online: &F" +stat.getTimeOnServer());
        Messages.sendMessage(sender,"&6Times kicked: &F" + stat.numKicks);
        Messages.sendMessage(sender,"&6Times died: &F"+ stat.numDeaths);
        Messages.sendMessage(sender,"&6Blocks placed: &F" + stat.blocksPlaced + " &6Blocks broken: &F"+ stat.blocksBroken);


        return true;
    }
}
