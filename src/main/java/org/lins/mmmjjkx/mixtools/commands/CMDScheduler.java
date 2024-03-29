package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.SchedulerManager;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsScheduler;

import java.util.ArrayList;
import java.util.List;

public class CMDScheduler extends PolymerCommand {
    private final SchedulerManager schedulerManager = MixTools.schedulerManager;

    public CMDScheduler(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            List<String> list = List.of("stopAll","stop","start","startAll");
            return StringUtil.copyPartialMatches(args[0], list, new ArrayList<>());
        }
        if (args.length==2){
            switch (args[0]) {
                case "stop", "start" -> {
                    List<String> schedulerNames = new ArrayList<>();
                    for (MixToolsScheduler scheduler : MixTools.schedulerManager.getSchedulers()) {
                        schedulerNames.add(scheduler.name());
                    }
                    return copyPartialMatches(args[1], schedulerNames);
                }
            }
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
        if (hasCustomPermission(sender,"scheduler")) {
            if (args.length==1){
                switch (args[0]) {
                    case "stopAll" -> {
                        schedulerManager.stopAllRunnable();
                        sendMessage(sender, "Scheduler.stopAll");
                        return true;
                    }
                    case "startAll" -> {
                        schedulerManager.startAllRunnable();
                        sendMessage(sender, "Scheduler.startAll");
                        return true;
                    }
                    default -> {
                        Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
                        return false;
                    }
                }
            } else if (args.length==2) {
                switch (args[0]) {
                    case "start" -> {
                        String schedulerName = args[1];
                        if (!schedulerManager.containsScheduler(schedulerName)) {
                            sendMessage(sender, "Scheduler.notFound");
                            return false;
                        }
                        schedulerManager.startRunnable(schedulerName);
                        sendMessage(sender, "Scheduler.start",schedulerName);
                        return true;
                    }
                    case "stop" -> {
                        String schedulerName = args[1];
                        if (!schedulerManager.containsScheduler(schedulerName)) {
                            sendMessage(sender, "Scheduler.nFound");
                            return false;
                        }
                        schedulerManager.stopRunnable(schedulerName);
                        sendMessage(sender, "Scheduler.stop",schedulerName);
                        return true;
                    }
                    default -> {
                        Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
                        return false;
                    }
                }
            } else {
                Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
