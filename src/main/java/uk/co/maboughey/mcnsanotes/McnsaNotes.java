package uk.co.maboughey.mcnsanotes;

import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.material.Crops;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.maboughey.mcnsanotes.commands.*;
import uk.co.maboughey.mcnsanotes.database.DatabaseManager;
import uk.co.maboughey.mcnsanotes.listener.PlayerListener;
import uk.co.maboughey.mcnsanotes.stats.StatsManager;
import uk.co.maboughey.mcnsanotes.tabcomplete.NotesTabComplete;
import uk.co.maboughey.mcnsanotes.utils.Configuration;
import uk.co.maboughey.mcnsanotes.utils.Log;

public class McnsaNotes extends JavaPlugin {
    public static Boolean isEnabled = true;

    private String name = "MCNSANotes";
    private String version = "v2.0";

    public static Configuration config;
    public DatabaseManager DBManager;
    public PlayerListener listener;
    public static StatsManager StatsManager;


    @Override
    public void onEnable() {

        Log.info("Loading "+name+" "+version);

        saveDefaultConfig();
        config = new Configuration(getConfig(), this);
        config.load();

        Log.info("Loaded Configuration");

        DBManager = new DatabaseManager();
        DBManager.connect();

        //Register commands
        this.getCommand("note").setExecutor(new NoteCommand());
        this.getCommand("notes").setExecutor(new NotesCommand());
        this.getCommand("deletenote").setExecutor(new DeleteCommand());
        this.getCommand("notesreload").setExecutor(new ReloadCommand());
        this.getCommand("recentnotes").setExecutor(new RecentCommand());
        this.getCommand("stats").setExecutor(new StatsCommand());
        this.getCommand("flag").setExecutor(new FlagCommand());
        this.getCommand("viewflags").setExecutor(new AllFlagsCommand());
        this.getCommand("deleteflags").setExecutor(new DeleteFlagsCommand());

        //Tab complete
        this.getCommand("note").setTabCompleter(new NotesTabComplete());
        this.getCommand("notes").setTabCompleter(new NotesTabComplete());
        this.getCommand("stats").setTabCompleter(new NotesTabComplete());
        this.getCommand("flag").setTabCompleter(new NotesTabComplete());
        this.getCommand("deleteflags").setTabCompleter(new NotesTabComplete());

        StatsManager = new StatsManager();
        
        this.listener = new PlayerListener(this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                StatsManager.saveChanged();
            }
        }, 6000L, 6000L);

    }

    @Override
    public void onDisable() {
        Log.info("Saving stats");
        StatsManager.saveChanged();
    }

}
