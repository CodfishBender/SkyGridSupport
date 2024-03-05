package com.skygridsupport;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class SkyGridSupport extends JavaPlugin {

    public static SkyGridSupport instance;
    public static boolean gpLoaded;
    public static boolean wgLoaded;

    @Override
    public void onEnable() {
        instance = this;
        gpLoaded = Bukkit.getPluginManager().isPluginEnabled("GriefPrevention");
        if (gpLoaded) Log("GriefPrevention support loaded.");
        wgLoaded = Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
        if (wgLoaded) Log("WorldGuard support loaded.");

        Bukkit.getPluginManager().registerEvents(new SkyGridListeners(), this);
        getCommand("gridrtp").setExecutor(new SkyGridRTPCommand());

    }

    public static void Log(String s) {
        instance.getLogger().log(Level.INFO,s);
    }
    public static void Log(Level l, String s) {
        instance.getLogger().log(l,s);
    }
}
