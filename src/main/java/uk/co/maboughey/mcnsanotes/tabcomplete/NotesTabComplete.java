package uk.co.maboughey.mcnsanotes.tabcomplete;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import uk.co.maboughey.mcnsanotes.database.DBuuid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotesTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> output = new ArrayList<String>();

        if (args.length == 1) {
            for (Player player: Bukkit.getOnlinePlayers()) {
                if (!output.contains(player.getName()) && player.getName().toLowerCase().startsWith(args[0])) {
                    output.add(player.getName());
                }
            }

            List<String> other = DBuuid.getTabComplete(args[0]);
            if (other.size() > 0) {
                for (String oUsername: other) {
                    if (!output.contains(oUsername)) {
                        output.add(oUsername);
                    }
                }
            }
        }

        return output;
    }
}
