package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CMDItemName implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> s = new ArrayList<>();
        if (sender instanceof Player p){
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (!hand.getType().equals(Material.AIR) && hand.hasItemMeta()) {
                ItemMeta meta = hand.getItemMeta();
                if (meta != null) {
                    if (args.length==1 & meta.hasDisplayName()) {
                        return Collections.singletonList(ChatColor.stripColor(meta.getDisplayName()));
                    }
                }
            }
        }
        return s;
    }

    @Override
    public String name() {
        return "itemname";
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
                ItemStack hand = p.getInventory().getItemInMainHand();
                if (!hand.getType().equals(Material.AIR)) {
                    if (args.length==1) {
                        ItemMeta meta = hand.getItemMeta();
                        meta.setDisplayName(MixTools.messageHandler.legacyColorize(args[0]));
                        hand.setItemMeta(meta);
                        return true;
                    }else {
                        sendMessage(p, "Command.ArgError");
                        return false;
                    }
                }else {
                    sendMessage(p,"Item.AIR");
                    return false;
                }
            }
        }
        return false;
    }
}
