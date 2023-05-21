package org.lins.mmmjjkx.mixtools.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
                    MiniMessage.miniMessage().deserialize(
                            MixTools.hookManager.checkPAPIInstalled() ?
                                    PlaceholderAPI.setPlaceholders(player, strings.get(0)) :
                                    strings.get(0)));
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (int i = 1; i < strings.size()-1; i++) {
                Component component = MiniMessage.miniMessage().deserialize(strings.get(i));
                LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().character('&')
                        .hexColors().build();
                sidebar.getScore(serializer.serialize(component)).setScore(0);
            }
        }
        return scoreboardList;
    }

    @Override
    public void run() {
        long delay = MixTools.settingsManager.getSection("scoreboard").getLong("changeDelay")*20L;
        new BukkitRunnable() {
            private int index = 0;
            @Override
            public void run() {
                player.setScoreboard(getScoreboards().get(index));
                index += 1;
            }
        }.runTaskTimerAsynchronously(MixTools.INSTANCE,delay,delay);
    }
}
