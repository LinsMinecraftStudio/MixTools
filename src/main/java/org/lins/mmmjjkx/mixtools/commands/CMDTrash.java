package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;

import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.*;

public class CMDTrash implements MixCommandExecutor {
    @Override
    public String name() {
        return "trash";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                p.closeInventory();
                Inventory inv = Bukkit.createInventory(null, MixTools.settingsManager.getInt(TRASH_SIZE),
                        getMessage("GUI.TrashBin"));
                List<Integer> slots = MixTools.settingsManager.getIntList(TRASH_PUT_THING_SLOTS);
                if (!slots.isEmpty()) {
                    ItemStack stack = MixTools.settingsManager.getItemStack(TRASH_PUT_THING);
                    for (int i : slots) {
                        if (i >= MixTools.settingsManager.getInt(TRASH_SIZE)||i < 0){
                            continue;
                        }
                        inv.setItem(i, stack);
                    }
                }
                sendMessage(p, "GUI.OpenTrashBin");
                p.openInventory(inv);
                return true;
            }
        }
        return false;
    }
}
