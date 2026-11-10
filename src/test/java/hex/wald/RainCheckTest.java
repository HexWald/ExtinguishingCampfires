package hex.wald;

import org.bukkit.Chunk;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.DoubleSupplier;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class RainCheckTest {
    private final World world = mock(World.class);
    private final Chunk chunk = mock(Chunk.class);
    private final org.bukkit.block.Campfire state = mock(org.bukkit.block.Campfire.class);
    private final Block block = mock(Block.class);
    private final Campfire campfire = mock(Campfire.class);
    private final DoubleSupplier roll = mock(DoubleSupplier.class);
    private final RainCheck check = new RainCheck(10, 0.15, roll);
    private final Location location = new Location(world, -8, 64, -8);

    @BeforeEach
    void setUp() {
        when(world.hasStorm()).thenReturn(true);
        when(world.isChunkLoaded(-1, -1)).thenReturn(true);
        when(world.getChunkAt(-1, -1)).thenReturn(chunk);
        when(chunk.getTileEntities()).thenReturn(new BlockState[]{state});
        when(state.getX()).thenReturn(location.getBlockX());
        when(state.getY()).thenReturn(location.getBlockY());
        when(state.getZ()).thenReturn(location.getBlockZ());
        when(state.getBlock()).thenReturn(block);
        when(block.getX()).thenReturn(location.getBlockX());
        when(block.getY()).thenReturn(location.getBlockY());
        when(block.getZ()).thenReturn(location.getBlockZ());
        when(block.getLocation()).thenReturn(location);
        when(block.getWorld()).thenReturn(world);
        when(block.getBlockData()).thenReturn(campfire);
        when(campfire.isLit()).thenReturn(true);
        when(world.getHighestBlockYAt(-8, -8, HeightMap.MOTION_BLOCKING)).thenReturn(64);
        when(block.getHumidity()).thenReturn(0.8);
        when(block.getTemperature()).thenReturn(0.7);
    }

    @Test
    void extinguishesCampfireInRain() {
        check.run(List.of(playerAt(-7, 64, -7)));

        verify(campfire).setLit(false);
        verify(block).setBlockData(campfire);
        verify(world).playSound(location, "minecraft:block.fire.extinguish", 1f, 1f);
    }

    @Test
    void rollsOnceWhenTwoPlayersAreNearby() {
        when(roll.getAsDouble()).thenReturn(0.9);

        check.run(List.of(playerAt(-7, 64, -7), playerAt(-9, 64, -9)));

        verify(chunk).getTileEntities();
        verify(roll).getAsDouble();
        verify(campfire, never()).setLit(false);
    }

    @Test
    void skipsUnloadedChunks() {
        when(world.isChunkLoaded(-1, -1)).thenReturn(false);

        check.run(List.of(playerAt(-7, 64, -7)));

        verify(world, never()).getChunkAt(anyInt(), anyInt());
        verifyNoInteractions(roll);
    }

    @Test
    void skipsCampfireAbovePlayerRadius() {
        check.run(List.of(playerAt(-7, 53, -7)));

        verify(state, never()).getBlock();
        verifyNoInteractions(roll);
    }

    @Test
    void skipsCampfireUnderRoof() {
        when(world.getHighestBlockYAt(-8, -8, HeightMap.MOTION_BLOCKING)).thenReturn(67);

        check.run(List.of(playerAt(-7, 64, -7)));

        verifyNoInteractions(roll);
    }

    @Test
    void skipsDryBiomeDuringStorm() {
        when(block.getHumidity()).thenReturn(0.0);

        check.run(List.of(playerAt(-7, 64, -7)));

        verifyNoInteractions(roll);
    }

    @Test
    void skipsSnow() {
        when(block.getTemperature()).thenReturn(0.1);

        check.run(List.of(playerAt(-7, 64, -7)));

        verifyNoInteractions(roll);
    }

    @Test
    void skipsUnlitCampfire() {
        when(campfire.isLit()).thenReturn(false);

        check.run(List.of(playerAt(-7, 64, -7)));

        verifyNoInteractions(roll);
    }

    @Test
    void skipsClearWeather() {
        when(world.hasStorm()).thenReturn(false);

        check.run(List.of(playerAt(-7, 64, -7)));

        verify(world, never()).getChunkAt(anyInt(), anyInt());
        verifyNoInteractions(roll);
    }

    private Player playerAt(int x, int y, int z) {
        Player player = mock(Player.class);
        when(player.getWorld()).thenReturn(world);
        when(player.getLocation()).thenReturn(new Location(world, x, y, z));
        return player;
    }
}
