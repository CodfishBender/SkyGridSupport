package com.skygridsupport;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
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
import java.util.Vector;

public class SkyGridListeners implements Listener {

    Random rand = new Random();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void brush(PlayerInteractEvent event) {

        if (event.getClickedBlock() == null) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.BRUSH) return;

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

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
            l.add(0,0.5,0);
            e.getClickedBlock().getWorld().dropItemNaturally(l,item);
        }
    }
}
