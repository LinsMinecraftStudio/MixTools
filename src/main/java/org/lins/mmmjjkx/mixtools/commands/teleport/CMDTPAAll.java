package org.lins.mmmjjkx.mixtools.commands.teleport;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.setters.TpaSetter;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsTeleportRequest;

public class CMDTPAAll extends PolymerCommand {
    public CMDTPAAll(@NotNull String name) {
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (hasPermission(sender)) {
            Player p = toPlayer(sender);
            if (p != null) {
                if (args.length == 1) {
                    TpaSetter tpaSetter = MixTools.miscFeatureManager.getTpaManager();
                    for (Player p2 : Bukkit.getOnlinePlayers()){
                        if (p.getName().equals(p2.getName())) continue;
                        MixToolsTeleportRequest request = new MixToolsTeleportRequest(p2,p);
                        tpaSetter.setExpireTime(request, tpaSetter.DEFAULT_EXPIRE_TIME);
                        sendMessage(p,"TPA.Sent");
                        tpaSetter.buildRequestMessage(request,true);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                int timeLeft = tpaSetter.getExpireTime(request);
                                tpaSetter.setExpireTime(request, --timeLeft);
                                if  (timeLeft == 1){
                                    sendMessage(sender,"TPA.Timeout",p2.getName());
                                }
                                if (timeLeft == 0){
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(MixTools.getInstance(), 20, 20);
                        return true;
                    }
                }else {
                    Polymer.messageHandler.sendMessage(sender,"Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }
}
