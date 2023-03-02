package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.List;

public class CMDCommandGroup implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==0){
            return getPlayerNames();
        } else if (args.length==1) {
            return MixTools.commandGroupManager.getAllGroupsName().stream().toList();
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
    public String usage() {
        return "/<command> <player> <groupname>";
    }

    @Override
    public List<String> aliases() {
        return List.of("cg");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==2) {
                Player p = findPlayer(sender,args[0]);
                MixTools.commandGroupManager.runCommandGroup(sender, p, args[1]);
                sendMessage(sender,"Command.CommandGroupExecuted");
                return true;
            }else {
                sendMessage(sender,"Command.NoEnoughOrTooManyArgs");
                return false;
            }
        }
        return false;
    }
}
