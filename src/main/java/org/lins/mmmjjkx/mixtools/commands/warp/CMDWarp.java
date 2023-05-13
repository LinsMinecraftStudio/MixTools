package org.lins.mmmjjkx.mixtools.commands.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsWarp;

import java.util.List;

public class CMDWarp implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length==1){
            return copyPartialMatches(strings[0], MixTools.warpManager.getWarpNames());
        }
        return null;
    }

    @Override
    public String name() {
        return "warp";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (hasPermission(commandSender)) {
            Player p = toPlayer(commandSender);
            if (p != null) {
                if (strings.length==1){
                    WarpManager warpManager = MixTools.warpManager;
                    MixToolsWarp warp = warpManager.getWarpByName(strings[0]);
                    if (warp == null) {
                        sendMessage(commandSender, "Warp.notFound");
                        return false;
                    }
                    p.teleport(warp.location());
                    sendMessage(commandSender, "Warp.Teleported", strings[0]);
                    return true;
                }else {
                    sendMessage(commandSender, "Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }
}
