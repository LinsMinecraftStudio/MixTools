package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

public class CMDBack extends PolymerCommand {
    public CMDBack(String name){
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                Location loc = MixTools.miscFeatureManager.getBackPlayerLocation(p);
                if (loc != null) {
                    p.teleport(MixTools.miscFeatureManager.getBackPlayerLocation(p));
                    sendMessage(p, "Command.Back");
                    return true;
                } else {
                    sendMessage(p, "Command.NoBackLocation");
                    return false;
                }
            }
        }
        return false;
    }
}
