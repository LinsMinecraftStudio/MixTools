package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

public class CMDNick extends PolymerCommand {
    public CMDNick(@NotNull String name) {
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
