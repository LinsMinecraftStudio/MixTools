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

public class CMDItemLore implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack i = p.getInventory().getItemInMainHand();
            if (i != null && !i.getType().equals(Material.AIR) && i.hasItemMeta()) {
                ItemMeta m = i.getItemMeta();
                return List.of(MixStringUtils.unformatString(m.getLore().get(args.length)));
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
    public String usage() {
        return "/<command> <lines>";
    }

    @Override
    public List<String> aliases() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null) {
                ItemStack i = p.getInventory().getItemInMainHand();
                if (i != null&&!i.getType().equals(Material.AIR)&&i.hasItemMeta()){
                    ItemMeta m = i.getItemMeta();
                    List<String> lines = new ArrayList<>();
                    if (args.length>0){
                        for (String arg : args) {
                            String str = MixTools.messageHandler.colorize(arg.replaceAll(":space:"," "));
                            lines.add(str);
                        }
                        m.setLore(lines);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
