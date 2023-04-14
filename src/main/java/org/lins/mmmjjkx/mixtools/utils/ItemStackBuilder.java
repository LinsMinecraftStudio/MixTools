package org.lins.mmmjjkx.mixtools.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class ItemStackBuilder {
    public static ItemStack toItemStack(ConfigurationSection section){
        String mat = section.getString("material","STONE");
        Material material = Material.getMaterial(mat);
        if (material == null){
            material = Material.STONE;
        }
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (section.contains("amount")) {
            item.setAmount(section.getInt("amount", 1));
        }
        if (meta != null) {
            if (section.contains("displayname")) {
                meta.setDisplayName(section.getString("displayname"));
            }
            if (section.contains("lore")) {
                meta.setLore(section.getStringList("lore"));
            }
            ConfigurationSection section2 = section.getConfigurationSection("enchants");
            if (section2 != null) {
                for (String enchant : section2.getKeys(false)){
                    ConfigurationSection section3 = section2.getConfigurationSection(enchant);
                    if (section3 == null) {continue;}
                    Enchantment enchantment = Enchantment.getByKey(
                            NamespacedKey.fromString(section3.getString("key","")));
                    if (enchantment != null) {
                        int level = section3.getInt(enchant);
                        item.addUnsafeEnchantment(enchantment, level);
                    }
                }
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ConfigurationSection asSection(ConfigurationSection section, ItemStack item){
        ItemMeta meta = item.getItemMeta();
        section.set("material", item.getType().toString());
        section.set("amount", item.getAmount());
        if(meta != null){
            if(meta.hasDisplayName()){
                section.set("displayname", meta.getDisplayName());
            }
            if(meta.hasLore()){
                section.set("lore", meta.getLore());
            }
            if(meta.hasEnchants()){
                ConfigurationSection section2 = section.createSection("enchants");
                Map<Enchantment, Integer> enchants = meta.getEnchants();
                int i = 1;
                for (Enchantment enchantment : enchants.keySet()) {
                    NamespacedKey key = enchantment.getKey();
                    ConfigurationSection section3 = section2.createSection(String.valueOf(i));
                    section3.set("key", key.toString());
                    section3.set("lvl", enchants.get(enchantment));
                }
            }
            section.set("unbreakable", meta.isUnbreakable());
        }
        return section;
    }
}
