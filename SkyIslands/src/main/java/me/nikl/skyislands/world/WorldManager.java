package me.nikl.skyislands.world;

import me.nikl.skyislands.SkyIslands;
import me.nikl.skyislands.island.Island;
import me.nikl.skyislands.nms.NmsFactory;
import me.nikl.skyislands.nms.Schematic;
import me.nikl.skyislands.utility.LocationUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by Niklas on 12.04.2017.
 *
 *
 */
public class WorldManager {
    private String skyWorldName = "SkyIslands";
    private World skyWorld;
    private SkyIslands plugin;
    private IslandGrid islandGrid;

    private File islandsFile;
    private FileConfiguration islandsYML;

    private int numberOfIslands;

    private Map<UUID, Island> islands;
    private Schematic islandSchematic;
    private String schematicFilePath = "schematics/island.schematic";

    public Location worldSpawn;

    public WorldManager(SkyIslands plugin){
        this.plugin = plugin;
        islandSchematic = NmsFactory.getSchematic(plugin.getResource(schematicFilePath));
        loadWorld();
        prepareWorldSpawn();

        loadIslandFile();
    }

    public void prepareIslandGrid() {
        if(islandsYML.isSet("numberOfIslands") && islandsYML.isInt("numberOfIslands")){
            numberOfIslands = islandsYML.getInt("numberOfIslands");
        } else {
            numberOfIslands = 0;
        }
        islands = new HashMap<>();
        islandGrid = new IslandGrid(plugin);
    }

    private void prepareWorldSpawn(){
        worldSpawn = skyWorld.getSpawnLocation();
        SkyIslands.debug("is safe? " + LocationUtil.isSafeSpawn(worldSpawn));
        if (!LocationUtil.isSafeSpawn(worldSpawn)) {
            skyWorld.setSpawnLocation(WorldSettings.GRID_SIZE / 2, WorldSettings.ISLAND_Y, WorldSettings.GRID_SIZE / 2);
            worldSpawn = skyWorld.getSpawnLocation();
            Block spawnBlock = skyWorld.getBlockAt(worldSpawn).getRelative(BlockFace.DOWN);
            spawnBlock.setType(Material.BEDROCK);
            Block air1 = spawnBlock.getRelative(BlockFace.UP);
            air1.setType(Material.AIR);
            air1.getRelative(BlockFace.UP).setType(Material.AIR);
        }
    }

    private boolean loadIslandFile() {
        this.islandsFile = new File(plugin.getDataFolder().toString() + File.separatorChar + "data" + File.separatorChar + "islands.yml");
        if(!islandsFile.exists()){
            try {
                islandsFile.getParentFile().mkdir();
                islandsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            this.islandsYML = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(this.islandsFile), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void loadWorld() {
        skyWorld = Bukkit.getWorld(skyWorldName);
        if(skyWorld == null){
            skyWorld = WorldCreator
                    .name(skyWorldName)
                    .type(WorldType.FLAT)
                    .generateStructures(false)
                    .environment(World.Environment.NORMAL)
                    .generator(new IslandChunkGenerator(plugin))
                    .createWorld();
        }
    }

    public void onDisable(){
        save();
    }

    public void save(){
        islandsYML.set("numberOfIslands", numberOfIslands);
        try {
            this.islandsYML.save(islandsFile);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "failed to save islands", e);
            e.printStackTrace();
        }
    }

    public boolean createIsland(UUID uuid){
        if(islands.keySet().contains(uuid)){
            Bukkit.getLogger().log(Level.WARNING, "there is already an island with hat uuid!");
            //return false;
        }
        numberOfIslands++;
        Location nextIsland = islandGrid.nthIslandPosition(numberOfIslands + 1);
        islandSchematic.paste(nextIsland);
        islands.put(uuid, new Island(nextIsland, uuid));
        return true;
    }
    public void reset() {
        islands.clear();
        numberOfIslands = 0;
    }


    public void saveIsland(Island island) {
        if(islandsYML.isConfigurationSection(island.getOwner().toString())){
            islandsYML.set(island.getOwner().toString(),island.save(islandsYML.getConfigurationSection(island.getOwner().toString())));
        } else {
            islandsYML.set(island.getOwner().toString(), islandsYML.createSection(island.getOwner().toString()));
        }
    }

    public boolean hasIsland(UUID uuid){
        return islandsYML.isConfigurationSection(uuid.toString());
    }

    public ConfigurationSection getIslandConfigurationSection(UUID owner){
        return islandsYML.getConfigurationSection(owner.toString());
    }

    public World getSkyWorld() {
        return skyWorld;
    }

    @Deprecated
    public Location getIslandBedrockLocation(UUID uniqueId) {
        return islands.get(uniqueId).getBedrock();
    }
}
