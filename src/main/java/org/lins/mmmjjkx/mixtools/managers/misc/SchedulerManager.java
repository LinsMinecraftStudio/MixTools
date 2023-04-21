package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsScheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SchedulerManager {
    private YamlConfiguration config;
    private final File cfgFile;
    private final List<MixToolsScheduler> schedulers = new ArrayList<>();
    private final List<BukkitTask> tasks = new ArrayList<>();

    public SchedulerManager() {
        File f = new File(MixTools.INSTANCE.getDataFolder(), "scheduler.yml");
        if (!f.exists()) {
            MixTools.INSTANCE.saveResource("scheduler.yml",false);
        }
        cfgFile = f;
        try {
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.load(f);
            config = configuration;
        }catch (Exception e){
            e.printStackTrace();
        }
        loadSchedulers();
        startAllRunnable();
    }

    public boolean createScheduler(String name, int delay,List<String> actions){
        if (!config.contains(name)){
            ConfigurationSection section = config.createSection(name);
            section.set("delay",delay);
            section.set("actions",actions);
            try {
                config.save(cfgFile);
                schedulers.add(new MixToolsScheduler(name, delay * 20L, actions));
            }
            catch (Exception e) {e.printStackTrace();}
        }
        return false;
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
            tasks.add(task);
        }
    }

    public void stopAllRunnable() {
        for (BukkitTask task : tasks){
            task.cancel();
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
