package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.List;

public class CMDHeal implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],getPlayerNames());
        }
        return null;
    }

    @Override
    public String name() {
        return "heal";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==0){
                Player p = toPlayer(sender);
                if (p != null){
                    p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    p.setFoodLevel(20);
                    sendMessage(sender,"Command.Healed",p.getName());
                    return true;
                }
            } else if (args.length==1) {
                Player p = findPlayer(sender,args[0]);
                if (p != null){
                    p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    p.setFoodLevel(20);
                    sendMessage(sender,"Command.Healed",p.getName());
                    return true;
                }
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
