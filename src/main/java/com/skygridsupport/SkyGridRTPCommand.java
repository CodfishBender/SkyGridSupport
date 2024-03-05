package com.skygridsupport;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.logging.Level;

public class SkyGridRTPCommand implements CommandExecutor {

    Random r = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission check
        if (!sender.hasPermission("SkyGridSupport.RTP")) {
            sender.sendMessage("No permission");
            return false;
        }

        // Get world
        if (args.length == 0) {
            SkyGridSupport.Log("Specify a world to RTP.");
            return false;
        }
        // Validate world
        World w = SkyGridSupport.instance.getServer().getWorld(args[0]);
        if (w == null) {
            SkyGridSupport.Log("Cannot find valid world.");
            return false;
        }

        // Make sure player is specified
        if (sender instanceof ConsoleCommandSender && args.length == 1) {
            SkyGridSupport.Log("Specify a player to RTP.");
            return false;
        }
        // Set target player as argument or sender
        Player p = (args.length > 1) ? SkyGridSupport.instance.getServer().getPlayer(args[1]) : (Player)sender;
        if (p == null) {
            SkyGridSupport.Log(Level.SEVERE, "Could not find player name " + args[1]);
            return false;
        }

        // Try max of 10 times
        for (int i = 0; i < 10; i++) {
            if (rtp(w, p)) {
                SkyGridSupport.Log("RTP'd " + p.getName() + " in "+ i +" attempts.");
                break;
            }
        }

        return true;

    }

    boolean rtp(World w, Player p) {

        int size = (int) w.getWorldBorder().getSize();

        // y = 124 is top block height for grid generation
        Location newLoc = new Location(w, -size/2f + r.nextInt(size),124, -size/2f + r.nextInt(size));

        search: {
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    Location searchLoc = newLoc.add(x, 0, y);
                    if (tryLocation(searchLoc)) {
                        newLoc = searchLoc;
                        break search;
                    }
                }
            }
        }

        if (!tryLocation(newLoc)) {
            SkyGridSupport.Log("Couldn't find valid location - try again");
            return false;
        }
        newLoc.add(0,2,0);

        p.teleport(newLoc);
        return true;
    }

    boolean tryLocation(Location loc) {
        return loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != Material.LAVA;
    }
}
