package org.lins.mmmjjkx.mixtools.commands;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.misc.CommandGroupManager;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsCommandGroup;
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
            argsList.add("list");
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
            if (args.length==0) {
                sendMessage(sender, "Command.ArgError");
                return false;
            } else if (args.length==1 & args[0].equals("list")) {
                sendMessages(sender,1);
                return true;
            } else if (args.length>2&args[0].equals("add")) {
                List<String> cmds = new ArrayList<>();
                for (int i=2;i<args.length;i++) {
                    if (args[i].isBlank()) continue;
                    String cmd = args[i].replaceAll("<sp>"," ");
                    cmds.add(cmd);
                }
                if (commandGroupManager.containsGroup(args[1])) {
                    commandGroupManager.addCommands(args[1], cmds);
                    sendMessage(sender, "CommandGroup.AddCommandsSuccess", args[1]);
                }else {
                    commandGroupManager.addGroup(new MixToolsCommandGroup(args[1],cmds));
                    sendMessage(sender, "CommandGroup.Created", args[1]);
                }
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
                    case "list" -> {
                        sendMessages(sender,toInteger(sender,args[1],2));
                        return true;
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

    private void sendMessages(CommandSender sender,int page){
        List<MixToolsCommandGroup> operators = new ArrayList<>(commandGroupManager.getAllGroups());
        List<List<MixToolsCommandGroup>> partition = Lists.partition(operators, 10);
        if (page==-100) {
            return;
        }else if (operators.isEmpty() & page==1) {
            sendMessage(sender,"Info.List.CommandGroupListEmpty");
            return;
        } else if (page>partition.size()) {
            sendMessage(sender,"Value.TooHigh",1);
            return;
        }
        int real_page = page-1;
        List<MixToolsCommandGroup> groups_parted = partition.get(real_page);
        sendMessage(sender, "Info.List.Head",page);
        int head = page==1 ? 1 : (10*real_page)+1;
        for (MixToolsCommandGroup group : groups_parted) {
            sendMessage(sender, "Info.List.Default", head, group.name());
            head++;
        }
        sendMessage(sender, "Info.List.Tail");
    }
}
