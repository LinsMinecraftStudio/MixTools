package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;

public class CMDRepairAll implements MixCommandExecutor {
    @Override
    public String name() {
        return "repairall";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                ItemStack[] contents = p.getInventory().getContents();
                for (ItemStack stack : contents) {
                    ItemMeta meta = stack.getItemMeta();
                    if (stack.getType().isAir()) continue;
                    Damageable damageable = (Damageable) meta;
                    if (damageable == null || !damageable.hasDamage()) continue;
                    damageable.setDamage(0);
                    stack.setItemMeta(damageable);
                }
                sendMessage(sender, "Item.RepairAll");
                return true;
            }
        }
        return false;
    }
}
