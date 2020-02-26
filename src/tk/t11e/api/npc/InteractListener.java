package tk.t11e.api.npc;
// Created by booky10 in PaperT11EAPI (17:21 26.02.20)

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tk.t11e.api.packets.PacketUseEntityEvent;
import tk.t11e.api.util.BungeeUtils;

public class InteractListener implements Listener {

    @EventHandler
    public void onPacketInteract(PacketUseEntityEvent event) {
        for (NPC npc : NPCRegistry.getNPCs())
            if (npc.getEntity().getId() == event.getId())
                if (!(npc.getAction() == null || npc.getAction().equals(NPC.Action.NOTHING)))
                    switch (npc.getAction()) {
                        case EXECUTE_COMMAND:
                            event.getAttacker().chat(npc.getActionString());
                            break;
                        case SEND_SERVER:
                            if (!isReallyEmpty(npc.getActionString()))
                                BungeeUtils.sendPlayerToServer(event.getAttacker(), npc.getActionString());
                            break;
                        default:
                            break;
                    }
    }

    public static Boolean isReallyEmpty(String string) {
        return string.equals("") || string.equals(" ") || string.isEmpty();
    }
}