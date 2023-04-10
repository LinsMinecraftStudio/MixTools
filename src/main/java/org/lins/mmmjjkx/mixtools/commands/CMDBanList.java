package org.lins.mmmjjkx.mixtools.commands;

import com.google.common.collect.Lists;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
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

public class CMDBanList implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1) {
            List<String> argList = new ArrayList<>();
            List<OfflinePlayer> operators = new ArrayList<>(Bukkit.getBannedPlayers());
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
        return "banlist";
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
                sendMessages(sender, toInteger(sender, args[0]));
                return true;
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }

    private void sendMessages(CommandSender sender,int page){
        List<BanEntry> bannedPlayers = new ArrayList<>(Bukkit.getBanList(BanList.Type.NAME).getBanEntries());
        List<List<BanEntry>> partition = Lists.partition(bannedPlayers, 10);
        if (page==-100){
            return;
        } else if (bannedPlayers.isEmpty() & page==1) {
            sendMessage(sender,"Info.List.BannedListEmpty");
            return;
        } else if (page>partition.size()) {
            sendMessage(sender,"Value.TooHigh",1);
            return;
        }
        int real_page = page-1;
        List<BanEntry> banned_players_parted = partition.get(real_page);
        sendMessage(sender, "Info.List.Head",page);
        int head = page==1 ? 1 : (10*real_page)+1;
        for (BanEntry entry : banned_players_parted) {
            sendMessage(sender, "Info.List.Banned", head, entry.getTarget(), entry.getReason());
            head++;
        }
        sendMessage(sender, "Info.List.Tail");
    }
}
