package org.lins.mmmjjkx.mixtools.commands.teleport;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class CMDTPAll extends PolymerCommand {
    public CMDTPAll(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],getPlayerNames());
        }
        return new ArrayList<>();
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {}

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)) {
            if (args.length==0) {
                Player p = toPlayer(sender);
                if (p != null) {
                    for (Player ps: Bukkit.getOnlinePlayers()) {
                        ps.teleport(p);
                    }
                    MixTools.messageHandler.broadcastMessage("Location.TeleportAll",p.getName());
                    return true;
                }
            }else if (args.length==1) {
                Player p = findPlayer(sender, args[0]);
                if (p != null) {
                    for (Player ps: Bukkit.getOnlinePlayers()) {
                        ps.teleport(p);
                    }
                    MixTools.messageHandler.broadcastMessage("Location.TeleportAll",p.getName());
                    return true;
                }
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
