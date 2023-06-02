package org.lins.mmmjjkx.mixtools.commands.speed;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public abstract class SpeedCMD extends PolymerCommand {
    private final List<String> speedList = new ArrayList<>();
    public SpeedCMD(String name){
        super(name);
        for (int i = 1; i < maxSpeed(); i++) {
            speedList.add(String.valueOf(i));
        }
        speedList.add(String.valueOf(maxSpeed()));
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],speedList);
        } else if (args.length==2) {
            return copyPartialMatches(args[1],getPlayerNames());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==1) {
                Player p = toPlayer(sender);
                if (p!=null) {return changeSpeed(p, toDouble(sender,args[0],1));}
                return false;
            } else if (args.length==2) {
                Player p = findPlayer(sender,args[1]);
                if (p!=null){return changeSpeed(p, toDouble(sender,args[0],1));}
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

    private boolean changeSpeed(Player p, double speed){
        if (speed < minSpeed()){
            return false;
        }else if (speed > maxSpeed()){
            sendMessage(p,"Value.TooHigh",1);
            return false;
        }else {
            changePlayerSpeed(p, speed);
            return true;
        }
    }
}
