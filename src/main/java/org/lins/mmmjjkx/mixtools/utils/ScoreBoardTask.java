package org.lins.mmmjjkx.mixtools.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.List;
import java.util.Set;

public class ScoreBoardTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Scoreboard playerScoreboard = player.getScoreboard();
                if (!isScoreboardEmpty(playerScoreboard)) {
                    Objective objective = playerScoreboard.getObjective(DisplaySlot.SIDEBAR);
                    if (objective != null) {
                        objective.unregister();
                    }
                }
                List<String> strings = MixTools.settingsManager.getSection("scoreboard").getStringList("scoreboard");
                Objective objective = playerScoreboard.registerNewObjective("SCOREBOARD_TITLE", Criteria.DUMMY,
                        MixTools.settingsManager.getComponent("scoreboard.title", true));
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                for (String string : strings){
                    objective.getScore(parse(player, string)).setScore(0);
                }
                player.setScoreboard(playerScoreboard);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isScoreboardEmpty(Scoreboard scoreboard){
        return scoreboard.equals(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    private String parse(Player player, String text) {
        return MixTools.hookManager.checkPAPIInstalled() ? MixTools.messageHandler.legacyColorize(PlaceholderAPI.setPlaceholders(player, text))
                : MixTools.messageHandler.legacyColorize(text);
    }
}
