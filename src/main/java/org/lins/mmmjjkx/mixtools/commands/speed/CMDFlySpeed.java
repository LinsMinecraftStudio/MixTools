package org.lins.mmmjjkx.mixtools.commands.speed;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

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
            if (args.length==1) {
                Player p = toPlayer(sender);
                if (p!=null) {
                    return changeSpeed(p, args[0]);
                }
                return false;
            } else if (args.length==2) {
                Player p = findPlayer(sender,args[1]);
                if (p!=null){
                    return changeSpeed(p,args[0]);
                }
            } else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }

    private boolean changeSpeed(Player p,String s){
        float speed;
        try {speed = Float.parseFloat(s);
        }catch (NumberFormatException e){
            sendMessage(p,"Value.NotFloat",1);
            return false;
        }
        if (speed < 0){
            sendMessage(p,"Value.TooLow");
            return false;
        }else if (speed > 5){
            sendMessage(p,"Value.TooHigh");
            return false;
        }else {
            float speed2 = speed / 5;
            p.setFlySpeed(speed2);
            sendMessage(p, "Command.WalkSpeedSet", speed);
            return true;
        }
    }
}
