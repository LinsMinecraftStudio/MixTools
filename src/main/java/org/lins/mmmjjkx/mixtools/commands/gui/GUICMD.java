package org.lins.mmmjjkx.mixtools.commands.gui;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.List;

public abstract class GUICMD extends PolymerCommand {
    public GUICMD(@NotNull String name) {
        super(name);
    }

    public GUICMD(@NotNull String name, List<String> aliases){
        super(name, aliases);
    }

    public abstract void openGUI(Player player);

    public abstract void openGUI2(Player player, Player player2);

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (hasPermission(sender)) {
            if (args.length == 0) {
                Player p = toPlayer(sender);
                if (p != null) {
                    openGUI(p);
                    return true;
                }
                return false;
            } else if (args.length == 1) {
                Player p = toPlayer(sender);
                Player p2 = findPlayer(sender, args[0]);
                if (p != null && p2 != null) {
                    openGUI2(p, p2);
                    return true;
                }
                return false;
            } else {
                Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
