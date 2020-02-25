package tk.t11e.api.packets;
// Created by booky10 in PaperT11EAPI (20:58 10.02.20)

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("NullableProblems")
public class PacketKeepAliveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public PacketKeepAliveEvent(Player player) {
        this.player=player;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}