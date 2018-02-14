package me.nikl.skyislands.commands;

import me.nikl.skyislands.SkyIslands;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Niklas on 12.04.2017.
 *
 * Handling the main command /skyisland
 */
public class MainCommand implements CommandExecutor{
    private SkyIslands plugin;

    public MainCommand(SkyIslands plugin){
        this.plugin = plugin;

        plugin.getCommand("skyisland").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0){

        } else if(args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
            if(!(sender instanceof Player)){
                sender.sendMessage("only as a player!");
                return true;
            }
            ((Player) sender).teleport(plugin.getWorldManager().getSkyWorld().getSpawnLocation());
            return true;
        } else if(args.length == 1 && args[0].equalsIgnoreCase("new")) {
            if(!(sender instanceof Player)){
                sender.sendMessage("only as a player!");
                return true;
            }
            sender.sendMessage("Making new island and porting you there");
            if(!plugin.getWorldManager().createIsland(((Player) sender).getUniqueId())){
                sender.sendMessage("you already have an island... porting you there now");
            }
            Location loc = plugin.getWorldManager().getIslandBedrockLocation(((Player) sender).getUniqueId());
            ((Player) sender).teleport(loc.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getLocation().add(0.5, 1.1, 0.5));
            return true;
        } else if(args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            plugin.getWorldManager().reset();
            sender.sendMessage("reset all islands");
            return true;
        }
        sender.sendMessage("Whaaaaaat?");
        return true;
    }
}
