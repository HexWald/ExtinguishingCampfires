package hex.wald;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class ExtinguishingCampfires extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        double chance = getConfig().getDouble("chance", 0.15);
        if (!Double.isFinite(chance) || chance < 0 || chance > 1) {
            getLogger().warning("chance must be between 0 and 1. Using 0.15.");
            chance = 0.15;
        }

        int radius = getConfig().getInt("radius", 10);
        if (radius < 0 || radius > 64) {
            getLogger().warning("radius must be between 0 and 64. Using 10.");
            radius = 10;
        }

        int interval = getConfig().getInt("check-interval-ticks", 100);
        if (interval < 1) {
            getLogger().warning("check-interval-ticks must be positive. Using 100.");
            interval = 100;
        }

        if (chance > 0) {
            RainCheck check = new RainCheck(radius, chance, new Random()::nextDouble);
            getServer().getScheduler().runTaskTimer(this, () -> check.run(getServer().getOnlinePlayers()),
                    interval, interval);
        }
    }
}
