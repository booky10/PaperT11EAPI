package tk.t11e.api.listener;
// Created by booky10 in PaperT11EAPI (20:32 10.02.20)

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tk.t11e.api.npc.NPC;
import tk.t11e.api.npc.NPCRegistry;
import tk.t11e.api.util.UUIDFetcher;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        UUIDFetcher.nameCache.put(player.getUniqueId(), player.getName());
        UUIDFetcher.uuidCache.put(player.getName(), player.getUniqueId());

        for (NPC npc:NPCRegistry.getNPCs())
            npc.sendPacket(player);
    }
}