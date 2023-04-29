package org.lins.mmmjjkx.mixtools.events;

import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class SignEditEvent extends Event {
    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }

    public static HandlerList getHandlerList() {
        return new HandlerList();
    }

    private final Sign sign;

    public SignEditEvent(Sign sign){
        this.sign = sign;
    }

    public Sign getSign() {return sign;}
}
