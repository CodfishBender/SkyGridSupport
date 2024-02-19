package com.skygridsupport;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class SkyGridSupport extends JavaPlugin {

    public static SkyGridSupport instance;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new SkyGridListeners(), this);
        getCommand("gridrtp").setExecutor(new SkyGridRTPCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void Log(String s) {
        instance.getLogger().log(Level.INFO,s);
    }
}
