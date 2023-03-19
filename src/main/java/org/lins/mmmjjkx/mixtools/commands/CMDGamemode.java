package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDGamemode implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length==0){
            for (GameMode mode: GameMode.values()) {
                result.add(mode.name());
            }
            return result;
        }
        if (args.length==1){
            return getPlayerNames();
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
                        mode = GameMode.valueOf(args[0].toUpperCase());
                    }
                    if (mode != null) {
                        String s = MixTools.messageHandler.getColoredReplaceToOtherMessages("GameMode.Change",false,"GameMode."+
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
                            mode2 = GameMode.valueOf(args[0].toUpperCase());
                        }
                        Player p2 = findPlayer(p, args[1]);
                        if (mode2 != null) {
                            String s2 = MixTools.messageHandler.getColoredReplaceToOtherMessages("GameMode.Change",false,"GameMode."+
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
}
