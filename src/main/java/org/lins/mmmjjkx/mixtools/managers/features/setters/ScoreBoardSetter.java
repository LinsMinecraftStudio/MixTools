package org.lins.mmmjjkx.mixtools.managers.features.setters;

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
            taskMap.put(p, new ScoreBoardTask().runTask(MixTools.INSTANCE));
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
        for (Player player : taskMap.keySet()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            taskMap.get(player).cancel();
        }
        taskMap.clear();
    }

    public static void restart(){
        stopTasks();
        if (MixTools.settingsManager.getBoolean(SCOREBOARD_ENABLED)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                addPlayer(player);
            }
        }
    }
}
