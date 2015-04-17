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
        if (plugin.PlantableCrops.contains(crop))
            seed = plugin.CropToSeed.get(crop);
        else
            return;

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
            applyDamage (player, numSeeds);
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
        for (ItemStack item : inventorySlot) {
            if (item != null && item.getType() == seed)
                totalSeeds += item.getAmount();
        }
        return totalSeeds;
    }

    /*
        Decrement the tool's durability and break it if it is past its durability limit.
     */
    public void applyDamage (Player player, int numSeeds)
    {
        int index = -1;
        ItemStack tool = null;
        Material[] hoeTools = { Material.DIAMOND_HOE, Material.GOLD_HOE, Material.IRON_HOE };
        for (Material hoe : hoeTools)
        {
            index = player.getInventory().first(hoe);
            if (index >= 0)
            {
                tool = player.getInventory().getItem(index);
                if (tool != null) break;
            }
        }

        Material toolMaterial = tool.getData().getItemType();
        int maxDurability = toolMaterial.getMaxDurability();
        tool.setDurability((short) (tool.getDurability() + (numSeeds/8)));
        int currentDurability = tool.getDurability();
        if (currentDurability >= maxDurability) player.getInventory().clear(index);
        player.updateInventory();
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

        if (playerInventory.contains(Material.DIAMOND_HOE))
            return plugin.Tools.get(Material.DIAMOND_HOE);
        else if (playerInventory.contains(Material.GOLD_HOE))
            return plugin.Tools.get(Material.GOLD_HOE);
        else if (playerInventory.contains(Material.IRON_HOE))
            return plugin.Tools.get(Material.IRON_HOE);
        else
            return 0;
    }
}
