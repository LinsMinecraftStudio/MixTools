package org.lins.mmmjjkx.mixtools.commands.teleport;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.setters.TpaSetter;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsTeleportRequest;

public class CMDTPARefuse extends PolymerCommand {

    public CMDTPARefuse(@NotNull String name) {
        super(name);
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
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
