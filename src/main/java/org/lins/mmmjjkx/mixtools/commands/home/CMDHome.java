package org.lins.mmmjjkx.mixtools.commands.home;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CMDHome extends PolymerCommand {
    public CMDHome(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player p = toPlayer(sender);
        if (p != null){
            if (args.length==1){
                Location home = MixTools.getDataManager().getHomeLocation(p.getUniqueId(),args[0]);
                if (home != null){
                    p.teleport(home);
                    sendMessage(p,"Home.Teleported",args[0]);
                    return true;
                }else {
                    sendMessage(p,"Home.NotExists");
                    return false;
                }
            }else {
                sendMessage(p,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
