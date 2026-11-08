package hex.wald;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PluginDescriptionTest {
    @Test
    void pointsToThePluginClass() throws Exception {
        try (var file = ExtinguishingCampfires.class.getResourceAsStream("/plugin.yml")) {
            var description = new PluginDescriptionFile(file);
            assertEquals(ExtinguishingCampfires.class.getName(), description.getMain());
        }
    }
}
