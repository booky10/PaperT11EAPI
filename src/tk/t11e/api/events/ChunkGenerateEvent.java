package tk.t11e.api.events;
// Created by booky10 in PaperT11EAPI (09:42 17.06.20)

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkEvent;

public class ChunkGenerateEvent extends ChunkEvent {

    public ChunkGenerateEvent(Chunk chunk) {
        super(chunk);
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }
}