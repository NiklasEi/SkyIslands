package me.nikl.skyislands.world;



import me.nikl.skyislands.SkyIslands;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Niklas.
 *
 * ChunkGenerator that generates empty chunks
 */
public class IslandChunkGenerator extends ChunkGenerator {
    private static final byte[] generate = new byte[32768];
    private static final short[][] extBlockSections = new short[16][];
    private static final List<BlockPopulator> emptyBlockPopulatorList = new ArrayList<>();

    private SkyIslands plugin;

    public IslandChunkGenerator(SkyIslands plugin){
        this.plugin = plugin;
    }

    @Override
    public byte[] generate(World world, Random random, int x, int z) {
        return generate;
    }

    @Override
    @SuppressWarnings("deprecation")
    public short[][] generateExtBlockSections(World world, Random random, int cx, int cz, BiomeGrid biomes) {
        setDefaultBiome(biomes);
        return extBlockSections;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0.5, WorldSettings.ISLAND_Y, 0.5);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return emptyBlockPopulatorList;
    }

    /**
     * Hardcoded OCEAN biome as default
     *
     * @param biomes biomegrid to set the biome in
     */
    private void setDefaultBiome(BiomeGrid biomes) {
        if (biomes != null) {
            Biome biome = Biome.valueOf("OCEAN");
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    biomes.setBiome(x,z, biome);
                }
            }
        }
    }

}
