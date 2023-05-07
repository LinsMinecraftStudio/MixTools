package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;

public class CMDRepair implements MixCommandExecutor {
    @Override
    public String name() {
        return "repair";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                ItemStack stack = p.getInventory().getItemInMainHand();
                ItemMeta meta = stack.getItemMeta();
                if (stack.getType().isAir()) {
                    sendMessage(p,"Item.AIR");
                    return false;
                }
                Damageable damageable = (Damageable) meta;
                if (damageable == null || !damageable.hasDamage()){
                    sendMessage(sender, "Item.Unrepairable");
                    return false;
                }
                damageable.setDamage(0);
                stack.setItemMeta(damageable);
                return true;
            }
        }
        return false;
    }
}
