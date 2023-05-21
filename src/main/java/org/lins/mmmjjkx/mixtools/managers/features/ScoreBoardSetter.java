package org.lins.mmmjjkx.mixtools.managers.features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.utils.ScoreBoardTask;

import java.util.HashMap;
import java.util.Map;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.SCOREBOARD_ENABLED;

public class ScoreBoardSetter {
    private static final Map<Player, BukkitTask> taskMap = new HashMap<>();
    public static void addPlayer(Player p){
        if (MixTools.settingsManager.getBoolean(SCOREBOARD_ENABLED)) {
            taskMap.put(p, new ScoreBoardTask(p).runTaskAsynchronously(MixTools.INSTANCE));
        }
    }

    public static void removePlayer(Player p){
        if (MixTools.settingsManager.getBoolean(SCOREBOARD_ENABLED)) {
            BukkitTask task = taskMap.get(p);
            if (!task.isCancelled()) {
                task.cancel();
            }
            taskMap.remove(p);
        }
    }

    public static void stopTasks(){
        if (MixTools.settingsManager.getBoolean(SCOREBOARD_ENABLED)) {
            for (BukkitTask task : taskMap.values()) {
                task.cancel();
            }
            taskMap.clear();
        }
    }

    public static void restart(){
        if (MixTools.settingsManager.getBoolean(SCOREBOARD_ENABLED)) {
            stopTasks();
            for (Player player : Bukkit.getOnlinePlayers()) {
                addPlayer(player);
            }
        }
    }
}
