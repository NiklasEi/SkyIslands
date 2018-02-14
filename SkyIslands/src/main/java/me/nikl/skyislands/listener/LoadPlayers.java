package me.nikl.skyislands.listener;

import me.nikl.skyislands.SkyIslands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * Created by Niklas
 *
 */
public class LoadPlayers extends SkyListener{

    public LoadPlayers(SkyIslands plugin) {
        super(plugin);

        for(Player player : Bukkit.getOnlinePlayers()){
            loadPlayer(player.getUniqueId());
        }
    }

    private void loadPlayer(UUID uuid) {
        plugin.getPlayerManager().loadPlayer(uuid);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        loadPlayer(event.getPlayer().getUniqueId());
    }

}
