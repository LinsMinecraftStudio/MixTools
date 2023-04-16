package org.lins.mmmjjkx.mixtools.commands;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDOPList implements MixTabExecutor {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1) {
            List<String> argList = new ArrayList<>();
            List<OfflinePlayer> operators = new ArrayList<>(Bukkit.getOperators());
            List<List<OfflinePlayer>> partition = Lists.partition(operators, 10);
            for (int i = 0; i < partition.size(); i++) {
                int ri = i+1;
                argList.add(String.valueOf(ri));
            }
            return StringUtil.copyPartialMatches(args[0],argList,new ArrayList<>());
        }
        return null;
    }

    @Override
    public String name() {
        return "oplist";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==0){
                sendMessages(sender,1);
                return true;
            }else if (args.length==1){
                sendMessages(sender, toInteger(sender, args[0], 1));
                return true;
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }

    private void sendMessages(CommandSender sender,int page){
        List<OfflinePlayer> operators = new ArrayList<>(Bukkit.getOperators());
        List<List<OfflinePlayer>> partition = Lists.partition(operators, 10);
        if (page==-100) {
            return;
        }else if (operators.isEmpty() & page==1) {
            sendMessage(sender,"Info.List.OPListEmpty");
            return;
        } else if (page>partition.size()) {
            sendMessage(sender,"Value.TooHigh",1);
            return;
        }
        int real_page = page-1;
        List<OfflinePlayer> operators_parted = partition.get(real_page);
        sendMessage(sender, "Info.List.Head",page);
        int head = page==1 ? 1 : (10*real_page)+1;
        for (OfflinePlayer offlinePlayer : operators_parted) {
            sendMessage(sender, "Info.List.Default", head, offlinePlayer.getName());
            head++;
        }
        sendMessage(sender, "Info.List.Tail");
    }
}