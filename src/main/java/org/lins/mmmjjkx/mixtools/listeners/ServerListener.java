package org.lins.mmmjjkx.mixtools.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.google.common.io.Files;
import io.github.linsminecraftstudio.polymer.objects.plugin.SimpleSettingsManager;
import io.github.linsminecraftstudio.polymer.utils.ObjectConverter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerCommandEvent;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixToolsListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.MOTD_ENABLED;

public class ServerListener implements MixToolsListener {
    @EventHandler
    public void onMotd(PaperServerListPingEvent e){
        SimpleSettingsManager manager = MixTools.settingsManager;
        if (manager.getBoolean(MOTD_ENABLED)) {
            ConfigurationSection section = manager.getSection("motd");
            if (section == null) return;

            Random random = new Random();
            String motd;
            if (manager.getBoolean("motd.randomMotd")) {
                ConfigurationSection motds = section.getConfigurationSection("motds");
                if (motds == null) {
                    motd = section.getString("defaultMotd", "");
                } else {
                    String[] motdsKeys = motds.getKeys(false).toArray(new String[0]);
                    motd = motds.getString(motdsKeys[random.nextInt(motdsKeys.length)], "");
                }
            }else {
                motd = section.getString("defaultMotd","");
            }

            int onlinePlayers = Bukkit.getOnlinePlayers().size();
            motd = motd.replaceAll("%maxPlayers%", String.valueOf(Bukkit.getMaxPlayers()));
            motd = motd.replaceAll("%currentPlayers%", String.valueOf(onlinePlayers));
            motd = motd.replaceAll("%mspt%", String.valueOf(new DecimalFormat("#.00").format(Bukkit.getAverageTickTime())));
            motd = motd.replaceAll("%tps%", String.valueOf(Math.min(Math.round(Bukkit.getTPS()[0] * 100.0) / 100.0, 20.0)));

            e.motd(MiniMessage.miniMessage().deserialize(motd));
            if (section.getBoolean("changeVersion")) {
                e.setVersion(manager.getString("motd.version", true));
            }
            if (section.getBoolean("Xmore.enabled")) {
                ConfigurationSection xmore = section.getConfigurationSection("Xmore");
                if (xmore != null) {
                    int x = xmore.getInt("x", 1);
                    x += onlinePlayers;
                    e.setMaxPlayers(x);
                }
            }
            if (section.getBoolean("players.changeCurrentPlayers")) {
                e.setNumPlayers(section.getInt("players.currentPlayers"));
            }
            if (section.getBoolean("players.changeMaxPlayers")) {
                e.setMaxPlayers(section.getInt("players.maxPlayers"));
            }

            File icon = new File(MixTools.getInstance().getDataFolder(), section.getString("icon", "example.png"));
            if (!icon.exists()) {
                MixTools.warn("Cannot set the motd icon, because it's not exists.");
            } else if (!Files.getFileExtension(icon.getName()).equals("png")) {
                MixTools.warn("Cannot set the motd icon, because it's not a png file.");
            } else {
                try {e.setServerIcon(Bukkit.loadServerIcon(icon));
                } catch (Exception ex) {ex.printStackTrace();}
            }
            e.setHidePlayers(section.getBoolean("players.hidePlayers"));
        }
    }

    @EventHandler
    public void onCmdExecuted(ServerCommandEvent e){
        CommandSender sender = e.getSender();
        Logger logger = MixTools.getInstance().getLogger();
        if (sender instanceof BlockCommandSender bls) {
            logger.log(Level.WARNING, "[Command block in location {} is executed, command: {}]",
                    new Object[]{ObjectConverter.toLocationString(bls.getBlock().getLocation()),
                    e.getCommand()});
        }
        if (sender instanceof CommandMinecart cm) {
            logger.log(Level.WARNING, "[Command minecart in location {} is executed, command: {}]",
                    new Object[]{ObjectConverter.toLocationString(cm.getLocation()),
                            e.getCommand()});
        }
    }
}
