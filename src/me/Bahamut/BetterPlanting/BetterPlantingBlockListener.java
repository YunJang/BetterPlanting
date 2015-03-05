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
        // Some people might not even like mod and enjoy planting 1x1. A config toggle might be convenient.

        // Get the player invoking the event.
        Player player = event.getPlayer();

        // Get the block that the player is invoking the event with.
        Block block = event.getBlock();

        Material crop = block.getType();
        Material seed;
        if      (crop == Material.CROPS)        seed = Material.SEEDS;
        else if (crop == Material.CARROT)       seed = Material.CARROT;
        else if (crop == Material.POTATO)       seed = Material.POTATO;
        else if (crop == Material.NETHER_WARTS) seed = Material.NETHER_WARTS;
        else                                    return;

        // Check player's inventory and plant in a 3x3 if they have enough.
        if (player.getInventory().contains(seed, 8))
        {
            int numSeeds = 1;

            // This needs to be reworked to be modular.
            for (int x = -1; x < 2; ++x)
            {
                for (int z = -1; z < 2; ++z)
                {
                    // Since we are planting at the (x, z) coordinate already anyway.
                    if (x != 0 || z != 0)
                    {
                        Block soilBlocks = block.getRelative(x, -1, z);
                        Block airBlocks = block.getRelative(x, 0, z);
                        if (soilBlocks.getType() == Material.SOIL && airBlocks.getType() == Material.AIR)
                        {
                            airBlocks.setType(crop);
                            ++numSeeds;
                        }
                    }
                }
            }
            removeSeeds (player, seed, numSeeds);
        }
    }

    public void removeSeeds (Player player, Material seed, int numSeeds)
    {
        int itemIndex = player.getInventory().first(seed);
        int itemStackSize = player.getInventory().getItem(itemIndex).getAmount();
        ItemStack itemStack = player.getInventory().getItem(itemIndex);

        // Ternary the deduction of the item stack size. Not a good idea to set the item stack size to negative.
        int newItemStackSize = (itemStackSize - numSeeds > 0) ? itemStackSize - numSeeds : 0;
        itemStack.setAmount(newItemStackSize);
        player.getInventory().setItem(itemIndex, itemStack);
        player.updateInventory();
    }
}
