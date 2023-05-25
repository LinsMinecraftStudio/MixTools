package org.lins.mmmjjkx.mixtools.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;

public class CMDNick implements MixCommandExecutor {
    @Override
    public String name() {
        return "nick";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (hasPermission(commandSender)) {
            Player player = toPlayer(commandSender);
            if (player != null) {
                if (strings.length == 1) {
                    Component component = MixTools.messageHandler.colorize(strings[0]);
                    player.displayName(component);
                    return true;
                }else {
                    sendMessage(commandSender,"Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }
}
