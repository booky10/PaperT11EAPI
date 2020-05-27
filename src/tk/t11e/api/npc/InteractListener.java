package tk.t11e.api.npc;
// Created by booky10 in PaperT11EAPI (17:21 26.02.20)

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tk.t11e.api.packets.PacketUseEntityEventID;
import tk.t11e.api.util.BungeeUtils;

import java.util.HashMap;
import java.util.UUID;

public class InteractListener implements Listener {

    public static final HashMap<UUID, Long> timeout = new HashMap<>();

    @EventHandler
    public void onPacketInteract(PacketUseEntityEventID event) {
        Player player = event.getAttacker();
        for (NPC npc : NPCRegistry.getNPCs())
            if (npc.getEntityID().equals(event.getId()))
                if (!(npc.getAction() == null || npc.getAction().equals(NPC.Action.NOTHING)))
                    switch (npc.getAction()) {
                        case EXECUTE_COMMAND:
                            player.chat(npc.getActionString());
                            break;
                        case SEND_SERVER:
                            if (!isInCooldown(player))
                                if (!isReallyEmpty(npc.getActionString())) {
                                    BungeeUtils.sendPlayerToServer(player, npc.getActionString());
                                    putCooldown(player, 5);
                                }
                            break;
                        default:
                            break;
                    }
    }

    public static Boolean isReallyEmpty(String string) {
        return string == null || string.equals("") || string.equals(" ") || string.isEmpty();
    }

    public static Boolean isInCooldown(Player player) {
        if (!timeout.containsKey(player.getUniqueId()))
            return false;
        else
            return timeout.get(player.getUniqueId()) >= System.currentTimeMillis();
    }

    public static void putCooldown(Player player, Integer seconds) {
        timeout.put(player.getUniqueId(), System.currentTimeMillis() + seconds * 1000);
    }
}