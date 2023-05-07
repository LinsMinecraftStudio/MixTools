package org.lins.mmmjjkx.mixtools.managers.kit;

import com.google.common.primitives.Booleans;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.utils.ItemStackBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class KitCreator implements Listener {
    private final Player player;
    private final String kitName;
    private final List<Integer> unusableSlot = List.of(4,9,10,11,12,13,14,15,16,17);

    public KitCreator(Player p, String kitName) {
        this.player = p;
        this.kitName = kitName;
        Bukkit.getPluginManager().registerEvents(this, MixTools.INSTANCE);
    }

    public void openInventory(){
        Inventory inventory = Bukkit.createInventory(null, 54,
                MixTools.messageHandler.getColored("Kit.Title",kitName));
        ItemStack glassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int i: unusableSlot){
            inventory.setItem(i,glassPane);
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void onCreate(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        if (e.getView().getTitle().contains(kitName)) {
            saveToFile(inventory);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getView().getTitle().contains(kitName)) {
            if (unusableSlot.contains(e.getRawSlot())){
                e.setCancelled(true);
            }
        }
    }

    public void saveToFile(Inventory inv) {
        try {
            File file = new File(MixTools.INSTANCE.getDataFolder(), "kit");
            if (!file.exists()) {
                file.mkdirs();
            }
            File kitFile = new File(file, kitName + ".yml");
            if (!kitFile.exists()) {
                kitFile.createNewFile();
            }
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            for (int i = 0; i < 54;i++){
                if (unusableSlot.contains(i)) continue;
                ItemStack stack = inv.getItem(i);
                if (stack == null) continue;
                ConfigurationSection section = yamlConfiguration.createSection("slot");
                section.createSection(String.valueOf(i), ItemStackBuilder.asMap(stack));
            }
            yamlConfiguration.save(kitFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}