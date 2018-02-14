package me.nikl.skyislands.player;

import me.nikl.skyislands.SkyIslands;
import me.nikl.skyislands.island.Island;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Niklas on 13.04.2017.
 *
 *
 */
public class PlayerManager {
    private SkyIslands plugin;
    private Map<UUID, SIPlayer> players;

    public PlayerManager(SkyIslands plugin){
        this.plugin = plugin;

        players = new HashMap<>();
    }

    public void shutDown() {
        for (SIPlayer player : players.values()){
            player.save();
        }
    }

    public void loadPlayer(UUID uuid) {
        SIPlayer player;
        if(plugin.getWorldManager().hasIsland(uuid)){
            player = new SIPlayer(plugin, new Island(uuid, plugin.getWorldManager().getIslandConfigurationSection(uuid)));
        } else {
            player = new SIPlayer(plugin, null);
        }
        players.put(uuid, player);
    }
}
