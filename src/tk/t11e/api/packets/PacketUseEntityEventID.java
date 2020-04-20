package tk.t11e.api.packets;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("NullableProblems")
public class PacketUseEntityEventID extends Event {

    private final EnumWrappers.EntityUseAction action;
    private final Player attacker;
    private final Integer id;
    private static final HandlerList handlers = new HandlerList();

    public PacketUseEntityEventID(EnumWrappers.EntityUseAction action, Player attacker, Integer id) {
        this.action = action;
        this.attacker = attacker;
        this.id=id;
    }

    public EnumWrappers.EntityUseAction getAction() {
        return this.action;
    }

    public Player getAttacker() {
        return this.attacker;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Integer getId() {
        return id;
    }
}
