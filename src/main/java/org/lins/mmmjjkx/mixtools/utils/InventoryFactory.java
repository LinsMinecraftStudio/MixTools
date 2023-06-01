package org.lins.mmmjjkx.mixtools.utils;

import io.github.linsminecraftstudio.polymer.itemstack.ItemStackBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.mixtools.MixTools;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.*;

public class InventoryFactory {
    public static Inventory createDefaultStyleInventory(Component title, boolean closeButton){
        Inventory inventory = Bukkit.createInventory(null, 54, title);
        int[] unusableSlot = new int[]{4,6,7,8,9,10,11,12,13,14,15,16,17};
        ItemStackBuilder closeStackBuilder = new ItemStackBuilder(MixTools.settingsManager.getItemStack(INVENTORY_DEFAULT_STYLE_CLOSE_BUTTON_TYPE));
        closeStackBuilder.name(MixTools.settingsManager.getComponent(INVENTORY_DEFAULT_STYLE_CLOSE_BUTTON_NAME,true));
        ItemStack close = closeStackBuilder.build();
        ItemStackBuilder decorationBuilder = new ItemStackBuilder(MixTools.settingsManager.getItemStack(INVENTORY_DEFAULT_STYLE_DECORATION_TYPE));
        decorationBuilder.name(MixTools.settingsManager.getComponent(INVENTORY_DEFAULT_STYLE_DECORATION_NAME, true));
        ItemStack decoration = decorationBuilder.build();
        for (int slot : unusableSlot) {
            if (closeButton & slot == 8){
                inventory.setItem(8, close);
                continue;
            }
            inventory.setItem(slot, decoration);
        }
        return inventory;
    }
}
