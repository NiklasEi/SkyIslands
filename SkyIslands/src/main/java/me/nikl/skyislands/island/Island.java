package me.nikl.skyislands.island;

import me.nikl.skyislands.utility.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Niklas on 12.04.2017.
 */
public class Island {
    private UUID owner;
    private Location bedrock;

    private Map<SettingsFlag, Boolean> flags;

    /**
     * load an island from ConfigurationSection
     * @param owner uuid of the owner
     * @param section to load the island from
     */
    public Island(UUID owner, ConfigurationSection section){
        this.owner = owner;

        if(section != null){
            load(section);
        }
    }

    /**
     * Make a new island!
     * @param bedrock
     * @param owner
     */
    public Island(Location bedrock, UUID owner){
        this.owner = owner;
        this.bedrock = bedrock;

        // ToDo get default values
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getBedrock() {
        return bedrock;
    }

    public boolean isFlagSet(SettingsFlag flag){
        return (flags.get(flag) == null?false:flags.get(flag));
    }

    public ConfigurationSection save(ConfigurationSection section){
        section.set("owner", owner.toString());
        section.set("bedrock", LocationUtil.toString(bedrock));
        for(SettingsFlag flag : flags.keySet()){
            section.set("flags."+flag.toString().toLowerCase(), flags.get(flag));
        }

        return section;
    }

    public void load(ConfigurationSection section){
        String bedrockString = section.getString("bedrock");
        Bukkit.getLogger().log(Level.SEVERE, bedrockString);
    }
}
