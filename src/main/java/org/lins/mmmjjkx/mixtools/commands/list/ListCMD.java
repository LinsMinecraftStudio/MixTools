package org.lins.mmmjjkx.mixtools.commands.list;

import com.google.common.collect.Lists;
import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public abstract class ListCMD<T> extends PolymerCommand {
    public ListCMD(@NotNull String name) {
        super(name);
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    public abstract List<T> list(CommandSender sender);
    public abstract void sendLineMessage(CommandSender sender, int number, T object);

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args){
        if (args.length==1) {
            List<String> argList = new ArrayList<>();
            List<List<T>> partition = Lists.partition(list(commandSender), 10);
            for (int i = 0; i < partition.size(); i++) {
                int ri = i+1;
                argList.add(String.valueOf(ri));
            }
            return copyPartialMatches(args[0],argList);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args){
        if (hasPermission(sender)){
            if (args.length==0){
                sendMessages(sender,1);
                return true;
            }else if (args.length==1){
                sendMessages(sender, toInteger(sender, args[0], 1));
                return true;
            }else {
                Polymer.messageHandler.sendMessage(sender,"Command.ArgError");
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
            Polymer.messageHandler.sendMessage(sender,"Value.TooHigh",1);
            return;
        }
        int real_page = page-1;
        List<T> parted = partition.get(real_page);
        sendMessage(sender, "Info.List.Head",page);
        int head = page==1 ? 1 : (10*real_page)+1;
        for (T obj : parted) {
            sendLineMessage(sender, head, obj);
            head++;
        }
        sendMessage(sender, "Info.List.Tail");
    }
}
