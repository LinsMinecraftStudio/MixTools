package org.lins.mmmjjkx.mixtools.commands.list;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public interface ListCMD<T> extends MixTabExecutor {
    List<T> list(CommandSender sender);
    String getObjectName(T object);
    @NotNull Object[] args(T object);

    @Nullable
    @Override
    default List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args){
        if (args.length==1) {
            List<String> argList = new ArrayList<>();
            List<List<T>> partition = Lists.partition(list(commandSender), 10);
            for (int i = 0; i < partition.size(); i++) {
                int ri = i+1;
                argList.add(String.valueOf(ri));
            }
            return copyPartialMatches(args[0],argList);
        }
        return null;
    }

    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args){
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
        List<List<T>> partition = Lists.partition(list(sender), 10);
        if (page==-100){
            return;
        } else if (list(sender).isEmpty() & page==1) {
            sendMessage(sender,"Info.List.ListEmpty");
            return;
        } else if (page>partition.size()) {
            sendMessage(sender,"Value.TooHigh",1);
            return;
        }
        int real_page = page-1;
        List<T> parted = partition.get(real_page);
        sendMessage(sender, "Info.List.Head",page);
        int head = page==1 ? 1 : (10*real_page)+1;
        for (T obj : parted) {
            sendMessage(sender, "Info.List.Banned", head, getObjectName(obj), args(obj));
            head++;
        }
        sendMessage(sender, "Info.List.Tail");
    }
}
