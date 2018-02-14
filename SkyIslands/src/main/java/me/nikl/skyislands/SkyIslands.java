package me.nikl.skyislands;

import me.nikl.skyislands.commands.MainCommand;
import me.nikl.skyislands.listener.LoadPlayers;
import me.nikl.skyislands.listener.OreGenerator;
import me.nikl.skyislands.nms.NmsFactory;
import me.nikl.skyislands.nms.NmsUtility;
import me.nikl.skyislands.player.PlayerManager;
import me.nikl.skyislands.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

/**
 * Created by nikl on 09.02.18.
 */
public class SkyIslands extends JavaPlugin {

    public static final boolean debug = true;
    private WorldManager worldManager;
    private NmsUtility nms;
    private Language lang;
    private PlayerManager playerManager;

    private FileConfiguration configuration;


    @Override
    public void onEnable(){
        if (!reload()){
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    private boolean reloadConfiguration() {
        File con = new File(this.getDataFolder().toString() + File.separatorChar + "config.yml");
        if(!con.exists()){
            this.saveResource("config.yml", false);
        }
        try {
            this.configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(con), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean reload() {
        if(!reloadConfiguration()){
            getLogger().severe(" Failed to load config file!");
            return false;
        }
        if((nms = NmsFactory.getNmsUtility()) == null){
            Bukkit.getLogger().log(Level.SEVERE, "This plugin does not support your server version!");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        lang = new Language(this);

        // save players before saving the world
        if(playerManager != null){
            playerManager.shutDown();
        }
        if(worldManager != null){
            worldManager.save();
        }

        playerManager = new PlayerManager(this);
        worldManager = new WorldManager(this);
        worldManager.prepareIslandGrid();

        initializeListeners();
        initializeCommands();
        return true;
    }

    private void initializeCommands() {
        new MainCommand(this);
    }

    private void initializeListeners() {
        new LoadPlayers(this);
        new OreGenerator(this);
    }

    @Override
    public void onDisable(){
        if(worldManager != null)worldManager.onDisable();
    }

    public static void debug(String message){
        if(!debug) return;
        Bukkit.getLogger().log(Level.WARNING, message);
    }

    public WorldManager getWorldManager(){
        return this.worldManager;
    }

    public NmsUtility getNms(){
        return nms;
    }

    public Language getLanguage() {
        return lang;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public FileConfiguration getConfig(){
        return this.configuration;
    }
}
