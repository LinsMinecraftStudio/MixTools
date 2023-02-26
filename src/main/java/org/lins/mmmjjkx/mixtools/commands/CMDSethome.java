package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;
import org.lins.mmmjjkx.mixtools.objects.home.MixToolsHome;

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
                     int amount = MixTools.dataManager.getPlayerOwnedHomesAmount(p.getName());
                     if (amount==0){
                         MixTools.dataManager.addHome(new MixToolsHome("Home",p,p.getLocation()));
                         sendMessage(p, "Home.CreateSuccess", "Home");
                     }
                 }else if (args.length==1){

                 }else {
                     sendMessage(p, "Command.NoEnoughOrTooManyArgs");
                 }
             }
        }
        return false;
    }
}
