package tk.t11e.api.npc;
// Created by booky10 in PaperT11EAPI (20:48 22.02.20)

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.mineskin.MineskinClient;
import org.mineskin.customskins.CustomSkins;
import tk.t11e.api.main.Main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class NPC {

    private String name, skin, listName;
    private Location location;
    private final UUID uuid;

    private EntityPlayer entity;
    private final MinecraftServer server;
    private final WorldServer world;
    private GameProfile profile;
    private Player npc;

    public NPC(String name, Location location) {
        this(name, name, name, location);
    }

    public NPC(String name, String skin, String listName,Location location) {
        this(name, skin, listName, UUID.randomUUID(), location);
    }

    public NPC(String name, String skin, String listName,UUID uuid, Location location) {
        this.name = name;
        this.skin = skin;
        this.listName = listName;
        this.location = location;
        this.uuid = uuid;

        server = ((CraftServer) Bukkit.getServer()).getServer();
        world = ((CraftWorld) location.getWorld()).getHandle();
        MineskinClient mineskinClient = new MineskinClient();

        //Bukkit.getScheduler().runTaskAsynchronously(Main.main, this::updateNPC);
    }

    public NPC updateNPC() {
        remove();

        profile = new GameProfile(uuid, name);

        File skinFile = new File(CustomSkins.skinFolder, skin + ".json");
        if (!skinFile.exists())
            System.out.println("[NPCs] Please use \"/createcustomskin\" first!");
        else {
            try {
                JsonObject skinData = new JsonParser().parse(new FileReader(skinFile)).getAsJsonObject();
                JsonObject properties = skinData.getAsJsonArray("properties").get(0).getAsJsonObject();

                String name = properties.get("name").getAsString();
                String value = properties.get("value").getAsString();
                String signature = properties.get("signature").getAsString();

                Property property=new Property(name,value,signature);
                profile.getProperties().get(name).clear();
                profile.getProperties().put(name,property);
            } catch (IOException e) {
                System.out.println("[NPCs] Error loading Skin \""+skin+"\"!");
            }
        }

        entity = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
        npc = entity.getBukkitEntity().getPlayer();

        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(),
                location.getPitch());

        Bukkit.getScheduler().runTaskLater(Main.main, () -> entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(),
                location.getPitch()),20);

        return this;
    }

    public NPC sendPackets(Iterable<? extends Player> players) {
        for (Player player :players)
            sendPacket(player);
        return this;
    }

    public NPC sendPackets() {
        sendPackets(Bukkit.getOnlinePlayers());
        return this;
    }

    public NPC sendPacket(Player player) {
        remove(player);

        if(entity != null) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction
                    .ADD_PLAYER, entity));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entity));
        }else
            Bukkit.getScheduler().runTaskLater(Main.main, () -> sendPacket(player),20);
        return this;
    }

    public NPC remove(Player player) {
        try {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction
                    .REMOVE_PLAYER, entity));
            connection.sendPacket(new PacketPlayOutEntityDestroy(entity.getId()));
        }catch (NullPointerException ignored) {}
        return this;
    }

    public NPC remove(Iterable<? extends Player> players) {
        for (Player player:players)
            remove(player);
        return this;
    }

    public NPC remove() {
        remove(Bukkit.getOnlinePlayers());
        return this;
    }

    public EntityPlayer getEntity() {
        return entity;
    }

    public GameProfile getProfile() {
        return profile;
    }

    public Location getLocation() {
        return location;
    }

    public Player getNPC() {
        return npc;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getSkinName() {
        return skin;
    }

    public String getListName() {
        return listName;
    }

    public NPC setName(String name) {
        this.name = name;
        updateNPC();
        return this;
    }

    public NPC setSkinName(String skin) {
        this.skin = skin;
        updateNPC();
        return this;
    }

    public NPC setLocation(Location location) {
        this.location = location;
        updateNPC();
        return this;
    }

    public NPC setListName(String listName) {
        this.listName = listName;
        updateNPC();
        return this;
    }

    public NPC setSkin(String skin) {
        this.skin = skin;
        updateNPC();
        return this;
    }
}