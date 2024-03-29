package org.lins.mmmjjkx.mixtools.managers.features.kit;

import com.google.common.collect.Lists;
import io.github.linsminecraftstudio.polymer.itemstack.ItemStackBuilder;
import io.github.linsminecraftstudio.polymer.itemstack.ItemStackConverter;
import io.github.linsminecraftstudio.polymer.utils.ObjectConverter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.utils.InventoryFactory;

import java.io.File;
import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.KIT_EDITOR_CLOSE_BUTTON_ITEM;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.KIT_EDITOR_CLOSE_BUTTON_NAME;

public class KitCreator implements Listener {
    private final Player player;
    private final String kitName;
    private final Component kitNameComponent;
    private final List<Integer> unusableSlot = Lists.newArrayList(4,6,7,8,9,10,11,12,13,14,15,16,17);

    public KitCreator(Player p, String kitName) {
        this.player = p;
        this.kitName = kitName;
        this.kitNameComponent = ObjectConverter.toComponent(kitName);
        Bukkit.getPluginManager().registerEvents(this, MixTools.getInstance());
    }

    public void openInventory(){
        Inventory inventory = InventoryFactory.createDefaultStyleInventory(MixTools.messageHandler.getColored("Kit.Title",kitName), false);
        ItemStackBuilder closeStackBuilder = new ItemStackBuilder(MixTools.settingsManager.getItemStack(KIT_EDITOR_CLOSE_BUTTON_ITEM));
        closeStackBuilder.name(MixTools.settingsManager.getComponent(KIT_EDITOR_CLOSE_BUTTON_NAME,true));
        ItemStack close = closeStackBuilder.build();
        inventory.setItem(8,close);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onCreate(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        if (e.getView().title().contains(kitNameComponent)) {
            saveToFile(inventory);
            MixTools.messageHandler.sendMessage(e.getPlayer(),"Kit.CreateSuccess",kitName);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getView().title().contains(kitNameComponent)) {
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
            File file = new File(MixTools.getInstance().getDataFolder(), "kits");
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
                if (stack.getType().isAir()) continue;
                switch (i) {
                    case 0 -> equipment.set("head", ItemStackConverter.asMap(stack));
                    case 1 -> equipment.set("chest", ItemStackConverter.asMap(stack));
                    case 2 -> equipment.set("legs", ItemStackConverter.asMap(stack));
                    case 3 -> equipment.set("feet", ItemStackConverter.asMap(stack));
                    case 5 -> equipment.set("offHand", ItemStackConverter.asMap(stack));
                    default -> section.set(String.valueOf(i-18), ItemStackConverter.asMap(stack));
                }
            }
            yamlConfiguration.save(kitFile);
            MixTools.kitManager.loadKits();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}