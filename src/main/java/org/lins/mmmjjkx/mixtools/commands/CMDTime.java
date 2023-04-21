package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;
import org.lins.mmmjjkx.mixtools.utils.MixStringUtil;

import java.util.ArrayList;
import java.util.List;

public class CMDTime implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return StringUtil.copyPartialMatches(args[0],List.of("set","add"),new ArrayList<>());
        }
        return null;
    }

    @Override
    public String name() {
        return "time";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                if (args.length==2){
                    World w = p.getWorld();
                    switch (args[0]) {
                        case "set" -> {
                            long time = MixStringUtil.deserializeTime(args[0]);
                            w.setTime(time);
                            sendMessage(sender, "Command.TimeSet", time);
                            return true;
                        } case "add" -> {
                            long time = w.getTime();
                            time += MixStringUtil.deserializeTime(args[1]);
                            w.setTime(time);
                            sendMessage(sender, "Command.TimeSet", time);
                            return true;
                        }
                    }
                }else {
                    sendMessage(sender,"Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }
}
