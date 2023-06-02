package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMDOPList extends ListCMD<OfflinePlayer> {
    public CMDOPList(@NotNull String name) {
        super(name);
    }

    @Override
    public List<OfflinePlayer> list(CommandSender sender) {
        return Bukkit.getOperators().stream().toList();
    }

    @Override
    public void sendLineMessage(CommandSender sender, int number, OfflinePlayer object) {
        sendMessage(sender, "Info.List.Styles.Default", number, object.getName());
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}