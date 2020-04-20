package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (20:21 12.03.20)

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Will be deleted in a future update.
 * Please use now the <b>InventoryBuilder</b> class
 * and the <b>fillInventory</b> method!
 */
@Deprecated
public class InventoryUtils {

    public static Inventory createGrayInventory(int size, String title) {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        ItemStack nullItem = getNullItem();
        for (int i = 0; i < size; i++)
            inventory.setItem(i, nullItem);
        return inventory;
    }

    public static Inventory createGrayInventory(int size) {
        return createGrayInventory(size, "");
    }

    public static ItemStack getNullItem() {
        ItemStack itemStack = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("");
        itemMeta.addItemFlags(ItemFlag.values());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}