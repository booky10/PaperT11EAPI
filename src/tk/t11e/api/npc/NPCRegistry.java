package tk.t11e.api.npc;
// Created by booky10 in PaperT11EAPI (20:48 22.02.20)

import org.bukkit.Bukkit;
import tk.t11e.api.main.Main;
import tk.t11e.api.util.ExceptionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NPCRegistry {

    private static final List<NPC> NPCs = new ArrayList<>();
    private static final HashMap<String, List<NPC>> registered = new HashMap<>();

    public static void register(NPC npc) {
        NPCs.add(npc);

        List<NPC> NPCs = new ArrayList<>();
        if (registered.containsKey(npc.getListName()))
            NPCs = registered.get(npc.getListName());
        NPCs.add(npc);
        registered.put(npc.getListName(), NPCs);
    }

    public static void unmake() {
        for (NPC npc:NPCs)
            npc.remove();
    }

    public static void make() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(Main.class), () -> {
            for (NPC npc : NPCs) {
                Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(Main.class), () -> {
                    npc.remove();
                    npc.updateNPC();
                    npc.sendPackets();
                    try {
                        Thread.sleep(9000);
                    } catch (InterruptedException exception) {
                        ExceptionUtils.print(exception);
                    }
                });
            }
        });
    }

    public static List<NPC> getNPCs() {
        return NPCs;
    }

    public static void unregister(String name) {
        for (NPC ncp : registered.get(name)) {
            ncp.remove();
            NPCs.remove(ncp);
        }
        registered.remove(name);
    }
}