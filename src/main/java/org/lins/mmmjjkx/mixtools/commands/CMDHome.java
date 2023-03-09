package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDHome implements MixTabExecutor {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            List<String> arg = new ArrayList<>();
            if (args.length == 0) {
                arg.addAll(MixTools.dataManager.getPlayerOwnedHomesName(p));
            }
            return arg;
        }
        return null;
    }

    @Override
    public String name() {
        return "home";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = toPlayer(sender);
        if (p != null){
            if (args.length==1){
                Location home = MixTools.dataManager.getHomeLocation(p.getName(),args[0]);
                if (home != null){
                    p.teleport(home);
                    sendMessage(p,"Home.Teleported",args[0]);
                    return true;
                }else {
                    sendMessage(p,"Home.NotExists");
                    return false;
                }
            }else {
                sendMessage(p,"Command.NoEnoughOrTooManyArgs");
                return false;
            }
        }
        return false;
    }
}
