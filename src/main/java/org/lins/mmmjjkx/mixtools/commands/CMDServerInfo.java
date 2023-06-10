package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import io.github.linsminecraftstudio.polymer.objects.ArgumentReplacement;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.List;

public class CMDServerInfo extends PolymerCommand {

    public CMDServerInfo(@NotNull String name) {
        super(name);
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {}

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)) {
            String server_name = Bukkit.getName();
            int port = Bukkit.getPort();
            int online_player = Bukkit.getOnlinePlayers().size();
            int max_player = Bukkit.getMaxPlayers();
            String str = online_player + "/" + max_player;
            String version = Bukkit.getMinecraftVersion();
            int usedMem = (int) ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1048576);
            int maxMem = (int) (Runtime.getRuntime().maxMemory() / 1048576);
            String mem_str = usedMem + "MB/" + maxMem + "MB";
            List<Component> messages = MixTools.messageHandler.getColoredMessagesParseVarPerLine("Info.Server",
                    new ArgumentReplacement(server_name),new ArgumentReplacement(port),new ArgumentReplacement(str),
                    new ArgumentReplacement(version),new ArgumentReplacement(mem_str));
            MixTools.messageHandler.sendMessages(sender,messages);
            return true;
        }
        return false;
    }
}
