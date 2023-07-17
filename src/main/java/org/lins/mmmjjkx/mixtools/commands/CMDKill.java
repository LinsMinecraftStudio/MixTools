package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class CMDKill extends PolymerCommand {
    public CMDKill(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (args.length==0){
            return getPlayerNames();
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
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (args.length==0) {
                if (p != null) {
                    p.setHealth(0);
                    return true;
                }else {
                    sendMessage(sender,"Command.SpecifyPlayer");
                    return false;
                }
            } else if (args.length==1) {
                Player p2 = findPlayer(p, args[0]);
                if (p2 != null) {
                    p2.setHealth(0);
                    sendMessage(sender,"Command.Kill",p2.getName());
                    return true;
                }
            }else {
                Polymer.messageHandler.sendMessage(sender,"Command.ArgError");
                return false;
            }
            return false;
        }
        return false;
    }
}
