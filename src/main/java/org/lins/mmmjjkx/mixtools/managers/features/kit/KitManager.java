package org.lins.mmmjjkx.mixtools.managers.features.kit;

import com.google.common.io.Files;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsKit;
import org.lins.mmmjjkx.mixtools.utils.ItemStackBuilder;
import org.lins.mmmjjkx.mixtools.utils.OtherUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

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
        List<ItemStack> stacks = kit.items();
        player.getInventory().addItem(stacks.toArray(ItemStack[]::new));
        if (kit.hasEquipment()) {
            Map<EquipmentSlot, ItemStack> equipment = kit.equipment();
            for (EquipmentSlot slot : equipment.keySet()) {
                ItemStack stack = equipment.get(slot);
                player.getInventory().setItem(slot, stack);
            }
        }
        MixTools.messageHandler.sendMessage(player, "Kit.ReceivedKit", kit);
    }

    @Nullable
    public MixToolsKit getKitByName(String name){
        return OtherUtil.listGetIf(kits, k -> k.kitName().equals(name)).orElse(null);
    }

    public boolean removeKit(String name){
        if (kits.removeIf(p -> p.kitName().equals(name))){
            getKitFile(name).delete();
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
            List<ItemStack> itemStacks = new ArrayList<>();
            for (String key : section.getKeys(false)) {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                if (itemSection == null) continue;
                ItemStack stack = ItemStackBuilder.toItemStack(itemSection);
                itemStacks.add(stack);
            }
            ConfigurationSection equipmentSection = yamlConfiguration.getConfigurationSection("equipment");
            kits.add(new MixToolsKit(Files.getNameWithoutExtension(kitFile.getName()), itemStacks, readEquipment(equipmentSection)));
        }
    }

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
