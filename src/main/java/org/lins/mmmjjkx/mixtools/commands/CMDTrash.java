package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;

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
                Inventory i = Bukkit.createInventory(null,36,getMessage("GUI.Trash"));
                sendMessage(p, "GUI.OpenTrashBin");
                p.openInventory(i);
                return true;
            }
        }
        return false;
    }
}
