package org.inventivetalent.lightlevel;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import tk.t11e.api.main.Main;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LightLevel implements Listener {

    public static final int radius = 8;
    public static final Set<UUID> tasks = new HashSet<>();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, Main.main);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if ((player.isSneaking() + "").equalsIgnoreCase("true")) return;
        if (player.getGameMode().equals(GameMode.ADVENTURE)
                || player.getGameMode().equals(GameMode.SPECTATOR)) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.TORCH) return;
        if (tasks.contains(player.getUniqueId())) return;

        new ParticleTask(player).runTaskTimer(Main.main, 0, 30);
        tasks.add(player.getUniqueId());
    }
}