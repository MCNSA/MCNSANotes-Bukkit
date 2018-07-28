package uk.co.maboughey.mcnsanotes.utils;

import org.bukkit.configuration.file.FileConfiguration;
import uk.co.maboughey.mcnsanotes.McnsaNotes;

public class Configuration {
    public static String DBHost;
    public static String DBName;
    public static String DBUser;
    public static String DBPassword;
    public static String ServerName;
    private static McnsaNotes plugin;
    FileConfiguration config = null;

    public Configuration(FileConfiguration config, McnsaNotes mcnsaNotes) {
        plugin = mcnsaNotes;
        this.config = config;
    }

    public void load() {
        DBHost = config.getString("database-host");
        DBName = config.getString("database-name");
        DBUser = config.getString("database-user");
        DBPassword = config.getString("database-password");
        ServerName = config.getString("server-name");
    }

    public static String getDatabaseString() {
        return "jdbc:mysql://"+DBHost+"/"+DBName;
    }
    public static void reload() {
        plugin.reloadConfig();
        McnsaNotes.config.load();
    }
    public static void currentConfig() {
        Log.info("Current settings: DBH:"+DBHost+", DBN:"+DBName+", DBU:"+DBUser+", DBP:"+DBPassword);
    }
}
