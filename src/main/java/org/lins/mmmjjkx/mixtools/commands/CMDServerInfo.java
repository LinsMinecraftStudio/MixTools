package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;

import java.util.List;

public class CMDServerInfo implements MixCommandExecutor {
    @Override
    public String name() {
        return "serverinfo";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)) {
            String server_name = Bukkit.getName();
            int port = Bukkit.getPort();
            int online_player = Bukkit.getOnlinePlayers().size();
            int max_player = Bukkit.getMaxPlayers();
            String str = online_player + "/" + max_player;
            String version = Bukkit.getBukkitVersion().split("-")[0];
            int usedMem = (int) ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1048576);
            int maxMem = (int) (Runtime.getRuntime().maxMemory() / 1048576);
            String mem_str = usedMem + "MB/" + maxMem + "MB";
            List<String> messages = MixTools.messageHandler.getColoredMessagesParseVarPerLine("Info.Server",
                    server_name,port,str,version,mem_str);
            MixTools.messageHandler.sendMessages(sender,messages);
            return true;
        }
        return false;
    }
}
