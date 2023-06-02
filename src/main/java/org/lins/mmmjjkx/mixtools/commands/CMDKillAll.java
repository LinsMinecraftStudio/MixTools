package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

public class CMDKillAll extends PolymerCommand {
    public CMDKillAll(@NotNull String name) {
        super(name);
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {}

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (hasPermission(sender)){
            for (Player p: Bukkit.getOnlinePlayers()){
                p.setHealth(0);
            }
            MixTools.messageHandler.broadcastMessage("Command.KillAll",sender.getName());
            return true;
        }
        return false;
    }
}
