package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

public class CMDRepairAll extends PolymerCommand {
    public CMDRepairAll(@NotNull String name) {
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] strings) {
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
