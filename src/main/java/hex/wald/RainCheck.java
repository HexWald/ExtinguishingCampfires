package hex.wald;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.function.DoubleSupplier;

final class RainCheck {
    private final int radius;
    private final double chance;
    private final DoubleSupplier roll;

    RainCheck(int radius, double chance, DoubleSupplier roll) {
        this.radius = radius;
        this.chance = chance;
        this.roll = roll;
    }

    void run(Collection<? extends Player> players) {
        for (Player player : players) {
            World world = player.getWorld();
            if (!world.hasStorm()) {
                continue;
            }

            Location location = player.getLocation();
            int minX = (location.getBlockX() - radius) >> 4;
            int maxX = (location.getBlockX() + radius) >> 4;
            int minZ = (location.getBlockZ() - radius) >> 4;
            int maxZ = (location.getBlockZ() + radius) >> 4;
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (!world.isChunkLoaded(x, z)) {
                        continue;
                    }
                    for (BlockState state : world.getChunkAt(x, z).getTileEntities()) {
                        if (state instanceof org.bukkit.block.Campfire && isNearby(state, location)) {
                            tryExtinguish(state.getBlock());
                        }
                    }
                }
            }
        }
    }

    private boolean isNearby(BlockState campfire, Location location) {
        return Math.abs(campfire.getX() - location.getBlockX()) <= radius
                && Math.abs(campfire.getY() - location.getBlockY()) <= radius
                && Math.abs(campfire.getZ() - location.getBlockZ()) <= radius;
    }

    private void tryExtinguish(Block block) {
        if (!(block.getBlockData() instanceof Campfire campfire) || !campfire.isLit()) {
            return;
        }

        World world = block.getWorld();
        if (world.getHighestBlockYAt(block.getLocation()) > block.getY() || roll.getAsDouble() >= chance) {
            return;
        }

        campfire.setLit(false);
        block.setBlockData(campfire);
        world.playSound(block.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
    }
}
