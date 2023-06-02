package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CMDBanList extends ListCMD<OfflinePlayer> {
    public CMDBanList(@NotNull String name) {
        super(name);
    }

    @Override
    public List<OfflinePlayer> list(CommandSender sender) {
        return Bukkit.getBannedPlayers().stream().toList();
    }

    @Override
    public void sendLineMessage(CommandSender sender, int number, OfflinePlayer object) {
        String reason = Objects.requireNonNull(Bukkit.getBanList(BanList.Type.NAME).getBanEntry(Objects.requireNonNull(object.getName()))).getReason();
        sendMessage(sender, "Info.List.Styles.Banned", number, object.getName(), reason);
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}
