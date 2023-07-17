package org.lins.mmmjjkx.mixtools.commands.speed;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class CMDWalkSpeed extends SpeedCMD {
    public CMDWalkSpeed(String name) {
        super(name);
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    int maxSpeed() {
        return 10;
    }

    @Override
    int minSpeed() {
        return 1;
    }

    @Override
    void changePlayerSpeed(Player p, double speed) {
        float defaultSpeed = 0.10000000149011612f;
        p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed * defaultSpeed);
        sendMessage(p, "Speed.WalkSpeedSet", speed);
    }

    @Override
    void sendPlayerInfo(Player p) {
        sendMessage(p,"Speed.Current.WalkSpeed",p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
    }
}
