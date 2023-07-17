package org.lins.mmmjjkx.mixtools.commands.home;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CMDDelhome extends PolymerCommand {
    public CMDDelhome(String name){
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 1) {
                Set<String> names = MixTools.getDataManager().getPlayerOwnedHomesName(p);
                return copyPartialMatches(args[0],names);
            }
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
        if (hasPermission(sender)) {
            Player p = toPlayer(sender);
            if (p != null){
                if (args.length == 1) {
                    MixTools.getDataManager().removeHome(p, args[0]);
                    sendMessage(sender, "Home.Removed", args[0]);
                    return true;
                }else {
                    Polymer.messageHandler.sendMessage(p, "Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }
}
