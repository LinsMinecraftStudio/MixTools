package org.lins.mmmjjkx.mixtools.commands.warp;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsWarp;

import java.util.ArrayList;
import java.util.List;

public class CMDWarp extends PolymerCommand {
    public CMDWarp(@NotNull String name) {
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
