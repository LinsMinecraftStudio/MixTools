package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMDOPList implements ListCMD<OfflinePlayer> {
    @Override
    public List<OfflinePlayer> list() {
        return Bukkit.getOperators().stream().toList();
    }

    @Override
    public String getObjectName(OfflinePlayer object) {
        return object.getName();
    }

    @Override
    public @NotNull Object[] args(OfflinePlayer object) {
        return new Object[0];
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