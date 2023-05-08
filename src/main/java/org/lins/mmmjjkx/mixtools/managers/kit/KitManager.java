package org.lins.mmmjjkx.mixtools.managers.kit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsKit;
import org.lins.mmmjjkx.mixtools.utils.ItemStackBuilder;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitManager {
    private final List<MixToolsKit> kits = new ArrayList<>();
    public KitManager(){
        loadKits();
    }

    public void giveKit(Player player, String name){
        MixToolsKit kit = getKitByName(name);
        if (kit == null){
            MixTools.warn("The kit "+ name + " is not found.");
            return;
        }
        List<ItemStack> stacks = kit.items();
        player.getInventory().addItem(stacks.toArray(ItemStack[]::new));
        if (kit.hasOffHandStack()) player.getInventory().setItemInOffHand(kit.offHand());
        if (kit.hasEquipment()) {
            Map<EquipmentSlot, ItemStack> equipment = kit.equipment();
            for (EquipmentSlot slot : equipment.keySet()) {
                ItemStack stack = equipment.get(slot);
                player.getInventory().setItem(slot, stack);
            }
        }
    }

    @Nullable
    public MixToolsKit getKitByName(String name){
        for(MixToolsKit kit : kits){
            if (kit.kitName().equals(name)) return kit;
        }
        return null;
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
            ItemStack offHand = null;
            for (String key: section.getKeys(false)) {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                if (itemSection == null) continue;
                ItemStack stack = ItemStackBuilder.toItemStack(itemSection);
                itemStacks.add(stack);
            }
            ConfigurationSection equipmentSection = yamlConfiguration.getConfigurationSection("equipment");
            kits.add(new MixToolsKit(kitFile.getName(), itemStacks, readEquipment(equipmentSection) ,offHand));
        }
    }

    private Map<EquipmentSlot,ItemStack> readEquipment(ConfigurationSection section){
        if (section == null) return new HashMap<>();
        ConfigurationSection headSection = section.getConfigurationSection("head");
        ConfigurationSection chestSection = section.getConfigurationSection("chest");
        ConfigurationSection legsSection = section.getConfigurationSection("legs");
        ConfigurationSection feetSection = section.getConfigurationSection("feet");
        ConfigurationSection offHandSection = section.getConfigurationSection("offHand");
        Map<EquipmentSlot,ItemStack> equipmentSlotItemStackMap = new HashMap<>();
        if (headSection != null){
            equipmentSlotItemStackMap.put(EquipmentSlot.HEAD, ItemStackBuilder.toItemStack(headSection));
        }
        if (chestSection != null) {
            equipmentSlotItemStackMap.put(EquipmentSlot.CHEST, ItemStackBuilder.toItemStack(chestSection));
        }
        if (legsSection != null) {
            equipmentSlotItemStackMap.put(EquipmentSlot.LEGS, ItemStackBuilder.toItemStack(legsSection));
        }
        if (feetSection != null) {
            equipmentSlotItemStackMap.put(EquipmentSlot.FEET, ItemStackBuilder.toItemStack(feetSection));
        }
        if (offHandSection != null) {
            equipmentSlotItemStackMap.put(EquipmentSlot.OFF_HAND, ItemStackBuilder.toItemStack(offHandSection));
        }
        return equipmentSlotItemStackMap;
    }
}
