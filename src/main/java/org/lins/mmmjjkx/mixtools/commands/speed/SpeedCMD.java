package org.lins.mmmjjkx.mixtools.commands.speed;

import io.github.linsminecraftstudio.polymer.Polymer;
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
        if (hasPermission(sender)) {
            Player p = toPlayer(sender);
            return switch (args.length) {
                case 0:
                    if (p != null) {
                        sendPlayerInfo(p);
                        yield true;
                    }
                    yield false;
                case 1:
                    if (p != null) {
                        changeSpeed(p, toDouble(sender, args[0], 1));
                        yield true;
                    }
                    yield false;
                case 2:
                    Player p2 = findPlayer(sender,args[1]);
                    if (p2 != null) {
                        changeSpeed(p2, toDouble(sender, args[0], 1));
                        yield true;
                    }
                    yield false;
                default:
                    Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
                    yield false;
            };
        }
        return false;
    }

    abstract int maxSpeed();

    abstract int minSpeed();

    abstract void changePlayerSpeed(Player p, double speed);
    abstract void sendPlayerInfo(Player p);

    private void changeSpeed(Player p, double speed){
        if (speed < minSpeed()){
            Polymer.messageHandler.sendMessage(p,"Value.TooLow");
        }else if (speed > maxSpeed()){
            Polymer.messageHandler.sendMessage(p,"Value.TooHigh",1);
        }else {
            changePlayerSpeed(p, speed);
        }
    }
}
