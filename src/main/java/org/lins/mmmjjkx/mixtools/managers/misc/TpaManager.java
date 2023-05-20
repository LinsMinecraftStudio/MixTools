package org.lins.mmmjjkx.mixtools.managers.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsTeleportRequest;

import java.util.HashMap;
import java.util.Map;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.TPA_EXPIRE_TIME;

public class TpaManager {
    private final Map<MixToolsTeleportRequest,Integer> requestMap = new HashMap<>();
    public final int DEFAULT_EXPIRE_TIME = MixTools.settingsManager.getInt(TPA_EXPIRE_TIME);
    public void setExpireTime(MixToolsTeleportRequest request, int time){
        if(time < 1) {
            requestMap.remove(request);
        } else {
            requestMap.put(request, time);
        }
    }

    public int getExpireTime(MixToolsTeleportRequest request){
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

    public void buildRequestMessage(MixToolsTeleportRequest request,boolean here){
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
        if (here){
            MixTools.messageHandler.sendMessage(to,"TPA.HereRequest.Line1",to.getName());
        }else {
            MixTools.messageHandler.sendMessage(to, "TPA.Request.Line1", to.getName());
        }
        to.sendMessage(component3);
    }
}
