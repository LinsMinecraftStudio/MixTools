package org.lins.mmmjjkx.mixtools.managers.features.kit;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.utils.ItemStackBuilder;

import java.io.File;
import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.*;

public class KitCreator implements Listener {
    private final Player player;
    private final String kitName;
    private final List<Integer> unusableSlot = Lists.newArrayList(4,6,7,8,9,10,11,12,13,14,15,16,17);

    public KitCreator(Player p, String kitName) {
        this.player = p;
        this.kitName = kitName;
        Bukkit.getPluginManager().registerEvents(this, MixTools.INSTANCE);
    }

    public void openInventory(){
        Inventory inventory = Bukkit.createInventory(null, 54,
                MixTools.messageHandler.getColored("Kit.Title",kitName));
        ItemStack glassPane = MixTools.settingsManager.getItemStack(KIT_ITEM_IN_NON_PLACEABLE_SLOTS_TYPE);
        ItemMeta glassPaneMeta = glassPane.getItemMeta();
        glassPaneMeta.setDisplayName(MixTools.settingsManager.getString(KIT_ITEM_IN_NON_PLACEABLE_SLOTS_NAME,true));
        glassPane.setItemMeta(glassPaneMeta);
        for (int i: unusableSlot){
            if (i == 8) continue;
            inventory.setItem(i,glassPane);
        }
        ItemStack close = MixTools.settingsManager.getItemStack(KIT_EDITOR_CLOSE_BUTTON_ITEM);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(MixTools.settingsManager.getString(KIT_EDITOR_CLOSE_BUTTON_NAME));
        close.setItemMeta(closeMeta);
        inventory.setItem(8,close);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onCreate(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        if (e.getView().getTitle().contains(kitName)) {
            saveToFile(inventory);
            MixTools.messageHandler.sendMessage(e.getPlayer(),"Kit.CreateSuccess",kitName);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getView().getTitle().contains(kitName)) {
            if (unusableSlot.contains(e.getRawSlot())){
                if (e.getRawSlot() == 8) {
                    e.getWhoClicked().closeInventory();
                }
                e.setCancelled(true);
            }
        }
    }

    public void saveToFile(Inventory inv) {
        try {
            File file = new File(MixTools.INSTANCE.getDataFolder(), "kits");
            if (!file.exists()) {
                file.mkdirs();
            }
            File kitFile = new File(file, kitName + ".yml");
            if (!kitFile.exists()) {
                kitFile.createNewFile();
            }
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(kitFile);
            ConfigurationSection equipment = yamlConfiguration.createSection("equipment");
            ConfigurationSection section = yamlConfiguration.createSection("slot");
            for (int i = 0; i < 54;i++){
                if (unusableSlot.contains(i)) continue;
                ItemStack stack = inv.getItem(i);
                if (stack == null) continue;
                MixTools.log(i + ": " + stack);
                if (stack.getType().isAir()) continue;
                switch (i) {
                    case 0 -> equipment.set("head", ItemStackBuilder.asMap(stack));
                    case 1 -> equipment.set("chest", ItemStackBuilder.asMap(stack));
                    case 2 -> equipment.set("legs", ItemStackBuilder.asMap(stack));
                    case 3 -> equipment.set("feet", ItemStackBuilder.asMap(stack));
                    case 5 -> equipment.set("offHand", ItemStackBuilder.asMap(stack));
                    default -> section.set(String.valueOf(i), ItemStackBuilder.asMap(stack));
                }
            }
            yamlConfiguration.save(kitFile);
            MixTools.kitManager.loadKits();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}