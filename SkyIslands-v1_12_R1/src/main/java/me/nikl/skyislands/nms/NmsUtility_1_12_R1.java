package me.nikl.skyislands.nms;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * Created by nikl on 10.02.18.
 */
public class NmsUtility_1_12_R1 implements NmsUtility {

    @Override
    public void setBlock(Block block, int blockId, byte data, boolean usePhysics) {
        net.minecraft.server.v1_12_R1.World world = ((CraftWorld) block.getWorld()).getHandle();
        net.minecraft.server.v1_12_R1.Chunk chunk = world.getChunkAt(block.getX() >> 4, block.getZ() >> 4);
        BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());

        int idAndData = blockId + (data << 12);
        IBlockData blockData = net.minecraft.server.v1_12_R1.Block.getByCombinedId(idAndData);

        if (usePhysics) {
            world.setTypeAndData(blockPosition, blockData, 3);
        } else {
            world.setTypeAndData(blockPosition, blockData, 2);
        }
        chunk.a(blockPosition, blockData);
    }

    @Override
    public void pasteSchematic(Location location, InputStream inputStream) {
        try {
            Object nbtData = NBTCompressedStreamTools.class.getMethod("a", InputStream.class).invoke(null, inputStream);
            Method getShort  = nbtData.getClass().getMethod("getShort", String.class);
            Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);

            short width = ((short) getShort.invoke(nbtData, "Width"));
            short height = ((short) getShort.invoke(nbtData, "Height"));
            short length = ((short) getShort.invoke(nbtData, "Length"));

            byte[] blocks = ((byte[]) getByteArray.invoke(nbtData, "Blocks"));
            byte[] data = ((byte[]) getByteArray.invoke(nbtData, "Data"));

            net.minecraft.server.v1_12_R1.World world = ((CraftWorld) location.getWorld()).getHandle();

            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    for(int z = 0; z < length; z++){
                        int index = y * width * length + z * width + x;
                        int b = blocks[index] & 0xFF;//make the block unsigned,
                        // so that blocks with an id over 127, like quartz and emerald, can be pasted
                        int locX = location.getBlockX() - ((int) (width / 2) + 1) + x;
                        int locY = location.getBlockY() - 10 + y;
                        int locZ = location.getBlockZ() - ((int) (length / 2) + 1) + z;
                        BlockPosition blockPosition = new BlockPosition(locX, locY, locZ);

                        int idAndData = b + (data[index] << 12);
                        IBlockData blockData = net.minecraft.server.v1_12_R1.Block.getByCombinedId(idAndData);

                        world.setTypeAndData(blockPosition, blockData, 3);
                        world.getChunkAt(locX >> 4, locZ >> 4).a(blockPosition, blockData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ToDo: use to turn off clouds for all players (some donÄt seem to know that they can in the options..)
    // https://bitbucket.org/AventiumSoftworks/skychanger/src/47bdc17921e2de84824fac0e8d897eda2afc66de/src/main/java/com/dscalzi/skychanger/internal/SkyChangeImpl.java?at=master&fileviewer=file-view-default
    /*protected boolean sendPacket(Player player, float number){
        try	{
            Class<?> packetClass = ReflectionUtil.getNMSClass("PacketPlayOutGameStateChange");
            Constructor<?> packetConstructor = packetClass.getConstructor(int.class, float.class);
            Object packet = packetConstructor.newInstance(7, number);
            Method sendPacket = ReflectionUtil.getNMSClass("PlayerConnection").getMethod("sendPacket", ReflectionUtil.getNMSClass("Packet"));
            sendPacket.invoke(this.getConnection(player), packet);
        } catch (Exception e) {
            MessageManager.getInstance().logPacketError();
            e.printStackTrace();
            return false;
        }
        return true;
    }*/

}
