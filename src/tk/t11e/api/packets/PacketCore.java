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

public class PacketCore {

    public PacketCore(Plugin plugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType
                .Play.Client.USE_ENTITY) {
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null)return;

                EnumWrappers.EntityUseAction type = packet.getEntityUseActions().read(0);
                int entityId = packet.getIntegers().read(0);
                Entity entity = null;
                for (World worlds : Bukkit.getWorlds()) {
                    for (Entity entities : worlds.getEntities()) {
                        if (entities.getEntityId() == entityId) {
                            entity = entities;
                        }
                    }
                }
                if (entity == null) {
                    entity = player;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketUseEntityEvent(type, player,
                        entity));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType
                .Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null) return;

                Location blockLocation = new Location(player.getWorld(),
                        packet.getBlockPositionModifier().read(0).getX(),
                        packet.getBlockPositionModifier().read(0).getY(),
                        packet.getBlockPositionModifier().read(0).getZ());

                Bukkit.getServer().getPluginManager().callEvent(new PacketBlockDigEvent(player,
                        blockLocation));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType
                        .Play.Client.FLYING) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) return;

                Bukkit.getServer().getPluginManager().callEvent(new PacketFlyingEvent(player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType
                .Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null)return;

                String message=packet.getStrings().read(0);

                Bukkit.getPluginManager().callEvent(new PacketChatEvent(player,message));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType
                .Play.Client.KEEP_ALIVE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null)return;

                Bukkit.getPluginManager().callEvent(new PacketKeepAliveEvent(player));
            }
        });


        //packet.getResourcePackStatus();//TODO
    }
}