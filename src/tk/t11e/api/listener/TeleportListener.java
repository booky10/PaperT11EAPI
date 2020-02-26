package tk.t11e.api.listener;
// Created by booky10 in PaperT11EAPI (19:56 25.02.20)

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import tk.t11e.api.main.Main;
import tk.t11e.api.npc.NPC;
import tk.t11e.api.npc.NPCRegistry;

public class TeleportListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (event.getFrom().getWorld().getUID() != event.getTo().getWorld().getUID())
            for (NPC npc : NPCRegistry.getNPCs())
                if (npc.getLocation().getWorld().getUID().equals(event.getTo().getWorld().getUID())) {
                    npc.remove(player);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.main, () -> npc.sendPacket(player),
                            20);
                }
    }
}