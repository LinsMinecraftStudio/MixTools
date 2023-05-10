package org.lins.mmmjjkx.mixtools.commands.speed;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public abstract class SpeedCMD implements MixTabExecutor {
    private final List<String> speedList = new ArrayList<>();
    public SpeedCMD(){
        for (int i = 1; i < maxSpeed(); i++) {
            speedList.add(String.valueOf(i));
        }
        speedList.add(String.valueOf(maxSpeed()));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],speedList);
        } else if (args.length==2) {
            return copyPartialMatches(args[1],getPlayerNames());
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==1) {
                Player p = toPlayer(sender);
                if (p!=null) {return changeSpeed(p, args[0]);}
                return false;
            } else if (args.length==2) {
                Player p = findPlayer(sender,args[1]);
                if (p!=null){return changeSpeed(p,args[0]);}
            } else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }

    abstract int maxSpeed();

    abstract int minSpeed();

    abstract void changePlayerSpeed(Player p, double speed);

    private boolean changeSpeed(Player p, String s){
        double speed;
        try {speed = Double.parseDouble(s);
        }catch (NumberFormatException e){
            sendMessage(p,"Value.NotDouble",1);
            return false;
        }
        if (speed < minSpeed()){
            sendMessage(p,"Value.TooLow");
            return false;
        }else if (speed > maxSpeed()){
            sendMessage(p,"Value.TooHigh");
            return false;
        }else {
            changePlayerSpeed(p, speed);
            return true;
        }
    }
}
