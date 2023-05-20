package org.lins.mmmjjkx.mixtools.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.google.common.io.Files;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixToolsListener;

import java.io.File;

public class ServerListener implements MixToolsListener {
    @EventHandler
    public void onMotd(PaperServerListPingEvent e){
        ConfigurationSection section = MixTools.settingsManager.getSection("motd");
        if (section == null) return;
        String motd = section.getString("string","");
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        motd = motd.replaceAll("%maxPlayers%", String.valueOf(Bukkit.getMaxPlayers()));
        motd = motd.replaceAll("%currentPlayers%", String.valueOf(onlinePlayers));
        motd = motd.replaceAll("%mspt%", String.valueOf(Bukkit.getAverageTickTime()));
        motd = motd.replace("%tps%", String.valueOf(Math.min(Math.round(Bukkit.getTPS()[0] * 100.0) / 100.0, 20.0)));
        Component component = PlainTextComponentSerializer.plainText().deserialize(motd);
        e.motd(component);
        if (section.getBoolean("changeVersion")) {
            e.setVersion(MixTools.settingsManager.getString("motd.version", true));
        }
        if (section.getBoolean("Xmore.enabled")) {
            ConfigurationSection xmore = section.getConfigurationSection("xmore");
            if (xmore != null) {
                int x = xmore.getInt("x",1);
                x += onlinePlayers;
                e.setMaxPlayers(x);
            }
        }
        String protocolVersion = section.getString("protocolVersion","auto");
        if (!protocolVersion.equals("auto")) {
            if (section.isInt(protocolVersion)) {
                e.setProtocolVersion(section.getInt("protocolVersion"));
            }
        }
        if (section.getBoolean("players.changeCurrentPlayers")) {
            e.setNumPlayers(section.getInt("players.currentPlayers"));
        }
        File icon = new File(MixTools.INSTANCE.getDataFolder(), section.getString("icon","example.png"));
        if (!icon.exists()) {
            MixTools.warn("Cannot set the motd icon, because it's not exists.");
        }else if (!Files.getFileExtension(icon.getName()).equals("png")) {
            MixTools.warn("Cannot set the motd icon, because it's not a png file.");
        }else {
            try {e.setServerIcon(Bukkit.loadServerIcon(icon));
            } catch (Exception ex) {throw new RuntimeException(ex);}
        }
        e.setHidePlayers(section.getBoolean("players.hidePlayers"));
    }
}
