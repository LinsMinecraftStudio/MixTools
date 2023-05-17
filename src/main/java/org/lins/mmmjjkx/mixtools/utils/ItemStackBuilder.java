package org.lins.mmmjjkx.mixtools.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.Arrays;
import java.util.List;

public class ItemStackBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemStackBuilder(ItemStack item){
        this.itemStack = item;
        this.itemMeta = item.getItemMeta();
    }

    public ItemStackBuilder(Material material){
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public void name(String itemName){
        itemMeta.setDisplayName(itemName);
    }

    public void lore(List<String> lore){
        itemMeta.setLore(lore);
    }

    public void lore(String... lore){
        itemMeta.setLore(Arrays.stream(lore).toList());
    }

    public void unbreakable(boolean unbreakable){
        itemMeta.setUnbreakable(unbreakable);
    }

    public void enchantment(Enchantment enchantment, int lvl){
        itemStack.addUnsafeEnchantment(enchantment, lvl);
    }

    public void amount(int amount){
        itemStack.setAmount(amount);
    }

    public void flag(ItemFlag... flag){
        itemMeta.addItemFlags(flag);
    }

    public void setNameInConfig(String node){
        itemMeta.setDisplayName(MixTools.settingsManager.getString(node,true));
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
