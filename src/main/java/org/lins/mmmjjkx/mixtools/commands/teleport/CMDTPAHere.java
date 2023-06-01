package org.lins.mmmjjkx.mixtools.commands.teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.setters.TpaSetter;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsTeleportRequest;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.List;

public class CMDTPAHere implements MixTabExecutor {
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
                        TpaSetter tpaSetter = MixTools.miscFeatureManager.getTpaManager();
                        MixToolsTeleportRequest request = new MixToolsTeleportRequest(p2,p);
                        tpaSetter.setExpireTime(request, tpaSetter.DEFAULT_EXPIRE_TIME);
                        sendMessage(p,"TPA.Sent");
                        tpaSetter.buildRequestMessage(request,true);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                int timeLeft = tpaSetter.getExpireTime(request);
                                tpaSetter.setExpireTime(request, --timeLeft);
                                if (timeLeft == 1){
                                    sendMessage(sender,"TPA.Timeout",p2.getName());
                                }
                                if (timeLeft == 0) {
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
