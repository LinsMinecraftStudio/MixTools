package org.lins.mmmjjkx.mixtools.commands.teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.setters.TpaSetter;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsTeleportRequest;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;

public class CMDTPARefuse implements MixCommandExecutor {
    @Override
    public String name() {
        return "tparefuse";
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
                TpaSetter tpaSetter = MixTools.miscFeatureManager.getTpaManager();
                MixToolsTeleportRequest request = tpaSetter.getRequest(p);
                if (request == null){
                    sendMessage(sender,"TPA.NoRequest");
                    return false;
                }
                tpaSetter.setExpireTime(request,0);
                Player from = request.from();
                if (from == null){return false;}
                sendMessage(from,"TPA.Denied",p.getName());
                return true;
            }
        }
        return false;
    }
}
