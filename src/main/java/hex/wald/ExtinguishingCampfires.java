package hex.wald;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class ExtinguishingCampfires extends JavaPlugin {

    private final Random random = new Random();

    @Override
    public void onEnable() {

        saveDefaultConfig();

        int interval = getConfig().getInt("check-interval-ticks", 100);

        Bukkit.getScheduler().runTaskTimer(this, () -> {

            double chance = getConfig().getDouble("chance");
            int radius = getConfig().getInt("radius");

            for (Player player : Bukkit.getOnlinePlayers()) {

                if (!player.getWorld().hasStorm()) continue;

                Location loc = player.getLocation();

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {

                            Block block = loc.clone().add(x, y, z).getBlock();

                            if (block.getType() != Material.CAMPFIRE &&
                                    block.getType() != Material.SOUL_CAMPFIRE) continue;

                            if (!isExposedToSky(block)) continue;

                            if (random.nextDouble() <= chance) {

                                Campfire campfire = (Campfire) block.getBlockData();

                                if (!campfire.isLit()) continue;

                                campfire.setLit(false);
                                block.setBlockData(campfire);

                                block.getWorld().playSound(
                                        block.getLocation(),
                                        Sound.BLOCK_FIRE_EXTINGUISH,
                                        1f,
                                        1f
                                );
                            }
                        }
                    }
                }

            }

        }, interval, interval);

    }

    private boolean isExposedToSky(Block block) {

        int highest = block.getWorld().getHighestBlockYAt(block.getLocation());

        return block.getY() >= highest;
    }

}