package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;
import org.lins.mmmjjkx.mixtools.objects.MixToolsHome;
import org.lins.mmmjjkx.mixtools.utils.MixStringUtil;

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p = toPlayer(sender);
        if (p != null) {
             if (MixTools.getDataManager().canCreateHomes(p)) {
                 if (args.length==0){
                     int amount = MixTools.getDataManager().getPlayerOwnedHomesAmount(p);
                     MixTools.getDataManager().addHome(new MixToolsHome("Home"+amount,p,p.getLocation()));
                     sendMessage(p, "Home.CreateSuccess", "Home"+amount);
                     return true;
                 }else if (args.length==1){
                     String name = args[0];
                     if (MixStringUtil.matchStringRegex(name)) {
                         MixTools.getDataManager().addHome(new MixToolsHome(args[0], p, p.getLocation()));
                         sendMessage(p, "Home.CreateSuccess", args[0]);
                         return true;
                     }else {
                         sendMessage(p, "String.NoMatchRegex",1);
                         return false;
                     }
                 }else {
                     sendMessage(p, "Command.ArgError");
                     return false;
                 }
             }
        }
        return false;
    }
}
