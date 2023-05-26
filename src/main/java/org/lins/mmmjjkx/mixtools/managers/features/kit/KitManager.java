package org.lins.mmmjjkx.mixtools.managers.features.kit;

import com.google.common.io.Files;
import io.github.linsminecraftstudio.polymer.itemstack.ItemStackConverter;
import io.github.linsminecraftstudio.polymer.utils.ListUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsKit;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitManager {
    private List<MixToolsKit> kits = new ArrayList<>();
    public KitManager(){
        loadKits();
    }

    public List<MixToolsKit> getKits() {
        return kits;
    }

    public void giveKit(Player player, MixToolsKit kit){
        if (kit == null) return;
        Map<Integer, ItemStack> stacks = kit.items();
        for (int i : stacks.keySet()) {
            player.getInventory().setItem(i, stacks.get(i));
        }
        if (kit.hasEquipment()) {
            Map<EquipmentSlot, ItemStack> equipment = kit.equipment();
            for (EquipmentSlot slot : equipment.keySet()) {
                ItemStack stack = equipment.get(slot);
                player.getInventory().setItem(slot, stack);
            }
        }
        MixTools.messageHandler.sendMessage(player, "Kit.ReceivedKit", kit.kitName());
    }

    @Nullable
    public MixToolsKit getKitByName(String name){
        return ListUtil.listGetIf(kits, k -> k.kitName().equals(name)).orElse(null);
    }

    public boolean removeKit(String name){
        if (kits.removeIf(p -> p.kitName().equals(name))){
            File file = getKitFile(name);
            if (file != null) file.delete();
            return true;
        }
        MixTools.warn("The kit "+ name + " is not found.");
        return false;
    }

    void loadKits(){
        kits = new ArrayList<>();
        File file = new File(MixTools.INSTANCE.getDataFolder(), "kits");
        if (!file.exists()) {
            file.mkdirs();
            return;
        }
        File[] files = file.listFiles();
        if (files == null) return;

        for (File kitFile : files) {
            MixTools.log(kitFile.getAbsolutePath() + "/" + kitFile.getName());
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            try {yamlConfiguration.load(kitFile);
            } catch (Exception e) {
                MixTools.warn("Failed to load the kit file " + kitFile.getName() + ": " + e.getMessage());
                continue;
            }
            ConfigurationSection section = yamlConfiguration.getConfigurationSection("slot");
            if (section == null) {
                MixTools.warn("Kit " + kitFile.getName() + " cannot be loaded, because there were no slot configuration.");
                continue;
            }
            Map<Integer,ItemStack> itemStacks = new HashMap<>();
            for (String key : section.getKeys(false)) {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                if (itemSection == null) continue;
                ItemStack stack = ItemStackConverter.toItemStack(itemSection);
                itemStacks.put(Integer.parseInt(key),stack);
            }
            ConfigurationSection equipmentSection = yamlConfiguration.getConfigurationSection("equipment");
            kits.add(new MixToolsKit(Files.getNameWithoutExtension(kitFile.getName()), itemStacks, readEquipment(equipmentSection)));
        }
    }

    @Nullable
    private File getKitFile(String name){
        File file = new File(MixTools.INSTANCE.getDataFolder(), "kits");
        if (!file.exists()) {
            file.mkdirs();
            return null;
        }
        File kitFile = new File(file, name+".yml");
        if (kitFile.exists()){
            return kitFile;
        }
        return null;
    }

    @Nullable
    private Map<EquipmentSlot,ItemStack> readEquipment(ConfigurationSection section){
        if (section == null) return null;
        ConfigurationSection headSection = section.getConfigurationSection("head");
        ConfigurationSection chestSection = section.getConfigurationSection("chest");
        ConfigurationSection legsSection = section.getConfigurationSection("legs");
        ConfigurationSection feetSection = section.getConfigurationSection("feet");
        ConfigurationSection offHandSection = section.getConfigurationSection("offHand");
        Map<EquipmentSlot,ItemStack> equipmentSlotItemStackMap = new HashMap<>();
        if (headSection != null){
            equipmentSlotItemStackMap.put(EquipmentSlot.HEAD, ItemStackConverter.toItemStack(headSection));
        }
        if (chestSection != null) {
            equipmentSlotItemStackMap.put(EquipmentSlot.CHEST, ItemStackConverter.toItemStack(chestSection));
        }
        if (legsSection != null) {
            equipmentSlotItemStackMap.put(EquipmentSlot.LEGS, ItemStackConverter.toItemStack(legsSection));
        }
        if (feetSection != null) {
            equipmentSlotItemStackMap.put(EquipmentSlot.FEET, ItemStackConverter.toItemStack(feetSection));
        }
        if (offHandSection != null) {
            equipmentSlotItemStackMap.put(EquipmentSlot.OFF_HAND, ItemStackConverter.toItemStack(offHandSection));
        }
        return equipmentSlotItemStackMap;
    }
}
