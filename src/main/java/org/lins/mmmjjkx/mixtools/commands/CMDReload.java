package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;

public class CMDReload implements MixCommandExecutor {
    @Override
    public String name() {
        return "mixtoolsreload";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)) {
            MixTools.INSTANCE.reloadConfig();
            MixTools.INSTANCE.Reload();
            sendMessage(sender,"Command.ReloadSuccess");
            return true;
        }
        return false;
    }
}
