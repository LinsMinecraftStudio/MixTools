package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.SchedulerManager;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsScheduler;

import java.util.ArrayList;
import java.util.List;

public class CMDScheduler implements MixTabExecutor {
    private final SchedulerManager schedulerManager = MixTools.schedulerManager;

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
                    return StringUtil.copyPartialMatches(args[1], schedulerNames, new ArrayList<>());
                }
            }
        }
        return null;
    }

    @Override
    public String name() {
        return "scheduler";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
                        sendMessage(sender, "Command.ArgError");
                        return false;
                    }
                }
            } else if (args.length==2) {
                switch (args[0]) {
                    case "start" -> {
                        String schedulerName = args[1];
                        if (!schedulerManager.containsScheduler(schedulerName)) {
                            sendMessage(sender, "Scheduler.schedulerNotFound");
                            return false;
                        }
                        schedulerManager.startRunnable(schedulerName);
                        sendMessage(sender, "Scheduler.start",schedulerName);
                        return true;
                    }
                    case "stop" -> {
                        String schedulerName = args[1];
                        if (!schedulerManager.containsScheduler(schedulerName)) {
                            sendMessage(sender, "Scheduler.schedulerNotFound");
                            return false;
                        }
                        schedulerManager.stopRunnable(schedulerName);
                        sendMessage(sender, "Scheduler.stop",schedulerName);
                        return true;
                    }
                    default -> {
                        sendMessage(sender, "Command.ArgError");
                        return false;
                    }
                }
            } else {
                sendMessage(sender, "Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
