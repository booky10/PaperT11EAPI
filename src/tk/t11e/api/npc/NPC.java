package tk.t11e.api.npc;
// Created by booky10 in PaperT11EAPI (20:48 22.02.20)

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineskin.MineskinClient;
import org.mineskin.customskins.CustomSkins;
import tk.t11e.api.main.Main;
import tk.t11e.api.util.VersionHelper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.UUID;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class NPC {

    private String name, skin, listName, actionString;
    private Location location;
    private final UUID uuid;
    private Boolean showInTabList;
    private Action action;
    private final Property property;

    private Object entity = null;
    private Object server = null;
    private Object world = null;
    private GameProfile profile;
    private Player npc;

    private final Class<?> craftPlayerClass = VersionHelper.getOBCClass("entity.CraftPlayer");
    private final Class<?> entityPlayerClass = VersionHelper.getNMSClass("EntityPlayer");
    private final Class<?> entityHumanClass = VersionHelper.getNMSClass("EntityHuman");
    private final Class<?> entityClass = VersionHelper.getNMSClass("Entity");
    private final Class<?> minecraftServerClass = VersionHelper.getNMSClass("MinecraftServer");
    private final Class<?> worldServerClass = VersionHelper.getNMSClass("WorldServer");
    private final Class<?> playerInteractManagerClass = VersionHelper.getNMSClass("PlayerInteractManager");
    private final Class<?> playerConnectionClass = VersionHelper.getNMSClass("PlayerConnection");
    private final Class<?> packetClass = VersionHelper.getNMSClass("Packet");
    private final Class<?> playerInfoPacketClass = VersionHelper.getNMSClass("PacketPlayOutPlayerInfo");
    private final Class<?> playerInfoEnumClass = VersionHelper.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
    private final Class<?> entityDestroyPacketClass = VersionHelper.getNMSClass("PacketPlayOutEntityDestroy");
    private final Class<?> entitySpawnPacketClass = VersionHelper.getNMSClass("PacketPlayOutNamedEntitySpawn");
    private final Class<?> headRotationPacketClass = VersionHelper.getNMSClass("PacketPlayOutEntityHeadRotation");
    private final Class<?> dataWatcherClass = VersionHelper.getNMSClass("DataWatcher");
    private final Class<?> dataWatcherObjectClass = VersionHelper.getNMSClass("DataWatcherObject");
    private final Class<?> dataWatcherRegistryClass = VersionHelper.getNMSClass("DataWatcherRegistry");
    private final Class<?> dataWatcherSerializerClass = VersionHelper.getNMSClass("DataWatcherSerializer");
    private final Class<?> entityMetadataPacketClass = VersionHelper.getNMSClass("PacketPlayOutEntityMetadata");

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

        try {
            Class<?> craftServerClass = VersionHelper.getOBCClass("CraftServer");
            server = craftServerClass.getMethod("getServer").invoke(craftServerClass.cast(Bukkit.getServer()));
            Class<?> craftWorldClass = VersionHelper.getOBCClass("CraftWorld");
            world = craftWorldClass.getMethod("getHandle").invoke(craftWorldClass.cast(location.getWorld()));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            exception.printStackTrace();
        }
        MineskinClient mineskinClient = new MineskinClient();

        try {
            sendPackets();
            updateNPC();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            NPCRegistry.register(this);
        }
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

        try {
            entity = entityPlayerClass.getConstructor(minecraftServerClass, worldServerClass, GameProfile.class,
                    playerInteractManagerClass).newInstance(server, world, profile, playerInteractManagerClass
                    .getConstructor(worldServerClass).newInstance(world));

            Object watcher = entityPlayerClass.getMethod("getDataWatcher").invoke(entity);
            Method watcherSet = null;
            for (Method method : dataWatcherClass.getMethods())
                if (method.getName().endsWith("set"))
                    watcherSet = method;
            if (watcherSet == null)
                throw new NoSuchMethodException("Method not found!");
            Object watcherObject = dataWatcherObjectClass.getConstructor(int.class,
                    dataWatcherSerializerClass).newInstance(16, dataWatcherRegistryClass
                    .getField("a").get(null));
            watcherSet.invoke(watcher, watcherObject, (byte) 127);

            npc = (Player) craftPlayerClass.getMethod("getPlayer").invoke(entityPlayerClass
                    .getMethod("getBukkitEntity").invoke(entity));

            entityPlayerClass.getMethod("setLocation", double.class, double.class, double.class, float.class,
                    float.class).invoke(entity, location.getX(), location.getY(), location.getZ(), location.getYaw(),
                    location.getPitch());
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException
                | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public Boolean getShowInTabList() {
        return showInTabList;
    }

    public NPC setShowInTabList(Boolean showInTabList) {
        this.showInTabList = showInTabList;
        return this;
    }

    public Integer getEntityID() {
        if (entity != null)
            try {
                return (int) entityClass.getMethod("getId").invoke(entity);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        return 0;
    }

    public NPC removeFromTabList(Player player) {
        try {
            Object connection = entityPlayerClass.getField("playerConnection").get(craftPlayerClass
                    .getMethod("getHandle").invoke(craftPlayerClass.cast(player)));

            Object playerInfoPacket = playerInfoPacketClass.getConstructor(playerInfoEnumClass,
                    Iterable.class).newInstance(playerInfoEnumClass.getEnumConstants()[4],
                    Collections.singletonList(entity));

            playerConnectionClass.getMethod("sendPacket", packetClass).invoke(connection, playerInfoPacket);
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException
                | InvocationTargetException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
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
            Object connection = getPlayerConnection(player);

            try {
                Object playerInfoPacket = playerInfoPacketClass.getConstructor(playerInfoEnumClass,
                        Iterable.class).newInstance(playerInfoEnumClass.getEnumConstants()[0],
                        Collections.singletonList(entity));
                Object entitySpawnPacket = entitySpawnPacketClass.getConstructor(entityHumanClass)
                        .newInstance(entity);
                Object headRotationPacket = headRotationPacketClass.getConstructor(entityClass,
                        byte.class).newInstance(entity, (byte) (location.getYaw() * 256 / 360));

                Object watcher = entityPlayerClass.getMethod("getDataWatcher").invoke(entity);

                Constructor<?> constructor = entityMetadataPacketClass.getConstructor(int.class,
                        dataWatcherClass, boolean.class);
                Object entityMetadataPacket = constructor.newInstance(getEntityID(), watcher, true);

                playerConnectionClass.getMethod("sendPacket", packetClass).invoke(connection, playerInfoPacket);
                playerConnectionClass.getMethod("sendPacket", packetClass).invoke(connection, entitySpawnPacket);
                playerConnectionClass.getMethod("sendPacket", packetClass).invoke(connection, headRotationPacket);
                playerConnectionClass.getMethod("sendPacket", packetClass).invoke(connection, entityMetadataPacket);
            } catch (IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException | InstantiationException exception) {
                exception.printStackTrace();
            }

            /*DataWatcher watcher = entity.getDataWatcher();
            watcher.set(new DataWatcherObject<>(15, DataWatcherRegistry.a), (byte) 127);
            connection.sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), watcher, true));*/

            if (!showInTabList)
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.main, () ->
                        removeFromTabList(player), 20);
        } else
            Bukkit.getScheduler().runTaskLater(Main.main, () -> sendPacket(player), 20);
        return this;
    }

    private Object getPlayerConnection(Player player) {
        try {
            return entityPlayerClass.getField("playerConnection").get(craftPlayerClass
                    .getMethod("getHandle").invoke(craftPlayerClass.cast(player)));
        } catch (IllegalAccessException | NoSuchFieldException
                | InvocationTargetException | NoSuchMethodException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public NPC remove(Player player) {
        try {
            if (showInTabList)
                removeFromTabList();

            Object connection = getPlayerConnection(player);
            Constructor<?> packetConstructor = entityDestroyPacketClass.getConstructor(int[].class);
            //int id = (int) entityClass.getMethod("getId").invoke(entity);
            Object packet = packetConstructor.newInstance((Object) new int[]{getEntityID()});

            playerConnectionClass.getMethod("sendPacket", packetClass).invoke(connection, packet);
        } catch (NullPointerException ignored) {
        } catch (IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public NPC remove(Iterable<? extends Player> players) {
        if (players != null)
            for (Player player : players)
                remove(player);
        return this;
    }

    public NPC remove() {
        remove(Bukkit.getOnlinePlayers());
        NPCRegistry.unregister(this);
        return this;
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