package tk.t11e.api.packets;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("NullableProblems")
public class PacketUseEntityEvent extends Event {

    private final EnumWrappers.EntityUseAction action;
    private final Player attacker;
    private final Entity entity;
    private static final HandlerList handlers = new HandlerList();

    public PacketUseEntityEvent(EnumWrappers.EntityUseAction action, Player attacker, Entity entity) {
        this.action = action;
        this.attacker = attacker;
        this.entity=entity;
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

    public Entity getEntity() {
        return entity;
    }
}
