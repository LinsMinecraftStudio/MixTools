package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.misc.TpaManager;
import org.lins.mmmjjkx.mixtools.objects.MixToolsTeleportRequest;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;

public class CMDTPAAccept implements MixCommandExecutor {
    @Override
    public String name() {
        return "tpaaccept";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                TpaManager tpaManager = MixTools.miscFeatureManager.getTpaManager();
                MixToolsTeleportRequest request = tpaManager.getRequest(p);
                if (request == null){
                    sendMessage(sender,"TPA.NoRequest");
                    return false;
                }
                Player from = request.from();
                if (from == null){
                    sendMessage(sender,"TPA.PlayerLeft");
                    return false;
                }
                from.teleport(p);
                tpaManager.setExpireTime(request,0);
                sendMessage(sender,"TPA.Teleported",from.getName());
                return true;
            }
        }
        return false;
    }
}
