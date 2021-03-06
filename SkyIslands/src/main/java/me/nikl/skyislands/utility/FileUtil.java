package me.nikl.skyislands.utility;

import me.nikl.skyislands.SkyIslands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by nikl on 23.10.17.
 *
 * Utility class for language related stuff
 */
public class FileUtil {

    /**
     * Copy all default language files to the plugin folder
     *
     * This method checks for every .yml in the language folder
     * whether it is already present in the plugins language folder.
     * If not it is copied.
     */
    public static void copyDefaultLanguageFiles() {
        URL main = SkyIslands.class.getResource("SkyIslands.class");
        try {
            JarURLConnection connection = (JarURLConnection) main.openConnection();
            JarFile jar = new JarFile(URLDecoder.decode( connection.getJarFileURL().getFile(), "UTF-8" ));
            Plugin gameBox = Bukkit.getPluginManager().getPlugin("SkyIslands");
            for (Enumeration list = jar.entries(); list.hasMoreElements(); ) {
                JarEntry entry = (JarEntry) list.nextElement();
                if (entry.getName().split("/")[0].equals("language")) {
                    String[] pathParts = entry.getName().split("/");
                    if (pathParts.length < 2 || !entry.getName().endsWith(".yml")) {
                        continue;
                    }
                    File file = new File(gameBox.getDataFolder().toString() + File.separatorChar + entry.getName());
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        gameBox.saveResource(entry.getName(), false);
                    }
                }
            }
            jar.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}