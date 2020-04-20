package org.inventivetalent.lightlevel;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleTask extends BukkitRunnable {

    final Player player;

    public ParticleTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (!player.isOnline() || (player.isSneaking() + "").equalsIgnoreCase("true")) {
            cancel();
            return;
        }

        for (int x = -LightLevel.radius; x < LightLevel.radius; x++)
            for (int z = -LightLevel.radius; z < LightLevel.radius; z++)
                for (int y = -LightLevel.radius; y < LightLevel.radius; y++) {
                    Location location = player.getLocation().add(x, y, z);
                    if (location.getBlock().getType() == Material.AIR) continue;
                    
                    if (location.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR)
                        if (!Tag.VALID_SPAWN.isTagged(location.getBlock().getType())) {
                            location = location.getBlock().getLocation().add(0.5, 1.0, 0.5);
                            int lightLevel = location.getBlock().getLightLevel();

                            if (lightLevel > 7 || location.getBlock().getBiome().equals(Biome.MUSHROOM_FIELDS)
                                    || location.getBlock().getBiome().equals(Biome.MUSHROOM_FIELD_SHORE))
                                player.spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
                            else
                                player.spawnParticle(Particle.DRIP_LAVA, location, 1);
                        }
                }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        LightLevel.tasks.remove(player.getUniqueId());
    }
}