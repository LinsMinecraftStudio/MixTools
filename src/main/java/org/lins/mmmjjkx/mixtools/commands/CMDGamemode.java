package org.lins.mmmjjkx.mixtools.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDGamemode implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> modes = new ArrayList<>();
        if (args.length==1){
            for (GameMode mode: GameMode.values()) {
                modes.add(mode.toString().toLowerCase());
            }
            return copyPartialMatches(args[0],modes);
        }
        if (args.length==2){
            return copyPartialMatches(args[1],getPlayerNames());
        }
        return null;
    }

    @Override
    public String name() {
        return "gamemode";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p = toPlayer(sender);
        if (hasPermission(p)){
            switch (args.length) {
                case 1 -> {
                    GameMode mode;
                    try {
                        int in = Integer.parseInt(args[0]);
                        mode = intToGameMode(in);
                    }catch (NumberFormatException e) {
                        mode = stringToGameMode(args[0]);
                    }
                    if (mode != null) {
                        Component s = MixTools.messageHandler.getColoredReplaceToOtherMessages("GameMode.Change",false,"GameMode."+
                                mode.toString().toLowerCase());
                        p.setGameMode(mode);
                        p.sendMessage(s);
                        return true;
                    }else {
                        sendMessage(sender,"Command.ArgError");
                        return false;
                    }
                }
                case 2 -> {
                    if (hasSubPermission(sender, "others")) {
                        GameMode mode2;
                        try {
                            int in = Integer.parseInt(args[0]);
                            mode2 = intToGameMode(in);
                        }catch (NumberFormatException e) {
                            mode2 = stringToGameMode(args[0]);
                        }
                        Player p2 = findPlayer(p, args[1]);
                        if (mode2 != null) {
                            Component s2 = MixTools.messageHandler.getColoredReplaceToOtherMessages("GameMode.Change",false,"GameMode."+
                                    mode2.toString().toLowerCase());
                            p2.setGameMode(mode2);
                            p.sendMessage(s2);
                            return true;
                        }else {
                            sendMessage(sender,"Command.ArgError");
                            return false;
                        }
                    }
                    return false;
                }
                default -> {
                    sendMessage(sender,"Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }

    private GameMode intToGameMode(int mode) {
        switch (mode) {
            case 0 -> {
                return GameMode.SURVIVAL;
            }
            case 1 -> {
                return GameMode.CREATIVE;
            }
            case 2 -> {
                return GameMode.ADVENTURE;
            }
            case 3 -> {
                return GameMode.SPECTATOR;
            }
        }
        return null;
    }

    private GameMode stringToGameMode(String s) {
        GameMode mode;
        try {mode = GameMode.valueOf(s.toUpperCase());
        }catch (Exception e){return null;}
        return mode;
    }
}
