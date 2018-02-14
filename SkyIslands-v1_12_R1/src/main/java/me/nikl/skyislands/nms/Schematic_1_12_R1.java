package me.nikl.skyislands.nms;

import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nikl on 13.02.18.
 */
public class Schematic_1_12_R1 extends Schematic {
    public Schematic_1_12_R1(InputStream schematicStream) {
        super(schematicStream);
    }

    @Override
    protected void loadSchematic(InputStream schematicStream) {
        try {
            Object nbtData = NBTCompressedStreamTools.class.getMethod("a", InputStream.class).invoke(null, schematicStream);
            Method getShort  = nbtData.getClass().getMethod("getShort", String.class);
            Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);

            width = ((short) getShort.invoke(nbtData, "Width"));
            height = ((short) getShort.invoke(nbtData, "Height"));
            length = ((short) getShort.invoke(nbtData, "Length"));

            blocks = ((byte[]) getByteArray.invoke(nbtData, "Blocks"));
            data = ((byte[]) getByteArray.invoke(nbtData, "Data"));

            for(int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < length; z++) {
                        int index = y * width * length + z * width + x;
                        if ((blocks[index] & 0xFF) == 7) {
                            bedrockX = x;
                            bedrockY = y;
                            bedrockZ = z;
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paste(Location location) {
        net.minecraft.server.v1_12_R1.World world = ((CraftWorld) location.getWorld()).getHandle();
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                for(int z = 0; z < length; z++){
                    int index = y * width * length + z * width + x;
                    // so that blocks with an id over 127, like quartz and emerald, can be pasted
                    int locX = location.getBlockX() - bedrockX + x;
                    int locY = location.getBlockY() - bedrockY + y;
                    int locZ = location.getBlockZ() - bedrockZ + z;
                    BlockPosition blockPosition = new BlockPosition(locX, locY, locZ);

                    int idAndData = (blocks[index] & 0xFF) + (data[index] << 12);
                    IBlockData blockData = Block.getByCombinedId(idAndData);

                    world.setTypeAndData(blockPosition, blockData, 3);
                    world.getChunkAt(locX >> 4, locZ >> 4).a(blockPosition, blockData);
                }
            }
        }
    }
}
