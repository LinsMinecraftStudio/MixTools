package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        if (args.length==0){
            argsList.add("add");
            argsList.add("run");
            argsList.add("remove");
        } else if (args.length==1) {
            argsList.addAll(commandGroupManager.getAllGroupsName());
        }
        if (args.length==2&&args[1].equals("run")) {
            return getPlayerNames();
        }
        return argsList;
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
    public String usage() {
        return "/<command> <add/run/remove> <groupname/player>";
    }

    @Override
    public List<String> aliases() {
        return List.of("cg");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==0||args.length==1) {
                sendMessage(sender, "Command.NoEnoughOrTooManyArgs");
                return false;
            } else if (args.length>2&&args[0].equals("add")) {
                List<String> cmds = new ArrayList<>();
                for (int i=2;i<args.length-2;i++) {
                    String cmd = args[i].replaceAll(":sp:"," ");
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
                        commandGroupManager.addGroup(new MixToolsCommandGroup(args[1],null));
                        sendMessage(sender,"CommandGroup.Removed",args[1]);
                        return true;
                    }
                }
                return false;
            } else if (args.length==3&&args[0].equals("run")){
                Player p = findPlayer(sender, args[1]);
                String groupName = args[2];
                if (p!=null) {
                    commandGroupManager.runCommandGroup(sender, p, groupName);
                    return true;
                }
            }
        }
        return false;
    }
}
