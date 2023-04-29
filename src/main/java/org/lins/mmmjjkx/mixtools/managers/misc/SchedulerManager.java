package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsScheduler;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulerManager {
    private final YamlConfiguration config = new YamlConfiguration();
    private final File cfgFile;
    private List<MixToolsScheduler> schedulers = new ArrayList<>();
    private Map<BukkitTask,String> tasks = new HashMap<>();

    public SchedulerManager() {
        cfgFile = new File(MixTools.INSTANCE.getDataFolder(), "scheduler.yml");
        if (!cfgFile.exists()) {
            MixTools.INSTANCE.saveResource("scheduler.yml",false);
        }
        try {
            config.load(cfgFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        loadSchedulers();
        startAllRunnable();
    }

    public void reload() {
        stopAllRunnable();
        try {
            config.load(cfgFile);
            loadSchedulers();
            startAllRunnable();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadSchedulers(){
        for (String key : config.getKeys(false)){
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null) continue;
            long delay = section.getLong("delay")*20L;
            List<String> actions = section.getStringList("actions");
            boolean manuallyStart = section.getBoolean("manuallyStart");
            if (actions.isEmpty()) continue;
            if (delay < 0L) continue;
            schedulers.add(new MixToolsScheduler(key, delay, actions, manuallyStart));
        }
        List<MixToolsScheduler> distinct = schedulers.stream().distinct().toList();
        if (schedulers.size() != distinct.size()){
            schedulers = distinct;
        }
    }

    /**
     * Run all tasks(exclude manually start tasks)
     */
    public void startAllRunnable(){
        for(MixToolsScheduler scheduler: schedulers){
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
                }.runTaskTimerAsynchronously(MixTools.INSTANCE, 20L, scheduler.delay());
                tasks.put(task, scheduler.name());
            }
        }
    }

    public void startRunnable(String name){
        if (containsScheduler(name)) {
            MixToolsScheduler scheduler = getScheduler(name);
            if (scheduler != null) {
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
        }else {
            ConfigurationSection section = config.getConfigurationSection(name);
            if (section != null){
                long delay = section.getLong("delay")*20L;
                List<String> actions = section.getStringList("actions");
                boolean manuallyStart = section.getBoolean("manuallyStart");
                MixToolsScheduler scheduler = new MixToolsScheduler(name,delay,actions,manuallyStart);
                schedulers.add(scheduler);
                if (!manuallyStart) {
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
                    }.runTaskTimerAsynchronously(MixTools.INSTANCE, 20L, delay);
                    tasks.put(task, scheduler.name());
                }
            }
        }
    }

    public List<MixToolsScheduler> getSchedulers() {
        return schedulers;
    }

    public boolean containsScheduler(String name){
        for (MixToolsScheduler scheduler : schedulers) {
            if (scheduler.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public MixToolsScheduler getScheduler(String name){
        for (MixToolsScheduler scheduler: schedulers){
            if (scheduler.name().equals(name)){
                return scheduler;
            }
        }
        return null;
    }

    public void stopRunnable(String name){
        List<BukkitTask> list = new ArrayList<>(tasks.keySet());
        for (int i = 0; i < tasks.size(); i++) {
            BukkitTask task = list.get(i);
            if (tasks.get(task).equals(name)){
                Bukkit.getScheduler().cancelTask(task.getTaskId());
                tasks.remove(task);
            }
        }
    }

    public void stopAllRunnable() {
        Bukkit.getScheduler().cancelTasks(MixTools.INSTANCE);
        tasks = new HashMap<>();
    }

    private void runAction(String action){
        String[] split = action.split(":");
        switch (split[0]) {
            case "cmd" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), split[1]);
            case "cmdgroup" -> MixTools.miscFeatureManager.getCommandGroupManager().
                    runCommandGroupAsConsole(split[1]);
            case "broadcast" -> MixTools.messageHandler.broadcastCustomMessage(split[1]);
            case "wait" -> {
                long miliseconds = Integer.parseInt(split[1])*1000L;
                try {Thread.sleep(miliseconds);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
