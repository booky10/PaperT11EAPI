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

    private String name, skin, listName, actionString;
    private Location location;
    private final UUID uuid;
    private Boolean showInTabList;
    private Action action;
    private final Property property;

    private EntityPlayer entity;
    private final MinecraftServer server;
    private final WorldServer world;
    private GameProfile profile;
    private Player npc;

    public NPC(String name, Location location) {
        this(name, name, name, false, location,
                Action.NOTHING);
    }

    public NPC(String name, String skin, String listName, Boolean showInTabList, Location location,
               Action action) {
        this(name, skin, listName, UUID.randomUUID(), showInTabList, location, action);
    }

    public NPC(String name, String skin, String listName, UUID uuid, Boolean showInTabList, Location location,
               Action action) {
        this(name, skin, listName, uuid, showInTabList, location, action, null);
    }

    public NPC(String name, String skin, String listName, UUID uuid, Boolean showInTabList, Location location,
               Action action, Property property) {
        this.name = name;
        this.skin = skin;
        this.listName = listName;
        this.location = location;
        this.uuid = uuid;
        this.showInTabList = showInTabList;
        this.action = action;
        this.actionString = "";
        this.property = property;

        server = ((CraftServer) Bukkit.getServer()).getServer();
        world = ((CraftWorld) location.getWorld()).getHandle();
        MineskinClient mineskinClient = new MineskinClient();

        //Bukkit.getScheduler().runTaskAsynchronously(Main.main, this::updateNPC);
    }

    public NPC updateNPC() {
        remove();

        profile = new GameProfile(uuid, name);

        if (property == null) {
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

                    Property property = new Property(name, value, signature);
                    profile.getProperties().put(name, property);
                } catch (IOException e) {
                    System.out.println("[NPCs] Error loading Skin \"" + skin + "\"!");
                }
            }
        } else {
            profile.getProperties().get(property.getName()).clear();
            profile.getProperties().put(property.getName(), property);
        }

        entity = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
        npc = entity.getBukkitEntity().getPlayer();

        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(),
                location.getPitch());
        return this;
    }

    public Boolean getShowInTabList() {
        return showInTabList;
    }

    public NPC setShowInTabList(Boolean showInTabList) {
        this.showInTabList = showInTabList;
        return this;
    }

    public NPC removeFromTabList(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction
                .REMOVE_PLAYER, entity));
        return this;
    }

    public NPC removeFromTabList(Iterable<? extends Player> players) {
        for (Player player : players)
            removeFromTabList(player);
        return this;
    }

    public NPC removeFromTabList() {
        removeFromTabList(Bukkit.getOnlinePlayers());
        return this;
    }

    public NPC sendPackets(Iterable<? extends Player> players) {
        for (Player player : players)
            sendPacket(player);
        return this;
    }

    public NPC sendPackets() {
        sendPackets(Bukkit.getOnlinePlayers());
        return this;
    }

    public NPC sendPacket(Player player) {
        remove(player);

        if (entity != null) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction
                    .ADD_PLAYER, entity));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entity));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(entity, (byte) (location.getYaw() * 256 /
                    360)));

            if (!showInTabList)
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.main, () ->
                        removeFromTabList(player), 20);
        } else
            Bukkit.getScheduler().runTaskLater(Main.main, () -> sendPacket(player), 20);
        return this;
    }

    public NPC remove(Player player) {
        try {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            if (showInTabList)
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction
                        .REMOVE_PLAYER, entity));
            connection.sendPacket(new PacketPlayOutEntityDestroy(entity.getId()));
        } catch (NullPointerException ignored) {
        }
        return this;
    }

    public NPC remove(Iterable<? extends Player> players) {
        for (Player player : players)
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

    public Action getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public NPC setAction(Action action) {
        this.action = action;
        return this;
    }

    public NPC setActionString(String actionString) {
        this.actionString = actionString;
        return this;
    }

    public String getActionString() {
        return actionString;
    }

    public enum Action {

        EXECUTE_COMMAND,
        SEND_SERVER,
        NOTHING
    }
}