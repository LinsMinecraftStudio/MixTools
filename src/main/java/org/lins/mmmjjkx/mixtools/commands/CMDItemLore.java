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

public class CMDItemLore implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (sender instanceof Player p) {
            ItemStack i = p.getInventory().getItemInMainHand();
            if (!i.getType().equals(Material.AIR)) {
                ItemMeta m = i.getItemMeta();
                String lore = "";
                if (m.getLore()!=null) {
                    try {lore = m.getLore().get(args.length-1);
                    } catch (Exception ignored) {}
                    if (!lore.isBlank()) {
                        return List.of(MixStringUtil.unformatString(lore));
                    }
                }
            }
        }
        return null;
    }
    @Override
    public String name() {
        return "itemlore";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null) {
                ItemStack i = p.getInventory().getItemInMainHand();
                if (!i.getType().equals(Material.AIR)){
                    ItemMeta m = i.getItemMeta();
                    List<String> lines = new ArrayList<>();
                    if (args.length>0){
                        for (String arg : args) {
                            String str = MixTools.messageHandler.colorize(arg.replaceAll(":space:"," "));
                            lines.add(str);
                        }
                        m.setLore(lines);
                        i.setItemMeta(m);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
