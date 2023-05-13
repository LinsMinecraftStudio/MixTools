package org.lins.mmmjjkx.mixtools.commands.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.List;

public class CMDDelWarp implements MixTabExecutor {
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
        return "delwarp";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (hasPermission(commandSender)) {
            if (strings.length==1) {
                WarpManager warpManager = MixTools.warpManager;
                boolean result = warpManager.removeWarp(strings[0]);
                if (!result) {
                    sendMessage(commandSender, "Warp.notFound");
                    return false;
                }
                sendMessage(commandSender, "Warp.removed", strings[0]);
                return true;
            }
        }
        return false;
    }
}
