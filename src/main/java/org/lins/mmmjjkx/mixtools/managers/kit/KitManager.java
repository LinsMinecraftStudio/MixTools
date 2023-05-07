package org.lins.mmmjjkx.mixtools.managers.kit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsKit;
import org.lins.mmmjjkx.mixtools.utils.ItemStackBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KitManager {
    private List<MixToolsKit> kits = new ArrayList<>();
    public KitManager(){
        loadKits();
    }

    private void loadKits(){
        File file = new File(MixTools.INSTANCE.getDataFolder(), "kit");
        if (!file.exists()) {
            file.mkdirs();
            return;
        }
        File[] files = file.listFiles();
        if (files == null) return;

        for (File kitFile: files) {
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            try {yamlConfiguration.load(kitFile);
            } catch (Exception e) {throw new RuntimeException(e);}
            ConfigurationSection section = yamlConfiguration.getConfigurationSection("slot");
            if (section == null) continue;
            List<ItemStack> itemStacks = new ArrayList<>();
            for (String key: section.getKeys(false)) {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                if (itemSection == null) continue;
                ItemStack stack = ItemStackBuilder.toItemStack(itemSection);
                itemStacks.add(stack);
            }
            kits.add(new MixToolsKit(kitFile.getName(), itemStacks));
        }
    }
}
