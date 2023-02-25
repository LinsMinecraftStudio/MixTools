package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;
import org.lins.mmmjjkx.mixtools.utils.MixStringUtils;

import java.util.ArrayList;
import java.util.List;

public class CMDItemName implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> s = new ArrayList<>();
        if (sender instanceof Player){
            Player p = (Player) sender;
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand != null&&!hand.getType().equals(Material.AIR)&&hand.hasItemMeta()) {
                ItemMeta meta = hand.getItemMeta();
                if (meta.hasDisplayName()) {
                    if (args.length==0) {
                        s.add(MixStringUtils.unformatString(meta.getDisplayName()));
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
    public String usage() {
        return "/<command> <itemName>";
    }

    @Override
    public List<String> aliases() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                ItemStack hand = p.getInventory().getItemInMainHand();
                if (hand != null&&!hand.getType().equals(Material.AIR)&&hand.hasItemMeta()) {
                    if (args.length==1) {
                        ItemMeta meta = hand.getItemMeta();
                        meta.setDisplayName(MixTools.messageHandler.colorize(args[0]));
                        return true;
                    }else {
                        sendMessage(p, "Command.NoEnoughOrTooManyArgs");
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
