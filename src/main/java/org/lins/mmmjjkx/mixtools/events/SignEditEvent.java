package org.lins.mmmjjkx.mixtools.events;

import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SignEditEvent extends Event {
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }

    private final Sign sign;

    public SignEditEvent(Sign sign){
        this.sign = sign;
    }

    public Sign getSign() {return sign;}
}
