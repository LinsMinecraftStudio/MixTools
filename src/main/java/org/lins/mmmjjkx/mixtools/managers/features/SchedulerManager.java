package org.lins.mmmjjkx.mixtools.managers.features;

import io.github.linsminecraftstudio.polymer.objects.plugin.AbstractFeatureManager;
import io.github.linsminecraftstudio.polymer.utils.ListUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsScheduler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulerManager extends AbstractFeatureManager {
    private YamlConfiguration config;
    private final List<MixToolsScheduler> schedulers = new ArrayList<>();
    private final Map<BukkitTask,String> tasks = new HashMap<>();

    public SchedulerManager() {
        super(MixTools.getInstance());
        config = handleConfig("scheduler.yml");
        loadSchedulers();
        startAllRunnable();
    }

    public void reload() {
        stopAllRunnable();
        config = handleConfig("scheduler.yml");
        startAllRunnable();
    }

    public void loadSchedulers(){
        for (String key : config.getKeys(false)){
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null) continue;
            long delay = section.getLong("delay");
            List<String> actions = section.getStringList("actions");
            if (actions.isEmpty()) {
                MixTools.warn("Scheduler " + key + " has no actions. It will not be started.");
                continue;
            }
            if (delay < 1L) {
                MixTools.warn("Scheduler " + key + " delay is less than 1 second. It will not be started.");
                continue;
            }
            long delay2 = delay*20L;
            boolean manuallyStart = section.getBoolean("manuallyStart",false);
            schedulers.add(new MixToolsScheduler(key, delay2, actions, manuallyStart));
        }
    }

    public void startAllRunnable(){
        for(MixToolsScheduler scheduler: schedulers){
            startTask(scheduler);
        }
    }

    public void startRunnable(String name){
        MixToolsScheduler scheduler = getScheduler(name);
        if (scheduler != null) {
            startTask(scheduler);
        }
    }

    private void startTask(MixToolsScheduler scheduler) {
        if (!scheduler.manuallyStart()) {
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    for (String action : scheduler.actions()) {
                        String[] split = action.split(":");
                        if (split.length < 2) {
                            continue;
                        }
                        runAction(action);
                    }
                }
            }.runTaskTimerAsynchronously(MixTools.getInstance(), scheduler.delay(), scheduler.delay());
            tasks.put(task, scheduler.name());
        }
    }

    public List<MixToolsScheduler> getSchedulers() {
        return schedulers;
    }

    public boolean containsScheduler(String name){
        return ListUtil.listGetIf(schedulers, scheduler -> scheduler.name().equals(name)).isPresent();
    }

    @Nullable
    public MixToolsScheduler getScheduler(String name){
        return ListUtil.listGetIf(schedulers, sc -> sc.name().equals(name)).orElse(null);
    }

    public void stopRunnable(String name){
        for (BukkitTask task : tasks.keySet()) {
            if (tasks.get(task).equals(name)){
                task.cancel();
                tasks.remove(task);
            }
        }
    }

    public void stopAllRunnable() {
        for (BukkitTask task : tasks.keySet()) {
            task.cancel();
        }
        tasks.clear();
    }

    private void runAction(String action){
        String[] split = action.split("::");
        switch (split[0]) {
            case "cmd" -> new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), split[1]);
                }
            }.runTask(MixTools.getInstance());
            case "cmdgroup" -> new BukkitRunnable() {
                @Override
                public void run() {
                    MixTools.miscFeatureManager.getCommandGroupManager().runCommandGroupAsConsole(split[1]);
                }
            }.runTask(MixTools.getInstance());
            case "wait" -> {
                try {Thread.sleep(Integer.parseInt(split[1])*1000L);
                } catch (InterruptedException e) {throw new RuntimeException(e);}
            }
            case "broadcast" -> MixTools.messageHandler.broadcastCustomMessage(split[1]);
        }
    }
}
