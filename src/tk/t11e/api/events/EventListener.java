package tk.t11e.api.events;
// Created by booky10 in PaperT11EAPI (09:43 17.06.20)

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

@Deprecated
public class EventListener implements Listener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;
        new ChunkGenerateEvent(event.getChunk()).callEvent();
    }
}