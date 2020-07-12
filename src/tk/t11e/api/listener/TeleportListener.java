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

    private final HashMap<UUID, Boolean> init = new HashMap<>();

    public TeleportListener() {
        for (Player player : Bukkit.getOnlinePlayers())
            init.put(player.getUniqueId(), true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (init.get(event.getPlayer().getUniqueId())) {
            init.put(event.getPlayer().getUniqueId(), false);
            for (NPC npc : NPCRegistry.getNPCs()) {
                npc.remove(event.getPlayer());
                Bukkit.getScheduler().runTaskLaterAsynchronously(PaperT11EAPIMain.main,
                        () -> npc.sendPacket(event.getPlayer()), 2);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        init.put(event.getPlayer().getUniqueId(), true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        init.put(event.getPlayer().getUniqueId(), true);
    }
}