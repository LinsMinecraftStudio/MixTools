package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;
import org.lins.mmmjjkx.mixtools.utils.MixStringUtil;

import java.util.ArrayList;
import java.util.List;

public class CMDItemName implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> s = new ArrayList<>();
        if (sender instanceof Player p){
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (!hand.getType().equals(Material.AIR) && hand.hasItemMeta()) {
                ItemMeta meta = hand.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    if (args.length==0) {
                        s.add(MixStringUtil.unformatString(meta.getDisplayName()));
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
                if (!hand.getType().equals(Material.AIR) && hand.getItemMeta() != null) {
                    if (args.length==1) {
                        ItemMeta meta = hand.getItemMeta();
                        meta.setDisplayName(MixTools.messageHandler.colorize(args[0]));
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
