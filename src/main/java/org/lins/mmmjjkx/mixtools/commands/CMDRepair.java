package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

public class CMDRepair extends PolymerCommand {

    public CMDRepair(@NotNull String name) {
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
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
