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

public class SkyGridRTPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("SkyGridSupport.RTP")) {
            sender.sendMessage("No permission");
            return false;
        }
        if (args.length == 0) {
            SkyGridSupport.Log("Specify a world to RTP.");
            return false;
        }

        if (sender instanceof ConsoleCommandSender && args.length == 1) {
            SkyGridSupport.Log("Specify a player to RTP.");
            return false;
        }

        World w = SkyGridSupport.instance.getServer().getWorld(args[0]);
        if (w == null) {
            SkyGridSupport.Log("Cannot find valid world.");
            return false;
        }

        Random r = new Random();

        int size = (int) w.getWorldBorder().getSize();

        Location newLoc = new Location(w, -size/2f + r.nextInt(size),124, -size/2f + r.nextInt(size));


        search: {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
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
            if (sender instanceof Player) sender.sendMessage("Couldn't find valid location - try again");
            return false;
        }
        newLoc.add(0,2,0);

        if (args.length > 1) {
            Player p = SkyGridSupport.instance.getServer().getPlayer(args[1]);
            if (p == null) return false;
            p.teleport(newLoc);
        } else {
            ((Player)sender).teleport(newLoc);
        }
        return true;
    }

    boolean tryLocation(Location loc) {
        return loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != Material.LAVA;
    }
}
