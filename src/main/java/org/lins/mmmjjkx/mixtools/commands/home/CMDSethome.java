package org.lins.mmmjjkx.mixtools.commands.home;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsHome;
import org.lins.mmmjjkx.mixtools.utils.MTUtils;

public class CMDSethome extends PolymerCommand {
    public CMDSethome(@NotNull String name) {
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        Player p = toPlayer(sender);
        if (hasPermission(sender)) {
            if (p != null) {
                if (MixTools.getDataManager().canCreateHomes(p)) {
                    if (args.length == 1) {
                        String name = args[0];
                        if (MTUtils.matchStringRegex(name)) {
                            MixTools.getDataManager().addHome(new MixToolsHome(args[0], p, p.getLocation()));
                            sendMessage(p, "Home.CreateSuccess", args[0]);
                            return true;
                        } else {
                            sendMessage(p, "Value.NoMatchRegex", 1);
                            return false;
                        }
                    } else {
                        sendMessage(p, "Command.ArgError");
                        return false;
                    }
                } else {
                    sendMessage(p, "Home.Full");
                    return false;
                }
            }
        }
        return false;
    }
}
