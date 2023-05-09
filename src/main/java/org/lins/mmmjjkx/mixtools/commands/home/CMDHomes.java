package org.lins.mmmjjkx.mixtools.commands.home;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDHomes implements MixTabExecutor {
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
                return StringUtil.copyPartialMatches(args[0], argList, new ArrayList<>());
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null) {
                if (args.length == 0) {
                    sendMessages(p, 1);
                    return true;
                } else if (args.length == 1) {
                    sendMessages(p, toInteger(sender, args[0], 1));
                    return true;
                } else {
                    sendMessage(sender, "Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }

    private void sendMessages(Player p,int page){
        List<String> homeNames = new ArrayList<>(MixTools.getDataManager().getPlayerOwnedHomesName(p));
        List<List<String>> partition = Lists.partition(homeNames, 10);
        if (page == -100) {
            return;
        } else if (homeNames.isEmpty() & page == 1) {
            sendMessage(p, "Info.List.OPListEmpty");
            return;
        } else if (page > partition.size()) {
            sendMessage(p, "Value.TooHigh", 1);
            return;
        }
        int real_page = page - 1;
        List<String> homeNames_parted = partition.get(real_page);
        sendMessage(p, "Info.List.Head", page);
        int head = page == 1 ? 1 : (10 * real_page) + 1;
        for (String homeName : homeNames_parted) {
            sendMessage(p, "Info.List.Default", head, homeName);
            head++;
        }
        sendMessage(p, "Info.List.Tail");
    }
}
