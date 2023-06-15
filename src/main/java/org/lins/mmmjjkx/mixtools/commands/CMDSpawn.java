package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

public class CMDSpawn extends PolymerCommand {

    public CMDSpawn(@NotNull String name) {
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
        if(hasPermission(sender)) {
            Player p = toPlayer(sender);
            if (p != null) {
                Location spawn = MixTools.settingsManager.getLocation("spawn");
                if (spawn != null) {
                    p.teleport(spawn);
                    sendMessage(p, "Spawn.Teleported");
                    return true;
                }else {
                    sendMessage(p, "Spawn.NotFound");
                    return false;
                }
            }
        }
        return false;
    }
}
