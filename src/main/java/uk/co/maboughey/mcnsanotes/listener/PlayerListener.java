package uk.co.maboughey.mcnsanotes.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.maboughey.mcnsanotes.McnsaNotes;
import uk.co.maboughey.mcnsanotes.database.DBstat;
import uk.co.maboughey.mcnsanotes.database.DBtagged;
import uk.co.maboughey.mcnsanotes.database.DBuuid;
import uk.co.maboughey.mcnsanotes.type.Stat;
import uk.co.maboughey.mcnsanotes.type.Tag;
import uk.co.maboughey.mcnsanotes.utils.Log;
import uk.co.maboughey.mcnsanotes.utils.Messages;

import java.util.UUID;

public class PlayerListener implements Listener {

    public PlayerListener(McnsaNotes plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playerLogin(PlayerLoginEvent event) {
        //Get details
        UUID uuid = event.getPlayer().getUniqueId();
        String name = event.getPlayer().getName();

        //Add the details to the database
        DBuuid.addUUID(uuid.toString(), name);

        //get player stats
        Stat stat = DBstat.getStat(uuid);
        if (stat == null) {
            Log.info("Creating new stat");
            stat = new Stat(uuid);
            DBstat.saveNewStat(stat);
        }
        //Set login time
        stat.loginTime = System.currentTimeMillis() / 1000;
        //add to number of joins
        stat.numJoins += 1;

        McnsaNotes.StatsManager.addStat(stat);

        Tag tag = DBtagged.getTag(uuid.toString());
        if (tag != null) {
            Messages.sendToMods("&4-------Flagged Player-------");
            Messages.sendToMods("&f"+event.getPlayer().getName()+"&6 has been flagged by: "+tag.getTagger());
            Messages.sendToMods("&6Reason: &f"+tag.reason);
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        McnsaNotes.StatsManager.removeStat(uuid.toString());
    }

    @EventHandler
    public void playerKick(PlayerKickEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        McnsaNotes.StatsManager.getStat(uuid).numKicks += 1;
        McnsaNotes.StatsManager.getStat(uuid).changed = true;
        McnsaNotes.StatsManager.removeStat(uuid);
    }

    @EventHandler
    public void playerBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!event.isCancelled() || player != null || !player.getName().contains("[") || player.getGameMode().equals(GameMode.SURVIVAL)) {
            String uuid = player.getUniqueId().toString();
            McnsaNotes.StatsManager.getStat(uuid).changed = true;
            McnsaNotes.StatsManager.getStat(uuid).blocksBroken += 1;
        }
    }

    @EventHandler
    public void playerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!event.isCancelled() || player != null || !player.getName().contains("[") || player.getGameMode().equals(GameMode.SURVIVAL)) {
            String uuid = player.getUniqueId().toString();
            McnsaNotes.StatsManager.getStat(uuid).changed = true;
            McnsaNotes.StatsManager.getStat(uuid).blocksPlaced += 1;
        }
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!player.getName().contains("[") || player.getGameMode().equals(GameMode.SURVIVAL)) {
            String uuid = player.getUniqueId().toString();
            McnsaNotes.StatsManager.getStat(uuid).changed = true;
            McnsaNotes.StatsManager.getStat(uuid).numDeaths += 1;
        }
    }
}
