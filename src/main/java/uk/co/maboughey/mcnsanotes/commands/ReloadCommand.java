package uk.co.maboughey.mcnsanotes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Messages;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Configuration.reload();
        Messages.sendMessage(commandSender, "Config reloaded");
        return true;
    }
}
