package me.nikl.skyislands.nms;

import org.bukkit.Bukkit;

import java.io.InputStream;

/**
 * Created by nikl on 13.02.18.
 */
public class NmsFactory {
    private final static String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static NmsUtility getNmsUtility(){
        switch (VERSION){
            case "v1_12_R1":
                return new NmsUtility_1_12_R1();
            default: return null;
        }
    }

    public static Schematic getSchematic(InputStream schematicStream){
        switch (VERSION){
            case "v1_12_R1":
                return new Schematic_1_12_R1(schematicStream);
            default: return null;
        }
    }
}
