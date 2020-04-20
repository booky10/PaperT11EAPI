package tk.t11e.api.npc;
// Created by booky10 in PaperT11EAPI (20:48 22.02.20)

import org.bukkit.Bukkit;
import tk.t11e.api.main.Main;

import java.util.ArrayList;
import java.util.List;

public class NPCRegistry {

    private static List<NPC> NPCs;

    public static void register(NPC npc) {
        if (!NPCs.contains(npc))
            NPCs.add(npc);
    }

    public static void unmake() {
        if (NPCs == null)
            NPCs = new ArrayList<>();
        else {
            List<NPC> NPCs = new ArrayList<>(getNPCs());
            for (NPC npc : NPCs)
                npc.remove();
        }
    }

    public static void make() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(Main.class), () -> {
            if (NPCs == null)
                NPCs = new ArrayList<>();
            for (NPC npc : NPCs) {
                npc.remove();
                npc.updateNPC();
                Bukkit.getScheduler().runTaskAsynchronously(Main.main, (Runnable) npc::sendPackets);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static List<NPC> getNPCs() {
        if (NPCs == null)
            NPCs = new ArrayList<>();
        return NPCs;
    }

    public static void unregister(NPC npc) {
        if (NPCs == null)
            NPCs = new ArrayList<>();
        NPCs.remove(npc);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}