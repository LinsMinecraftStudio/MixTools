package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.*;

public class CMDTrash extends PolymerCommand {
    public CMDTrash(@NotNull String name) {
        super(name);
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                p.closeInventory();
                Inventory inv = Bukkit.createInventory(null, MixTools.settingsManager.getInt(TRASH_SIZE),
                        MixTools.messageHandler.getColored("GUI.TrashBin"));
                List<Integer> slots = MixTools.settingsManager.getIntList(TRASH_PUT_ITEM_SLOTS);
                if (!slots.isEmpty()) {
                    ItemStack stack = MixTools.settingsManager.getItemStack(TRASH_ITEM);
                    ItemMeta meta = stack.getItemMeta();
                    meta.displayName(MixTools.settingsManager.getComponent(TRASH_ITEM_NAME,true));
                    stack.setItemMeta(meta);
                    ItemStack closeButton = MixTools.settingsManager.getBoolean(TRASH_CLOSE_BUTTON_ENABLED) ?
                            MixTools.settingsManager.getItemStack(TRASH_CLOSE_BUTTON_ITEM) : null;
                    if (closeButton != null){
                        ItemMeta meta2 = closeButton.getItemMeta();
                        meta2.displayName(MixTools.settingsManager.getComponent(TRASH_CLOSE_BUTTON_NAME,true));
                    }
                    int closeButtonSlot = MixTools.settingsManager.getInt(TRASH_CLOSE_BUTTON_SLOT);
                    if (MixTools.settingsManager.getBoolean(TRASH_CLOSE_BUTTON_ENABLED)) {
                        if (slots.contains(closeButtonSlot)) {
                            slots.remove(closeButtonSlot);
                        }
                    }
                    for (int i : slots) {
                        if (i >= MixTools.settingsManager.getInt(TRASH_SIZE)||i < 0){
                            continue;
                        }
                        inv.setItem(i, stack);
                    }
                    if (closeButton != null) {
                        if (closeButtonSlot < MixTools.settingsManager.getInt(TRASH_SIZE)||closeButtonSlot >= 0) {
                            inv.setItem(closeButtonSlot, closeButton);
                        }
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
