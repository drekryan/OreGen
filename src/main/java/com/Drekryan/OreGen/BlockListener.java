package com.Drekryan.OreGen;

import com.Drekryan.OreGen.util.ConfigManager;
import com.Drekryan.OreGen.util.LogHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.Map;

class OreDefinition {
    boolean shouldGenerate;
    Location location;

    OreDefinition(boolean shouldGenerate, Location location) {
        this.shouldGenerate = shouldGenerate;
        this.location = location;
    }
}

public class BlockListener implements Listener {
    private OreGen plugin;
    private ConfigManager configManager;
    private LogHelper logHelper;
    private Map<String, Integer> blockChances;

    BlockListener(OreGen plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.logHelper = plugin.getLogHelper();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        ConfigManager configManager = this.plugin.getConfigManager();
        configManager.loadConfig(false);
        configManager.loadBlockChances();

        this.updateBlockValues();
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block fromBlock = event.getBlock();
        Block toBlock  = event.getToBlock();

        if (configManager.isDebug())
            logHelper.info("From Block: " + fromBlock.getType().toString() + " | " + "To Block: " + toBlock.getType().toString());

        // Check if an ore should be generated
        OreDefinition oreDefinition = checkBlocks(fromBlock, toBlock);
        if (oreDefinition.shouldGenerate && oreDefinition.location != null && blockChances != null) {
            Location oreLocation = oreDefinition.location;
            event.setCancelled(true);

            if (configManager.isDebug())
                logHelper.info("Ore Location: " + oreLocation.toString());

            //TODO: Improve chance system
            int roll = (int) Math.floor(Math.random() * 100);
            String spawnBlock = "COBBLESTONE";

            if (configManager.isDebug())
                logHelper.info("Picking from " + blockChances.keySet().size() + " values...");

            for (String blockName : blockChances.keySet()) {
                int chance = blockChances.get(blockName);

                if (roll < chance) {
                    spawnBlock = blockName;
                    break;
                }
            }

            //Spawn block and play sound
            Material blockMaterial = Material.getMaterial(spawnBlock);

            if (blockMaterial == null) {
                logHelper.error("Could not find block with name '" + spawnBlock + "'! Using Cobblestone instead...");
                blockMaterial = Material.COBBLESTONE;
            }

            fromBlock.getWorld().getBlockAt(oreLocation).setType(blockMaterial);
            fromBlock.getWorld().playSound(oreLocation, Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
        }
    }

    private OreDefinition checkBlocks(Block fromBlock, Block toBlock) {
        if (fromBlock == null || toBlock == null || fromBlock.getType() != Material.STATIONARY_LAVA || toBlock.getType() != Material.AIR) {
            return new OreDefinition(false, null);
        }

        BlockFace[] blockFaces = new BlockFace[]{BlockFace.SELF, BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        int length = blockFaces.length;
        int index = 0;

        while (index < length) {
            BlockFace blockFace = blockFaces[index];
            if (configManager.isDebug())
                logHelper.info("Found (" + blockFace.toString() + " ): " + toBlock.getRelative(blockFace, 1).getType().toString());

            if (toBlock.getRelative(blockFace, 1).getType() == Material.STATIONARY_WATER) {
                return new OreDefinition(true, toBlock.getLocation());
            }

            ++index;
        }

        Block belowBlock = toBlock.getRelative(BlockFace.DOWN, 1);
        if (belowBlock.getType() == Material.STATIONARY_WATER) {
            if (configManager.isDebug())
                logHelper.info("Found Water -- Creating Stone");
            return new OreDefinition(false, null);
        }

        return new OreDefinition(false, null);
    }

    public void updateBlockValues() {
        if (configManager != null) {
            if (!configManager.isValid()) {
                logHelper.warning("Config values did not equal 100%! Reverting to vanilla generation...");
            }

            this.blockChances = configManager.getBlockChances();
        }
    }
}