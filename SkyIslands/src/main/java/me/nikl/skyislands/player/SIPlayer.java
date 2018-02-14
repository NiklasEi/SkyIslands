package me.nikl.skyislands.player;

import me.nikl.skyislands.SkyIslands;
import me.nikl.skyislands.island.Island;
import org.bukkit.Location;

import java.util.UUID;

/**
 * Created by Niklas on 12.04.2017.
 */
public class SIPlayer {
    private Island island;
    private Location home;
    private UUID uuid;

    private SkyIslands plugin;

    public SIPlayer(SkyIslands plugin, Island island){
        this.island = island;
        this.plugin = plugin;
    }

    public void save(){
        if(island != null) plugin.getWorldManager().saveIsland(island);
    }
}
