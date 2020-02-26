package tk.t11e.api.npc;
// Created by booky10 in PaperT11EAPI (20:48 22.02.20)

import org.bukkit.Bukkit;
import tk.t11e.api.main.Main;

import java.util.ArrayList;
import java.util.List;

public class NPCRegistry {

    private static final List<NPC> NPCs = new ArrayList<>();

    public static void register(NPC npc) {
        NPCs.add(npc);
    }

    public static void unmake() {
        for (NPC npc : NPCs)
            npc.remove();
    }

    public static void make() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(Main.class), () -> {
            for (NPC npc : NPCs) {
                Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(Main.class), () -> {
                    npc.remove();
                    npc.updateNPC();
                    npc.sendPackets();
                });
            }
        });
    }

    public static List<NPC> getNPCs() {
        return NPCs;
    }

    public static void unregister(NPC npc) {
        NPCs.remove(npc);
    }
}