package uk.co.maboughey.mcnsanotes.utils;

import org.bukkit.Bukkit;

public class Log {

    public Log() {
    }
    public static void info(String message){
        Bukkit.getConsoleSender().sendMessage(Colour.colour("&A[MCNSA Notes] "+message));
    }
    public static void warn(String message){
        Bukkit.getConsoleSender().sendMessage(Colour.colour("&5[MCNSA Notes] "+message));
    }
    public static void error(String message){
        Bukkit.getConsoleSender().sendMessage(Colour.colour("&C[MCNSA Notes] "+message));
    }
}

