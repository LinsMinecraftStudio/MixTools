package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.misc.TpaManager;
import org.lins.mmmjjkx.mixtools.objects.MixToolsTeleportRequest;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.List;

public class CMDTPAHere implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==0){
            return getPlayerNames();
        }
        return null;
    }

    @Override
    public String name() {
        return "tpahere";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)) {
            Player p = toPlayer(sender);
            if (p != null) {
                if (args.length == 1) {
                    Player p2 = findPlayer(p, args[0]);
                    if (p2 != null) {
                        TpaManager tpaManager = MixTools.miscFeatureManager.getTpaManager();
                        MixToolsTeleportRequest request = new MixToolsTeleportRequest(p2,p);
                        tpaManager.setCooldown(request,tpaManager.DEFAULT_COOLDOWN);
                        sendMessage(p,"TPA.Sent");
                        tpaManager.buildRequest(request,true);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                int timeLeft = tpaManager.getCooldown(request);
                                tpaManager.setCooldown(request, --timeLeft);
                                if(timeLeft == 0){
                                    sendMessage(sender,"TPA.Timeout",p2.getName());
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
