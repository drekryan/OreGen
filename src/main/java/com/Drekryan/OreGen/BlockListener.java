package com.Drekryan.OreGen;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

class BlockListener implements Listener {
    private final BlockFace[] faces = new BlockFace[]{BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block block = event.getBlock();
        Block toBlock  = event.getToBlock();

        System.out.println("Got event");

        if (block.getType() != Material.STATIONARY_WATER || block.getType() != Material.WATER
                || block.getType() != Material.STATIONARY_LAVA || block.getType() != Material.LAVA
                || toBlock.getType() != Material.AIR || !generateCobble(block.getType(), toBlock)) return;

        System.out.println("Attempting to update block");
        Location location = this.getLocation(block.getType(), toBlock);
        block.getWorld().getBlockAt(location).setType(Material.DIAMOND_ORE);
        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
    }

    private boolean generateCobble(Material blockMaterial, Block toBlock) {
        BlockFace[] blockFaces = this.faces;
        int length = blockFaces.length;
        int index = 0;

        while (index < length) {
            BlockFace blockFace = blockFaces[index];
            if (toBlock.getRelative(blockFace, 1).getType() == (blockMaterial == Material.WATER ||
                blockMaterial == Material.STATIONARY_WATER ? Material.LAVA : Material.WATER) ||
                    toBlock.getRelative(blockFace, 1).getType() == (blockMaterial == Material.WATER ||
                     blockMaterial == Material.STATIONARY_WATER ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER)) {
                System.out.println("Should gen cobble....");
                return true;
            }
            ++index;
        }

        return false;
    }

    private Location getLocation(Material blockMaterial, Block toBlock) {
        Location location = null;
        BlockFace[] blockFaces = this.faces;
        int length = blockFaces.length;
        int index = 0;

        while (index < length) {
            BlockFace blockFace = blockFaces[index];
            if (toBlock.getRelative(blockFace, 1).getType() == (blockMaterial == Material.WATER ||
                    blockMaterial == Material.STATIONARY_WATER ? Material.LAVA : Material.WATER) ||
                    toBlock.getRelative(blockFace, 1).getType() == (blockMaterial == Material.WATER ||
                            blockMaterial == Material.STATIONARY_WATER ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER)) {
                location = toBlock.getLocation();
            }
            ++index;
        }

        return location;
    }
}
