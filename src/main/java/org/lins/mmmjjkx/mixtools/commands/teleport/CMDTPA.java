package org.lins.mmmjjkx.mixtools.commands.teleport;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.setters.TpaSetter;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsTeleportRequest;

import java.util.ArrayList;
import java.util.List;

public class CMDTPA extends PolymerCommand {
    public CMDTPA(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],getPlayerNames());
        }
        return new ArrayList<>();
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
        if (hasPermission(sender)) {
            Player p = toPlayer(sender);
            if (p != null) {
                if (args.length == 1) {
                    Player p2 = findPlayer(p, args[0]);
                    if (p2 != null) {
                        TpaSetter tpaSetter = MixTools.miscFeatureManager.getTpaManager();
                        MixToolsTeleportRequest request = new MixToolsTeleportRequest(p,p2);
                        tpaSetter.setExpireTime(request, tpaSetter.DEFAULT_EXPIRE_TIME);
                        sendMessage(p,"TPA.Sent");
                        tpaSetter.buildRequestMessage(request,false);
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
