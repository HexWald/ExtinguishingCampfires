package hex.wald;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class ExtinguishingCampfires extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        int interval = getConfig().getInt("check-interval-ticks", 100);
        RainCheck check = new RainCheck(getConfig().getInt("radius", 10),
                getConfig().getDouble("chance", 0.15), new Random()::nextDouble);
        getServer().getScheduler().runTaskTimer(this, () -> check.run(getServer().getOnlinePlayers()),
                interval, interval);
    }
}
