package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CMDBanList implements ListCMD<OfflinePlayer> {
    @Override
    public List<OfflinePlayer> list(CommandSender sender) {
        return Bukkit.getBannedPlayers().stream().toList();
    }

    @Override
    public String getObjectName(OfflinePlayer object) {
        return object.getName();
    }

    @NotNull
    @Override
    public Object[] args(OfflinePlayer object) {
        BanEntry entry = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(Objects.requireNonNull(object.getName()));
        return new Object[]{Objects.requireNonNull(Objects.requireNonNull(entry).getReason())};
    }

    @Override
    public String name() {
        return "banlist";
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}
