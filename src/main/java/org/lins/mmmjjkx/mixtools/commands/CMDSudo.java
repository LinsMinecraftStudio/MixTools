package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDSudo implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> s = new ArrayList<>();
        if (args.length==0){
            s = getPlayerNames();
        } else if (args.length > 1) {
            s.add("cmd:");
            s.add("chat:");
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
    public String usage() {
        return "/<command> <player> <action>";
    }

    @Override
    public List<String> aliases() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (hasPermission(sender)){
            if (args.length>1){
                Player p = findPlayer(sender,args[0]);
                if (p != null){
                    for (int i=1;i<args.length-1;i++){
                        String[] str = args[i].split(":");
                        String key = str[0];
                        String action = str[1].replaceAll(":space:"," ");
                        switch (key){
                            case "chat":
                                p.chat(action);
                                break;
                            case "cmd":
                                p.performCommand(action);
                                break;
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