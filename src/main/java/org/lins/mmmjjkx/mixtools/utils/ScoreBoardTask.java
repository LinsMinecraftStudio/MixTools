package org.lins.mmmjjkx.mixtools.utils;

import io.github.linsminecraftstudio.polymer.Polymer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardTask extends BukkitRunnable {
    private long second;
    private final Player player;
    public ScoreBoardTask(Player p){
        this.player = p;
    }

    private List<Scoreboard> getScoreboards(){
        ConfigurationSection section = MixTools.settingsManager.getSection("scoreboard");
        if (section == null) {
            return new ArrayList<>();
        }
        ConfigurationSection scoreboards = section.getConfigurationSection("scoreboards");
        if (scoreboards == null) {
            return new ArrayList<>();
        }
        List<Scoreboard> scoreboardList = new ArrayList<>();
        for (String key : scoreboards.getKeys(false)) {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            List<String> strings = scoreboards.getStringList(key);
            Objective sidebar = scoreboard.registerNewObjective(key, Criteria.DUMMY,
                    Polymer.serializer.deserialize(
                            MixTools.hookManager.checkPAPIInstalled() ?
                                    PlaceholderAPI.setPlaceholders(player, strings.get(0)) :
                                    strings.get(0)));
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (int i = 1; i < strings.size()-1; i++) {
                sidebar.getScore(MixTools.messageHandler.legacyColorize(strings.get(i))).setScore(0);
            }
            scoreboardList.add(scoreboard);
        }
        return scoreboardList;
    }

    @Override
    public void run() {
        long delay = MixTools.settingsManager.getSection("scoreboard").getLong("changeDelay")*20L;
        new BukkitRunnable() {
            @Override
            public void run() {
                while (MixTools.INSTANCE.isEnabled()) {
                    List<Scoreboard> scoreboards = getScoreboards();
                    int index = 0;
                    if (second % delay == 0) {
                        index = (int) (second / delay) - 1;
                        if (index < 0) index = 0;
                        if (index >= scoreboards.size()) {
                            index = 0;
                            second = 0;
                        }
                        player.setScoreboard(scoreboards.get(index));
                    }else {
                        player.setScoreboard(scoreboards.get(index));
                    }
                    second += 20L;
                }
            }
        }.runTask(MixTools.INSTANCE);
    }
}
