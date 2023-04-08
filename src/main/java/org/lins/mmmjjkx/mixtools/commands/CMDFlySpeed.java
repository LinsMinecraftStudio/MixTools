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

public class CMDFlySpeed implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> speedList = new ArrayList<>();
        if (args.length==1){
            speedList.add("1");
            speedList.add("2");
            speedList.add("3");
            speedList.add("4");
            speedList.add("5");
            return StringUtil.copyPartialMatches(args[0],speedList,new ArrayList<>());
        }
        return null;
    }

    @Override
    public String name() {
        return "flyspeed";
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
                if (args.length==1){
                    float speed;
                    try {speed = Float.parseFloat(args[0]);
                    }catch (NumberFormatException e){
                        sendMessage(sender,"Value.NotFloat",1);
                        return false;
                    }
                    if (speed <= 0){
                        sendMessage(sender,"Value.TooLow");
                        return false;
                    }else if (speed > 5){
                        sendMessage(sender,"Value.TooHigh");
                        return false;
                    }else {
                        float speed2 = speed / 5;
                        p.setFlySpeed(speed2);
                        sendMessage(p, "Command.FlyingSpeedSet", speed);
                        return true;
                    }
                }else {
                    sendMessage(p,"Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }
}
