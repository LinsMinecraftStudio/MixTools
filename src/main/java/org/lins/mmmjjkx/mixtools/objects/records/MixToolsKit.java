package org.lins.mmmjjkx.mixtools.objects.records;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public record MixToolsKit(String kitName, List<ItemStack> items, Map<EquipmentSlot,ItemStack> equipment, ItemStack offHand) {
    public boolean hasEquipment(){
        return !equipment.isEmpty();
    }

    public boolean hasOffHandStack(){
        return offHand != null;
    }
}
