package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.List;

public class CMDReload extends PolymerCommand {
    public CMDReload(@NotNull String name) {
        super(name);
        setAliases(List.of("mtlr"));
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
        if (hasPermission(sender)) {
            try {
                MixTools.INSTANCE.Reload();
            }catch (Exception e) {
                e.printStackTrace();
            }
            sendMessage(sender,"Command.ReloadSuccess");
            return true;
        }
        return false;
    }
}
