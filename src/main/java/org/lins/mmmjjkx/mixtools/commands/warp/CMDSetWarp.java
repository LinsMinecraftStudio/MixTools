package org.lins.mmmjjkx.mixtools.commands.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;

public class CMDSetWarp implements MixCommandExecutor {

    @Override
    public String name() {
        return "setwarp";
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
                    if (warpManager.getWarpByName(strings[0]) != null) {
                        sendMessage(commandSender, "Warp.Exists");
                        return false;
                    }
                    warpManager.addWarp(strings[0], p.getLocation(), p.getName());
                    sendMessage(commandSender, "Warp.Added", strings[0]);
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
