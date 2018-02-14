package me.nikl.skyislands.listener;

import me.nikl.skyislands.SkyIslands;
import me.nikl.skyislands.utility.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Niklas on 02.05.2017.
 *
 * Spawn configured ores in a cobble generator
 * with a configured possibility per ore
 */
public class OreGenerator extends SkyListener{
    private final BlockFace[] faces = new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    private Map<Integer, MaterialData> ores = new HashMap<>();
    private Random random = new Random();
    private int totalChance;
    private Stream<Integer> sortedStream;

    /**
     * Save main instance and register listener
     *
     * @param plugin instance
     */
    public OreGenerator(SkyIslands plugin) {
        super(plugin);

        loadChances();
    }

    private void loadChances() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("oreGen.ores");
        if(section == null) return;

        ItemStack itemStack;
        int key = 0;
        for(String matString : section.getKeys(false)){
            itemStack = ItemUtil.getItemStack(matString);

            if(itemStack == null){
                Bukkit.getConsoleSender().sendMessage(plugin.getLanguage().PREFIX + " Can't load the ore '" + matString + "'");
                continue;
            }
            if(!section.isInt(matString)){
                Bukkit.getConsoleSender().sendMessage(plugin.getLanguage().PREFIX + " '" + matString + "' has to be a int > 0(OreGen)");
                continue;
            }

            key += section.getInt(matString);
            ores.put(key, itemStack.getData());
        }
        totalChance = key;
        sortedStream = ores.keySet().stream().sorted();

        SkyIslands.debug(ores.size() + " ores were loaded from the config file");
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        if(!event.getBlock().getLocation().getWorld().equals(plugin.getWorldManager().getSkyWorld())) return;

        int id = event.getBlock().getTypeId();
        // return if not flowing/standing water/lava
        if (id < 8 || id > 11) return;
        Block block = event.getToBlock();
        // to block is air
        if (block.getTypeId() != 0) return;
        // check whether that block can turn to cobble
        if(!generatesCobble(id, block)) return;

        MaterialData data = ores.get(getKey(random.nextInt(totalChance)));

        block.setType(data.getItemType());
        block.setData(data.getData());
    }

    private Integer getKey(int randomNumber) {
        Iterator<Integer> it = sortedStream.iterator();
        int key;
        while (it.hasNext()){
            key = it.next();
            if(randomNumber < key){
                return key;
            }
        }
        // cannot happen
        return totalChance;
    }

    @SuppressWarnings("deprecation")
    public boolean generatesCobble(int fromBlockID, Block toBlock) {
        int mirrorID1 = (fromBlockID == 8 || fromBlockID == 9 ? 10 : 8);
        // keeping vanilla behaviour for standing lava (obsidian)
        for (BlockFace face : faces) {
            Block r = toBlock.getRelative(face, 1);
            if (r.getTypeId() == mirrorID1 || ((fromBlockID == 10 || fromBlockID == 11) && r.getTypeId() == 9)) {
                return true;
            }
        }
        return false;
    }
}
