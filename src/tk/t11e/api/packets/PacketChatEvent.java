package tk.t11e.api.packets;
// Created by booky10 in PaperT11EAPI (20:50 10.02.20)

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("NullableProblems")
public class PacketChatEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String message;

    public PacketChatEvent(Player player, String message) {
        this.player=player;
        this.message=message;
    }

    public String getMessage() {
        return message;
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