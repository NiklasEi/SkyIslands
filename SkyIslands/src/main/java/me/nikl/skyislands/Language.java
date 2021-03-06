package me.nikl.skyislands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by nikl on 24.10.17.
 *
 * Hold messages and handle the language files.
 * Default files are handled in {@link me.nikl.skyislands.utility.FileUtil}.
 */
public class Language {

    private SkyIslands plugin;

    private File languageFile;

    private FileConfiguration defaultLanguage;
    private FileConfiguration language;

    public String PREFIX = "["+ChatColor.DARK_AQUA+"SkyIslands"+ChatColor.RESET+"]";
    public String NAME = ChatColor.DARK_AQUA+"SkyIslands"+ChatColor.RESET;
    public String PLAIN_PREFIX = ChatColor.stripColor(PREFIX);
    public String PLAIN_NAME = ChatColor.stripColor(NAME);
    public String DEFAULT_NAME, DEFAULT_PLAIN_NAME;

    Language(SkyIslands plugin){
        this.plugin = plugin;
    }

    void reload(){
        getLangFile(plugin.getConfig());
        loadMessages();
    }

    /**
     * Load all messages from the language file
     */
    private void loadMessages(){
        PREFIX = getString("prefix");
        NAME = getString("name");
        PLAIN_PREFIX = ChatColor.stripColor(PREFIX);
        PLAIN_NAME = ChatColor.stripColor(NAME);
        DEFAULT_NAME = ChatColor.translateAlternateColorCodes('&'
                , defaultLanguage.getString("name", "SkyIslands"));
        DEFAULT_PLAIN_NAME = ChatColor.stripColor(DEFAULT_NAME);
    }

    /**
     * Try loading the language file specified in the
     * passed file configuration.
     *
     * The required set option is 'langFile'. Possible options
     * are:
     * 'default'/'default.yml': loads the english language file from inside the jar
     * 'lang_xx.yml': will try to load the given file inside the namespaces language folder
     * @param config
     */
    private void getLangFile(FileConfiguration config) {
        // load default language
        try {
            String defaultLangName = "language/lang_en.yml";
            defaultLanguage = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(plugin.getResource(defaultLangName), "UTF-8"));
        } catch (UnsupportedEncodingException e2) {
            plugin.getLogger().warning("Failed to load default language file.");
            e2.printStackTrace();
        }

        String fileName = config.getString("langFile");

        if(fileName != null && (fileName.equalsIgnoreCase("default") || fileName.equalsIgnoreCase("default.yml"))) {
            language = defaultLanguage;
            return;
        }

        if(fileName == null || !fileName.endsWith(".yml")){
            plugin.getLogger().warning("Language file is not specified in config.");
            plugin.getLogger().warning("Falling back to the default file...");
            language = defaultLanguage;
            return;
        }

        languageFile =
                new File(plugin.getDataFolder().toString() + File.separatorChar + "language" + File.separatorChar
                        + fileName);

        if(!languageFile.exists()){
            plugin.getLogger().warning("The in config as 'langFile' configured file '" + fileName + "' does not exist!");
            plugin.getLogger().warning("Falling back to the default file...");
            language = defaultLanguage;
            return;
        }

        // File exists
        try {
            language = YamlConfiguration
                    .loadConfiguration(new InputStreamReader(new FileInputStream(languageFile)
                            , "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            plugin.getLogger().warning("Language file '" + plugin.getDataFolder().toString() + File.separatorChar + "language" + File.separatorChar
                    + fileName + "' is not a valid yml.");
            plugin.getLogger().warning("Falling back to the default file...");
            language = defaultLanguage;
        }

        return;
    }

    /**
     * Load list messages from the language file
     *
     * If the requested path is not valid for the chosen
     * language file the corresponding list from the default
     * file is returned.
     * ChatColor can be translated here.
     * @param path path to the message
     * @param color if set, color the loaded message
     * @return message
     */
    protected List<String> getStringList(String path, boolean color) {
        List<String> toReturn;

        // load from default file if path is not valid
        if(!language.isList(path)){
            toReturn = defaultLanguage.getStringList(path);
            if(color && toReturn != null){
                for(int i = 0; i<toReturn.size(); i++){
                    toReturn.set(i, ChatColor.translateAlternateColorCodes('&',toReturn.get(i)));
                }
            }
            return toReturn;
        }

        // load from language file
        toReturn = language.getStringList(path);
        if(color && toReturn != null) {
            for (int i = 0; i < toReturn.size(); i++) {
                toReturn.set(i, ChatColor.translateAlternateColorCodes('&', toReturn.get(i)));
            }
        }
        return toReturn;
    }

    protected List<String> getStringList(String path){
        return getStringList(path, true);
    }

    /**
     * Get a message from the language file
     *
     * If the requested path is not valid for the
     * configured language file the corresponding
     * message from the default file is returned.
     * ChatColor is translated when reading the message.
     * @param path path to the message
     * @param color if set, color the loaded message
     * @return message
     */
    protected String getString(String path, boolean color) {
        String toReturn;
        if(!language.isString(path)){
            toReturn = defaultLanguage.getString(path);
            if(color && toReturn != null){
                return ChatColor.translateAlternateColorCodes('&', defaultLanguage.getString(path));
            }
            return toReturn;
        }
        toReturn = language.getString(path);
        if(!color) return toReturn;
        return ChatColor.translateAlternateColorCodes('&',toReturn);
    }

    protected String getString(String path){
        return getString(path, true);
    }

}
