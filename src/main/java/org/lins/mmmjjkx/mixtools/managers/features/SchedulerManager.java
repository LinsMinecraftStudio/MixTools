package org.lins.mmmjjkx.mixtools.managers.features;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsScheduler;
import org.lins.mmmjjkx.mixtools.utils.OtherUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SchedulerManager {
    private YamlConfiguration config = new YamlConfiguration();
    private final File cfgFile;
    private final List<MixToolsScheduler> schedulers = new ArrayList<>();
    private final Map<BukkitTask,String> tasks = new HashMap<>();

    public SchedulerManager() {
        File f = new File(MixTools.INSTANCE.getDataFolder(), "scheduler.yml");
        if (!f.exists()) {
            MixTools.INSTANCE.saveResource("scheduler.yml",false);
        }
        cfgFile = f;
        try {
            config.load(f);
        }catch (Exception e){
            e.printStackTrace();
        }
        loadSchedulers();
        startAllRunnable();
    }

    public void reload() {
        stopAllRunnable();
        try {
            config = new YamlConfiguration();
            config.load(cfgFile);
            startAllRunnable();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
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
            }.runTaskTimerAsynchronously(MixTools.INSTANCE, scheduler.delay(), scheduler.delay());
            tasks.put(task, scheduler.name());
        }
    }

    public List<MixToolsScheduler> getSchedulers() {
        return schedulers;
    }

    public boolean containsScheduler(String name){
        return OtherUtil.listGetIf(schedulers, scheduler -> scheduler.name().equals(name)).isPresent();
    }

    @Nullable
    public MixToolsScheduler getScheduler(String name){
        Optional<MixToolsScheduler> scheduler = OtherUtil.listGetIf(schedulers, sc -> sc.name().equals(name));
        return scheduler.orElse(null);
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
        Bukkit.getScheduler().cancelTasks(MixTools.INSTANCE);
    }

    private void runAction(String action){
        String[] split = action.split("::");
        switch (split[0]) {
            case "cmd" -> new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), split[1]);
                }
            }.runTask(MixTools.INSTANCE);
            case "cmdgroup" -> new BukkitRunnable() {
                @Override
                public void run() {
                    MixTools.miscFeatureManager.getCommandGroupManager().runCommandGroupAsConsole(split[1]);
                }
            }.runTask(MixTools.INSTANCE);
            case "wait" -> {
                try {Thread.sleep(Integer.parseInt(split[1])*1000L);
                } catch (InterruptedException e) {throw new RuntimeException(e);}
            }
            case "broadcast" -> MixTools.messageHandler.broadcastCustomMessage(split[1]);
        }
    }
}
