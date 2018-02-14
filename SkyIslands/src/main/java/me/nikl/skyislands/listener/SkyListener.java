package me.nikl.skyislands.listener;

import me.nikl.skyislands.SkyIslands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;


/**
 * Created by Niklas on 12.04.2017.
 */
public class SkyListener implements Listener {
    protected SkyIslands plugin;
    protected World skyWorld;

    /**
     * Save main instance and register listener
     *
     * @param plugin instance
     */
    public SkyListener(SkyIslands plugin){
        this.plugin = plugin;
        this.skyWorld = plugin.getWorldManager().getSkyWorld();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    protected boolean inWorld(Location location){
        return location.getWorld().equals(skyWorld);
    }
}
