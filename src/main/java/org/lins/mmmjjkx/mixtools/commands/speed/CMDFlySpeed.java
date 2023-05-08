package org.lins.mmmjjkx.mixtools.commands.speed;

import org.bukkit.entity.Player;

public class CMDFlySpeed extends SpeedCMD {
    @Override
    public String name() {
        return "flyspeed";
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
        p.setFlySpeed((float) (speed / 5));
        sendMessage(p, "Speed.FlyingSpeedSet", speed);
    }
}
