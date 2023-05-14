package org.lins.mmmjjkx.mixtools.objects.records;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public record MixToolsKit(String kitName, Map<Integer,ItemStack> items, Map<EquipmentSlot,ItemStack> equipment) {
    public boolean hasEquipment(){
        return !equipment.isEmpty();
    }
}
