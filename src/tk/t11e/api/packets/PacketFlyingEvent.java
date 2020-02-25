package tk.t11e.api.packets;
// Created by booky10 in NoCheatT11E (13:38 10.02.20)

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("NullableProblems")
public class PacketFlyingEvent extends Event {

    private final Player player;
    private static final HandlerList handlers = new HandlerList();

    public PacketFlyingEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}