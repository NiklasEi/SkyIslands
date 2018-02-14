package me.nikl.skyislands.utility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Niklas on 02.05.2017.
 */
public class ItemUtil {

    public static ItemStack getItemStack(String materialString){
        Material mat;
        short data;
        String[] obj = materialString.split(":");

        if (obj.length == 2) {
            try {
                mat = Material.matchMaterial(obj[0]);
            } catch (Exception e) {
                return null; // material name doesn't exist
            }

            try {
                data = Short.valueOf(obj[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null; // data not a number
            }

            //noinspection deprecation
            if (mat == null) return null;
            ItemStack stack = new ItemStack(mat);
            stack.setDurability(data);
            return stack;
        } else {
            try {
                mat = Material.matchMaterial(obj[0]);
            } catch (Exception e) {
                return null; // material name doesn't exist
            }
            //noinspection deprecation
            return (mat == null ? null : new ItemStack(mat));
        }
    }
}
