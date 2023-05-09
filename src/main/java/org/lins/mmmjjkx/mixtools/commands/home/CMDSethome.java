package org.lins.mmmjjkx.mixtools.commands.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsHome;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;
import org.lins.mmmjjkx.mixtools.utils.StringUtils;

public class CMDSethome implements MixCommandExecutor {
    @Override
    public String name() {
        return "sethome";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p = toPlayer(sender);
        if (hasPermission(sender)) {
            if (p != null) {
                if (MixTools.getDataManager().canCreateHomes(p)) {
                    if (args.length == 1) {
                        String name = args[0];
                        if (StringUtils.matchStringRegex(name)) {
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
