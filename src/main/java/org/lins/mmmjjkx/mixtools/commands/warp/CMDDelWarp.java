package org.lins.mmmjjkx.mixtools.commands.warp;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;

import java.util.ArrayList;
import java.util.List;

public class CMDDelWarp extends PolymerCommand {
    public CMDDelWarp(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (strings.length==1){
            return copyPartialMatches(strings[0], MixTools.warpManager.getWarpNames());
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
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
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
