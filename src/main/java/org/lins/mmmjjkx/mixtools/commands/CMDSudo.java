package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDSudo implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> s = new ArrayList<>();
        if (args.length==0){
            s = getPlayerNames();
        } else if (args.length > 1) {
            s.add("cmd:");
            s.add("chat:");
            s.add("cmdgroup:");
        }
        return s;
    }

    @Override
    public String name() {
        return "sudo";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (hasPermission(sender)){
            if (args.length>1){
                Player p = findPlayer(sender,args[0]);
                if (p != null){
                    for (int i=1;i<args.length-1;i++){
                        String[] str = args[i].split(":");
                        if (str.length!=2){
                            continue;
                        }
                        String key = str[0];
                        String action = str[1].replaceAll("<sp>"," ");
                        switch (key) {
                            case "chat" -> p.chat(action);
                            case "cmd" -> p.performCommand(action);
                            case "cmdgroup" -> MixTools.miscFeatureManager.getCommandGroupManager().runCommandGroup(sender, p, action);
                        }
                    }
                    return true;
                }
            }else {
                sendMessage(sender,"Command.NoEnoughOrTooManyArgs");
                return false;
            }
        }
        return false;
    }
}
