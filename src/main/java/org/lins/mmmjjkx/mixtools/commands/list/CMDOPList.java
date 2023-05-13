package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CMDOPList implements ListCMD<OfflinePlayer> {
    @Override
    public List<OfflinePlayer> list(CommandSender sender) {
        return Bukkit.getOperators().stream().toList();
    }

    @Override
    public void sendLineMessage(CommandSender sender, OfflinePlayer object, int number) {
        sendMessage(sender, "Info.List.Styles.Default", number, object.getName());
    }

    @Override
    public String name() {
        return "oplist";
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}