package org.lins.mmmjjkx.mixtools.managers.misc;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.MixToolsTeleportRequest;

import java.util.HashMap;
import java.util.Map;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.TPA_COOLDOWN;

public class TpaManager {
    private final Map<MixToolsTeleportRequest,Integer> requestMap = new HashMap<>();
    public final int DEFAULT_COOLDOWN = MixTools.settingsManager.getInt(TPA_COOLDOWN);
    public void setCooldown(MixToolsTeleportRequest request, int time){
        if(time < 1) {
            requestMap.remove(request);
        } else {
            requestMap.put(request, time);
        }
    }

    public int getCooldown(MixToolsTeleportRequest request){
        return requestMap.getOrDefault(request, 0);
    }

    public MixToolsTeleportRequest getRequest(Player to){
        for (MixToolsTeleportRequest r: requestMap.keySet()) {
            if (r.to().getName().equals(to.getName())) {
                return r;
            }
        }
        return null;
    }

    public void buildRequest(MixToolsTeleportRequest request){
        Player to = request.to();
        TextComponent component = LegacyComponentSerializer.legacyAmpersand().deserialize(
                MixTools.messageHandler.getColored("TPA.Request.Accept"));
        component = component.clickEvent(ClickEvent.runCommand("/tpaaccept"));
        component = component.hoverEvent(LegacyComponentSerializer.legacyAmpersand().deserialize(
                MixTools.messageHandler.getColored("TPA.Request.Hover.Accept")));
        TextComponent component2 = LegacyComponentSerializer.legacyAmpersand().deserialize(
                MixTools.messageHandler.getColored("TPA.Request.Refuse"));
        component2 = component2.clickEvent(ClickEvent.runCommand("/tparefuse"));
        component2 = component2.hoverEvent(LegacyComponentSerializer.legacyAmpersand().deserialize(
                MixTools.messageHandler.getColored("TPA.Request.Hover.Refuse")));
        TextComponent component3 = component.append(Component.space()).append(component2);
        MixTools.messageHandler.sendMessage(to,"TPA.Request.Line1",to.getName());
        MixTools.adventure.player(to).sendMessage(component3);
    }
}
