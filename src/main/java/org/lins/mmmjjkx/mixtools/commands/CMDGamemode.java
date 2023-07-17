package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class CMDGamemode extends PolymerCommand {
    public CMDGamemode(@NotNull String name, List<String> aliases) {
        super(name, aliases);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String[] args) {
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
        return new ArrayList<>();
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        Player p = toPlayer(sender);
        if (hasPermission(p)){
            switch (args.length) {
                case 0 -> {
                    if (p!=null) {
                        sendMessage(sender, "Gamemode.Current",p.getGameMode().toString());
                        return true;
                    }
                }
                case 1 -> {
                    if (p!=null) {
                        GameMode mode;
                        try {
                            int in = Integer.parseInt(args[0]);
                            mode = intToGameMode(in);
                        } catch (NumberFormatException e) {
                            mode = stringToGameMode(args[0]);
                        }
                        if (mode != null) {
                            Component s = MixTools.messageHandler.getColoredReplaceToOtherMessages("GameMode.Change", "GameMode." +
                                    mode.toString().toLowerCase());
                            p.setGameMode(mode);
                            p.sendMessage(s);
                            return true;
                        } else {
                            Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
                            return false;
                        }
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
                            Component s2 = MixTools.messageHandler.getColoredReplaceToOtherMessages("GameMode.Change","GameMode."+
                                    mode2.toString().toLowerCase());
                            p2.setGameMode(mode2);
                            p.sendMessage(s2);
                            return true;
                        }else {
                            Polymer.messageHandler.sendMessage(sender,"Command.ArgError");
                            return false;
                        }
                    }
                    return false;
                }
                default -> {
                    Polymer.messageHandler.sendMessage(sender,"Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }

    private GameMode intToGameMode(int mode) {
        return switch (mode) {
            case 0 -> GameMode.SURVIVAL;
            case 1 -> GameMode.CREATIVE;
            case 2 -> GameMode.ADVENTURE;
            case 3 -> GameMode.SPECTATOR;
            default -> null;
        };
    }

    private GameMode stringToGameMode(String s) {
        GameMode mode;
        try {mode = GameMode.valueOf(s.toUpperCase());
        }catch (Exception e){return null;}
        return mode;
    }
}
