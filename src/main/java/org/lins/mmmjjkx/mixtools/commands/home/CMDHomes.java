package org.lins.mmmjjkx.mixtools.commands.home;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.commands.list.ListCMD;

import java.util.ArrayList;
import java.util.List;

public class CMDHomes implements ListCMD<String> {
    @Override
    public List<String> list(CommandSender sender) {
        return sender instanceof Player ? new ArrayList<>(MixTools.getDataManager().getPlayerOwnedHomesName((Player) sender)) : new ArrayList<>();
    }

    @Override
    public String getObjectName(String object) {
        return object;
    }

    @Override
    public @NotNull Object[] args(String object) {
        return new Object[0];
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1) {
            if (sender instanceof Player p) {
                List<String> argList = new ArrayList<>();
                List<String> homeNames = new ArrayList<>(MixTools.getDataManager().
                        getPlayerOwnedHomesName(p));
                List<List<String>> partition = Lists.partition(homeNames, 10);
                for (int i = 0; i < partition.size(); i++) {
                    int ri = i + 1;
                    argList.add(String.valueOf(ri));
                }
                return copyPartialMatches(args[0], argList);
            }
        }
        return null;
    }

    @Override
    public String name() {
        return "homes";
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}
