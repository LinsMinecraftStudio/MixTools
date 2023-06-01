package org.lins.mmmjjkx.mixtools.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.setters.TpaSetter;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsTeleportRequest;

public class CMDTPAAll implements MixCommandExecutor {
    @Override
    public String name() {
        return "tpaall";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
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
                        }.runTaskTimer(MixTools.INSTANCE, 20, 20);
                        return true;
                    }
                }else {
                    sendMessage(sender,"Command.ArgError");
                    return false;
                }
            }
        }
        return false;
    }
}
