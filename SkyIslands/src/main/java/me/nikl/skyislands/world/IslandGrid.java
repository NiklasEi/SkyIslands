package me.nikl.skyislands.world;

import me.nikl.skyislands.SkyIslands;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by nikl on 13.02.18.
 */
public class IslandGrid {
    private SkyIslands skyIslands;
    private WorldManager worldManager;

    public IslandGrid(SkyIslands skyIslands){
        this.skyIslands = skyIslands;
        this.worldManager = skyIslands.getWorldManager();
    }

    public Location nthIslandPosition(int indexOfIsland){
        Location slotPosition = nthSlotPosition(indexOfIsland);
        return slotPosition.add(WorldSettings.GRID_SIZE/2, 0 , WorldSettings.GRID_SIZE/2);
    }

    public Location nthSlotPosition(int indexOfIsland){
        Validate.isTrue(indexOfIsland > 0, "The index of an island has to be bigger then 0");
        int ring = ringOfNthIsland(indexOfIsland);
        int islandIndexOnRing = indexOnNthRing(indexOfIsland, ring);
        return nthLocationOnRing(islandIndexOnRing, ring);
    }

    private Location nthLocationOnRing(int islandIndexOnRing, int ring) {
        Location location = new Location(worldManager.getSkyWorld(), WorldSettings.GRID_SIZE*ring, WorldSettings.ISLAND_Y, WorldSettings.GRID_SIZE*ring);
        if(islandIndexOnRing == 1) return location;
        islandIndexOnRing--; // this moves the first island in every ring to the top right corner. The last one is on the "left side" of the first.
        // move from the ring location to the island location
        switch ((islandIndexOnRing)/(ring*2)) {
            case 0:
                // on the right side
                location.add(0., 0., -(islandIndexOnRing % (ring * 2)) * WorldSettings.GRID_SIZE);
                break;
            case 1:
                // on the down side
                location.add(-(islandIndexOnRing % (ring * 2)) * WorldSettings.GRID_SIZE, 0., -(ring * 2) * WorldSettings.GRID_SIZE);
                break;
            case 2:
                // left side
                location.add(-(ring * 2) * WorldSettings.GRID_SIZE, 0., (islandIndexOnRing % (ring * 2)) * WorldSettings.GRID_SIZE - (ring * 2) * WorldSettings.GRID_SIZE);
                break;
            case 3:
                // upper side
                location.add((islandIndexOnRing % (ring * 2)) * WorldSettings.GRID_SIZE - (ring * 2) * WorldSettings.GRID_SIZE, 0., 0.);
                break;
            case 4:
                break;
        }
        return location;
    }

    private int indexOnNthRing(int indexOfIsland, int ring) {
        return indexOfIsland - (1 + 2*(ring-1))*(1 + 2*(ring-1)) + (1 + 2*WorldSettings.MAIN_ISLAND_SIZE_RADIUS)*(1 + 2*WorldSettings.MAIN_ISLAND_SIZE_RADIUS);
    }

    private int ringOfNthIsland(int islandIndex){
        return (int)Math.floor(Math.ceil(Math.sqrt(islandIndex+(1 + 2*WorldSettings.MAIN_ISLAND_SIZE_RADIUS)*(1 + 2*WorldSettings.MAIN_ISLAND_SIZE_RADIUS)))/2.);
    }

    private int islandsOnNthRing(int ring){
        switch (ring){
            case 0:
                return 1;
            default:
                return ring*8;
        }
    }

    public int getIslandIndexBySlot(int slotX, int slotY){
        int ring = Math.max(Math.abs(slotX), Math.abs(slotY));
        if(ring <= WorldSettings.MAIN_ISLAND_SIZE_RADIUS) return 0;
        int islandIndex = ring*2 - slotX - slotY;
        if(slotX < slotY) islandIndex = ring*8 - islandIndex + 1;
        return islandIndex + 1;
    }
}
