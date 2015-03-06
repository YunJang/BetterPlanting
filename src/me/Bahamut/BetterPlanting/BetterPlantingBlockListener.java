package me.Bahamut.BetterPlanting;

/**
 * Created by Yun on 3/5/2015.
 */

import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BetterPlantingBlockListener implements Listener
{
    public BetterPlantingPlugin plugin;
    public BetterPlantingLogger log;

    // Now we have an instance of the plugin that is running.
    public BetterPlantingBlockListener (BetterPlantingPlugin instance)
    {
        this.plugin = instance;
        this.log = new BetterPlantingLogger(instance);
    }

    @EventHandler
    public void onBlockPlace (BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        int radius = checkHoesInventory(player);
        if (radius <= 0) return;

        // Get the block that the player is invoking the event with.
        Block block = event.getBlock();

        Material crop = block.getType();
        Material seed;
        if      (crop == Material.CROPS)        seed = Material.SEEDS;
        else if (crop == Material.CARROT)       seed = Material.CARROT;
        else if (crop == Material.POTATO)       seed = Material.POTATO;
        else if (crop == Material.NETHER_WARTS) seed = Material.NETHER_WARTS;
        else                                    return;

        // Check player's inventory and plant based on tool's radius.
        int totalSeedsRemaining = getTotalItemStack (player, seed);
        if (player.getInventory().contains(seed))
        {
            int numSeeds = 1;
            plantingIteration:
            for (int x = (int) -Math.floor(radius/2.0); x < (int) Math.ceil(radius/2.0); ++x)
            {
                for (int z = (int) -Math.floor(radius/2.0); z < (int) Math.ceil(radius/2.0); ++z)
                {
                    Block soilBlocks = block.getRelative(x, -1, z);
                    Block airBlocks = block.getRelative(x, 0, z);
                    if (soilBlocks.getType() == Material.SOIL && airBlocks.getType() == Material.AIR)
                    {
                        airBlocks.setType(crop);
                        ++numSeeds;
                    }
                    if (numSeeds >= totalSeedsRemaining) break plantingIteration;
                }
            }
            removeSeeds (player, seed, numSeeds);
        }
    }

    /*
        Goes through the player's inventory and totals up how many seeds they have.
        Returns the total number of seeds.
     */
    public int getTotalItemStack (Player player, Material seed)
    {
        int totalSeeds = 0;
        ItemStack[] inventorySlot = player.getInventory().getContents();
        for (int x = 0; x < inventorySlot.length; ++x)
            if (inventorySlot[x] != null && inventorySlot[x].getType() == seed)
                totalSeeds += inventorySlot[x].getAmount();
        return totalSeeds;
    }

    public void removeSeeds (Player player, Material seed, int numSeeds)
    {
        int counterSeeds = numSeeds;
        while (counterSeeds > 0)
        {
            // Check to see if you have enough ItemStacks of the material.
            int itemIndex = player.getInventory().first(seed);
            if (itemIndex < 0) return;

            ItemStack itemStack = player.getInventory().getItem(itemIndex);
            int itemStackSize = itemStack.getAmount();

            // Ternary the deduction of the item stack size. Not a good idea to set the item stack size to negative.
            int newItemStackSize = (itemStackSize - counterSeeds > 0) ? itemStackSize - counterSeeds : 0;
            counterSeeds -= itemStackSize;
            itemStack.setAmount(newItemStackSize);
            player.getInventory().setItem(itemIndex, itemStack);
            player.updateInventory();
        }
    }

    /*
        Check if the player has an Iron, Golden, or Diamond Hoe.
        Returns the radius depending on the material-level of the hoe.
        If not, then return 0 and the onBlockPlace event will be ignored.
     */
    public int checkHoesInventory (Player player)
    {
        Inventory playerInventory = player.getInventory();
        if (playerInventory.contains(Material.DIAMOND_HOE))     return 9;
        else if (playerInventory.contains(Material.GOLD_HOE))   return 6;
        else if (playerInventory.contains(Material.IRON_HOE))   return 3;
        else                                                    return 0;
    }
}
