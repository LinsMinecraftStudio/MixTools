package org.lins.mmmjjkx.mixtools.commands.speed;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDWalkSpeed implements MixTabExecutor {
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
            speedList.add("5");
            speedList.add("6");
            speedList.add("7");
            speedList.add("8");
            speedList.add("9");
            speedList.add("10");
            return StringUtil.copyPartialMatches(args[0],speedList,new ArrayList<>());
        } else if (args.length==2) {
            return StringUtil.copyPartialMatches(args[1],getPlayerNames(),new ArrayList<>());
        }
        return null;
    }

    @Override
    public String name() {
        return "walkspeed";
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
        float defaultSpeed = 0.10000000149011612f;
        try {speed = Float.parseFloat(s);
        }catch (NumberFormatException e){
            sendMessage(p,"Value.NotFloat",1);
            return false;
        }
        if (speed < 0){
            sendMessage(p,"Value.TooLow");
            return false;
        }else if (speed > 10){
            sendMessage(p,"Value.TooHigh");
            return false;
        }else {
            p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed * defaultSpeed);
            sendMessage(p, "Command.WalkSpeedSet", speed);
            return true;
        }
    }
}
