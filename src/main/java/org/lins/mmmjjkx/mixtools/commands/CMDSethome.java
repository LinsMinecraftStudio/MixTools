package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;
import org.lins.mmmjjkx.mixtools.objects.home.MixToolsHome;
import org.lins.mmmjjkx.mixtools.utils.MixStringUtils;

import java.util.List;

public class CMDSethome implements MixCommandExecutor {
    @Override
    public String name() {
        return "sethome";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public String usage() {
        return "/<command> [name]";
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = toPlayer(sender);
        if (p != null) {
             if (MixTools.dataManager.canCreateHomes(p)) {
                 if (args.length==0){
                     int amount = MixTools.dataManager.getPlayerOwnedHomesAmount(p);
                     MixTools.dataManager.addHome(new MixToolsHome("Home"+amount,p,p.getLocation()));
                     sendMessage(p, "Home.CreateSuccess", "Home"+amount);
                     return true;
                 }else if (args.length==1){
                     String name = args[0];
                     if (MixStringUtils.matchRegex(name)) {
                         MixTools.dataManager.addHome(new MixToolsHome(args[0], p, p.getLocation()));
                         sendMessage(p, "Home.CreateSuccess", args[0]);
                         return true;
                     }else {
                         sendMessage(p, "String.NoMatchRegex");
                         return false;
                     }
                 }else {
                     sendMessage(p, "Command.NoEnoughOrTooManyArgs");
                     return false;
                 }
             }
        }
        return false;
    }
}
