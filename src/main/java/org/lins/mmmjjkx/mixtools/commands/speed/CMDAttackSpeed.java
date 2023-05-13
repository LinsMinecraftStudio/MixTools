package org.lins.mmmjjkx.mixtools.commands.speed;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class CMDAttackSpeed extends SpeedCMD{

    @Override
    public String name() {
        return "attackspeed";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    int maxSpeed() {
        return 5;
    }

    @Override
    int minSpeed() {
        return 1;
    }

    @Override
    void changePlayerSpeed(Player p, double speed) {
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed * 4d);
        sendMessage(p, "Speed.AttackSpeedSet", speed);
    }
}
