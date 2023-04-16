package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDBurn implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return StringUtil.copyPartialMatches(args[0],getPlayerNames(),new ArrayList<>());
        }else if (args.length==2){
            return StringUtil.copyPartialMatches(args[1],List.of("1","5","10"),new ArrayList<>());
        }
        return null;
    }

    @Override
    public String name() {
        return "burn";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==2) {
                Player p = findPlayer(sender,args[0]);
                if (p != null){
                    int sec = toInteger(sender,args[1],2);
                    if (!(sec<1)){
                        int tick = sec*20;
                        p.setFireTicks(tick);
                        return true;
                    }
                }
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
