package me.nikl.skyislands.nms;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.io.InputStream;

/**
 * Created by nikl on 09.02.18.
 */
public interface NmsUtility {
    void setBlock(Block block, int blockId, byte data, boolean usePhysics);
    void pasteSchematic(Location location, InputStream inputStream);
}
