package tk.t11e.api.packets;
// Created by booky10 in PaperT11EAPI (20:09 25.04.20)

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Deprecated
public class PacketNamedSoundEffectEvent extends PlayerEvent implements Cancellable {

    private boolean cancelled;
    private Sound sound;

    public PacketNamedSoundEffectEvent(Player player, Sound sound) {
        super(player);
        this.cancelled = false;
        this.sound=sound;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled=cancelled;
    }
}