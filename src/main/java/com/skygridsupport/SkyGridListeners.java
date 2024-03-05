package com.skygridsupport;

import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.util.Objects;
import java.util.Random;

public class SkyGridListeners implements Listener {

    Random rand = new Random();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void brush(PlayerInteractEvent event) {

        if (event.getClickedBlock() == null) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.BRUSH) return;

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        // GriefPrevention Support
        if (SkyGridSupport.gpLoaded) {
            if (GriefPrevention.instance.claimsEnabledForWorld(block.getWorld())) {
                String allowBuild = GriefPrevention.instance.allowBuild(player,block.getLocation());
                if (allowBuild != null) {
                    player.sendMessage("No permission.");
                    return;
                }
            }
        }

        // WorldGuard Support
        if (SkyGridSupport.wgLoaded) {
            RegionQuery query =  WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            com.sk89q.worldedit.util.Location loc = new com.sk89q.worldedit.util.Location(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(block.getWorld()), block.getX(), block.getY(), block.getZ());

            if (!WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(block.getWorld()))) {
                if (!query.testState(loc, localPlayer, Flags.BLOCK_BREAK)) {
                    return;
                }
            }
        }

        int r = rand.nextInt(100);

        if (block.getType() == Material.SUSPICIOUS_GRAVEL) {

            player.playSound(player.getLocation(), Sound.ITEM_BRUSH_BRUSHING_GRAVEL_COMPLETE, 1, 1);
            block.setType(Material.GRAVEL, true);

            if (r < 50) dropBrushLoot(event, LootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY.getLootTable());
            else if (r < 90) dropBrushLoot(event, LootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON.getLootTable());
            else dropBrushLoot(event, LootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE.getLootTable());
        }
        else if (block.getType() == Material.SUSPICIOUS_SAND) {

            player.playSound(player.getLocation(), Sound.ITEM_BRUSH_BRUSHING_SAND_COMPLETE, 1, 1);
            block.setType(Material.SAND, true);

            if (r < 33) dropBrushLoot(event, LootTables.DESERT_WELL_ARCHAEOLOGY.getLootTable());
            else if (r < 66) dropBrushLoot(event, LootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY.getLootTable());
            else dropBrushLoot(event, LootTables.DESERT_PYRAMID_ARCHAEOLOGY.getLootTable());
        }
    }

    void dropBrushLoot(PlayerInteractEvent e, LootTable lootTable) {
        LootContext.Builder builder = new LootContext.Builder(e.getPlayer().getLocation());
        LootContext lootContext = builder.build();

        for (ItemStack item : lootTable.populateLoot(new Random(), lootContext)) {
            Location l = Objects.requireNonNull(e.getClickedBlock()).getLocation();
            l.add(0.5,1.4,0.5);
            Item i = e.getClickedBlock().getWorld().dropItem(l,item);
            i.setPickupDelay(0);
        }
    }
}
