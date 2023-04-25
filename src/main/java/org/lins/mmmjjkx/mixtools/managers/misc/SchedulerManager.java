package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulerManager {
    private final YamlConfiguration config = new YamlConfiguration();
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
            long delay = (section.getInt("delay")*20L);
            List<String> actions = section.getStringList("actions");
            if (actions.isEmpty()) continue;
            if (delay >= 0L) continue;
            schedulers.add(new MixToolsScheduler(key, delay, actions));
        }
    }

    public void startAllRunnable(){
        for(MixToolsScheduler scheduler: schedulers){
            BukkitTask task = new BukkitRunnable(){
                @Override
                public void run() {
                    for (String action : scheduler.actions()){
                        String[] split = action.split(":");
                        if (split.length<2){
                            continue;
                        }
                        runAction(action);
                    }
                }
            }.runTaskTimerAsynchronously(MixTools.INSTANCE,scheduler.delay(),scheduler.delay());
            tasks.put(task,scheduler.name());
        }
    }

    public void startRunnable(String name){
        if (containsScheduler(name)) {
            MixToolsScheduler scheduler = getScheduler(name);
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
        for (MixToolsScheduler scheduler: schedulers){
            if (scheduler.name().equals(name)){
                return true;
            }
        }
        return false;
    }

    public MixToolsScheduler getScheduler(String name){
        for (MixToolsScheduler scheduler: schedulers){
            if (scheduler.name().equals(name)){
                return scheduler;
            }
        }
        return null;
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
        for (BukkitTask task : tasks.keySet()){
            task.cancel();
            tasks.remove(task);
        }
    }

    private void runAction(String action){
        String[] split = action.split(":");
        switch (split[0]) {
            case "cmd" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), split[1]);
            case "cmdgroup" -> MixTools.miscFeatureManager.getCommandGroupManager().
                    runCommandGroupAsConsole(split[1]);
            case "broadcast" -> MixTools.messageHandler.broadcastMessage(split[1]);
        }
    }
}
