package org.lins.mmmjjkx.mixtools.commands.warp;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;

public class CMDSetWarp extends PolymerCommand {

    public CMDSetWarp(@NotNull String name) {
        super(name);
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
