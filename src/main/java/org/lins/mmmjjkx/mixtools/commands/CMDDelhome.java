package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CMDDelhome implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (sender instanceof Player p) {
            if (args.length == 0) {
                Set<String> names = MixTools.dataManager.getPlayerOwnedHomesName(p);
                l.addAll(names);
            }
        }
        return l;
    }

    @Override
    public String name() {
        return "delhome";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (hasPermission(sender)) {
            Player p = toPlayer(sender);
            if (p != null){
                if (args.length == 1) {
                    MixTools.dataManager.removeHome(p, args[0]);
                    return true;
                }else {
                    sendMessage(p, "Command.NoEnoughOrTooManyArgs");
                    return false;
                }
            }
        }
        return false;
    }
}
