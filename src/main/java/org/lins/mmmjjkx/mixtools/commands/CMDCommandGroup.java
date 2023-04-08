package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.misc.CommandGroupManager;
import org.lins.mmmjjkx.mixtools.objects.MixToolsCommandGroup;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDCommandGroup implements MixTabExecutor {
    private final CommandGroupManager commandGroupManager = MixTools.miscFeatureManager.getCommandGroupManager();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> argsList = new ArrayList<>();
        if (args.length==1){
            argsList.add("add");
            argsList.add("run");
            argsList.add("remove");
            return StringUtil.copyPartialMatches(args[0],argsList,new ArrayList<>());
        } else if (args.length==2&!args[1].equals("add")) {
            return StringUtil.copyPartialMatches(args[1],commandGroupManager.getAllGroupsName(),new ArrayList<>());
        } else if (args.length==3&args[1].equals("run")) {
            return StringUtil.copyPartialMatches(args[2],getPlayerNames(),new ArrayList<>());
        }
        return null;
    }

    @Override
    public String name() {
        return "commandgroup";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasCustomPermission(sender,"commandgroup")){
            if (args.length==0||args.length==1) {
                sendMessage(sender, "Command.ArgError");
                return false;
            } else if (args.length>2&args[0].equals("add")) {
                List<String> cmds = new ArrayList<>();
                for (int i=2;i<args.length-2;i++) {
                    if (args[i].isBlank()) continue;
                    String cmd = args[i].replaceAll("<sp>"," ");
                    cmds.add(cmd);
                }
                commandGroupManager.addGroup(new MixToolsCommandGroup(args[1],cmds));
                sendMessage(sender,"CommandGroup.Created",args[1]);
                return true;
            } else if (args.length==2){
                switch (args[0]){
                    case "add" -> {
                        commandGroupManager.addGroup(args[1]);
                        sendMessage(sender,"CommandGroup.Created",args[1]);
                        return true;
                    }
                    case "remove" -> {
                        if (commandGroupManager.removeGroup(args[1])) {
                            sendMessage(sender, "CommandGroup.Removed", args[1]);
                            return true;
                        }else {
                            sendMessage(sender, "CommandGroup.NotFound");
                            return false;
                        }
                    }
                    case "run" -> {
                        sendMessage(sender,"Command.ArgError");
                        return false;
                    }
                }
                return false;
            } else if (args.length==3&&args[0].equals("run")){
                Player p = findPlayer(sender, args[2]);
                String groupName = args[1];
                if (p!=null) {
                    if (!commandGroupManager.runCommandGroup(p, groupName)){
                        sendMessage(sender, "CommandGroup.NotFound");
                        return false;
                    }
                    sendMessage(sender, "CommandGroup.Executed");
                    return true;
                }
            }
        }
        return false;
    }
}
