package tk.t11e.api.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tk.t11e.api.main.PaperT11EAPIMain;

public class PacketCore {

    public PacketCore(Plugin plugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null) return;

                EnumWrappers.EntityUseAction type = packet.getEntityUseActions().read(0);
                int entityId = packet.getIntegers().read(0);
                Entity entity = null;
                for (World worlds : Bukkit.getWorlds()) {
                    for (Entity entities : worlds.getEntities())
                        if (entities.getEntityId() == entityId)
                            entity = entities;
                }
                if (entity == null) entity = player;

                Entity finalEntity = entity;
                Bukkit.getScheduler().runTask(PaperT11EAPIMain.main,
                        () -> Bukkit.getServer().getPluginManager().callEvent(new PacketUseEntityEvent(type,
                                player, finalEntity)));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null) return;

                EnumWrappers.EntityUseAction type = packet.getEntityUseActions().read(0);
                int entityId = packet.getIntegers().read(0);

                Bukkit.getScheduler().runTask(PaperT11EAPIMain.main,
                        () -> Bukkit.getPluginManager().callEvent(new PacketUseEntityEventID(type, player,
                                entityId)));
                Bukkit.getScheduler().runTask(PaperT11EAPIMain.main,
                        () -> Bukkit.getPluginManager().callEvent(new PacketUseEntityEventID(type, player,
                                entityId)));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null) return;

                Location blockLocation = new Location(player.getWorld(),
                        packet.getBlockPositionModifier().read(0).getX(),
                        packet.getBlockPositionModifier().read(0).getY(),
                        packet.getBlockPositionModifier().read(0).getZ());

                Bukkit.getScheduler().runTask(PaperT11EAPIMain.main,
                        () -> Bukkit.getPluginManager().callEvent(new PacketBlockDigEvent(player,
                                blockLocation)));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Client.FLYING) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) return;

                Bukkit.getScheduler().runTask(PaperT11EAPIMain.main,
                        () -> Bukkit.getPluginManager().callEvent(new PacketFlyingEvent(player)));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null) return;

                String message = packet.getStrings().read(0);

                Bukkit.getScheduler().runTask(PaperT11EAPIMain.main,
                        () -> Bukkit.getPluginManager().callEvent(new PacketChatEvent(player, message)));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Client.KEEP_ALIVE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) return;

                Bukkit.getScheduler().runTask(PaperT11EAPIMain.main,
                        () -> Bukkit.getPluginManager().callEvent(new PacketKeepAliveEvent(player)));
            }
        });
        /*ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
                PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                Sound sound = packetEvent.getPacket().getSoundEffects().read(0);
                PacketNamedSoundEffectEvent event = new PacketNamedSoundEffectEvent(player, sound);
                Bukkit.getScheduler().runTask(PaperT11EAPIMain.main, () -> {
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled())
                        packetEvent.setCancelled(true);
                    else
                        packetEvent.getPacket().getSoundEffects().write(0, event.getSound());
                });
            }
        });*/
        //packet.getResourcePackStatus();
    }
}