package tk.t11e.api.npc;
// Created by booky10 in PaperT11EAPI (20:48 22.02.20)

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
        if (NPCs == null)
            NPCs = new ArrayList<>();
        for (NPC npc : NPCs) {
            npc.remove();
            npc.updateNPC();
            npc.sendPackets();
        }
    }

    public static List<NPC> getNPCs() {
        if (NPCs == null)
            NPCs = new ArrayList<>();
        return NPCs;
    }

    public static void unregister(NPC npc) {
        if (NPCs == null)
            NPCs = new ArrayList<>();
        else
            NPCs.remove(npc);
    }
}