package me.nikl.skyislands.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Created by Niklas on 12.04.2017.
 *
 * Utility class for locations
 */
public class LocationUtil {

    public static boolean isSafeSpawn(final Location location) {
        if (location == null) {
            return false;
        }

        final Block block1 = location.getBlock().getRelative(BlockFace.DOWN);
        final Block block2 = location.getBlock();
        final Block block3 = location.getBlock().getRelative(BlockFace.UP);
        return block1.getType().isSolid() && !block2.getType().isSolid() && !block2.isLiquid() && !block3.getType().isSolid() && !block3.isLiquid();
    }

    public static Location centerInBlock(Block block){
        return block.getLocation().add(0.5, 0, 0.5);
    }

    public static Location centerInBlock(Location location){
        return centerInBlock(location.getBlock());
    }

    public static Location fromString(String locationString){
        String[] split = locationString.split(":");
        if(split.length != 6) return null;

        World world = Bukkit.getWorld(split[0]);
        if(world == null) return null;

        double x, y, z;
        float yaw, pitch;

        try{
            x = Double.parseDouble(split[1]);
            y = Double.parseDouble(split[2]);
            z = Double.parseDouble(split[3]);

            yaw = Float.parseFloat(split[4]);
            pitch = Float.parseFloat(split[5]);
        } catch (NumberFormatException exception){
            return null;
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String toString(Location location){
        return location.getWorld().getName() + ":"
                + String.format("%.2f", location.getX()) + ":"
                + String.format("%.2f", location.getY()) + ":"
                + String.format("%.2f", location.getZ()) + ":"
                + String.format("%.2f", location.getYaw()) + ":"
                + String.format("%.2f", location.getPitch());
    }
}
