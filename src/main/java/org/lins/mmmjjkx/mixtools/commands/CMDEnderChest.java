package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.List;

public class CMDEnderChest implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==0){
            return getPlayerNames();
        }
        return null;
    }

    @Override
    public String name() {
        return "enderchest";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==0) {
            if (hasPermission(sender)) {
                Player p = toPlayer(sender);
                if (p != null) {
                    p.openInventory(p.getEnderChest());
                    return true;
                }
            }
            return false;
        }else if (args.length==1) {
            if (hasSubPermission(sender,"others")) {
                Player p = toPlayer(sender);
                Player p2 = findPlayer(sender, args[0]);
                if (p != null && p2 != null) {
                    p.openInventory(p2.getEnderChest());
                    return true;
                }
            }
            return false;
        }else {
            sendMessage(sender,"Command.ArgError");
            return false;
        }
    }
}
