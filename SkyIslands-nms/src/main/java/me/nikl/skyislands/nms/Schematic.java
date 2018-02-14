package me.nikl.skyislands.nms;

import org.bukkit.Location;

import java.io.InputStream;

/**
 * Created by nikl on 13.02.18.
 */
public abstract class Schematic {
    protected short width;
    protected short height;
    protected short length;

    protected byte[] blocks;
    protected byte[] data;

    protected int bedrockX, bedrockY, bedrockZ;

    public Schematic(InputStream schematicStream){
        loadSchematic(schematicStream);
    }

    abstract protected void loadSchematic(InputStream schematicStream);

    abstract public void paste(Location location);
}
