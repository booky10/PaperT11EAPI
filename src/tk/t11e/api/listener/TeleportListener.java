package tk.t11e.api.listener;
// Created by booky10 in PaperT11EAPI (19:56 25.02.20)

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import tk.t11e.api.main.PaperT11EAPIMain;
import tk.t11e.api.npc.NPC;
import tk.t11e.api.npc.NPCRegistry;

import java.util.HashMap;
import java.util.UUID;

public class TeleportListener implements Listener {

    private final HashMap<UUID, Integer> timeout = new HashMap<>();

    public TeleportListener() {
        for (Player player : Bukkit.getOnlinePlayers())
            timeout.put(player.getUniqueId(), 0);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        timeout.put(event.getPlayer().getUniqueId(), timeout.get(event.getPlayer().getUniqueId()) + 1);
        if (timeout.get(event.getPlayer().getUniqueId()) != 1) {
            if (timeout.get(event.getPlayer().getUniqueId()) >= 175)
                timeout.put(event.getPlayer().getUniqueId(), 0);
        } else
            for (NPC npc : NPCRegistry.getNPCs()) {
                npc.remove(event.getPlayer());
                Bukkit.getScheduler().runTaskLaterAsynchronously(PaperT11EAPIMain.main,
                        () -> npc.sendPacket(event.getPlayer()), 5);
            }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        timeout.put(event.getPlayer().getUniqueId(), 0);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        timeout.put(event.getPlayer().getUniqueId(), 0);
    }
}