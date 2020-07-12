package tk.t11e.api.npc;
// Created by booky10 in PaperT11EAPI (20:48 22.02.20)

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineskin.MineskinClient;
import org.mineskin.customskins.CustomSkins;
import tk.t11e.api.main.PaperT11EAPIMain;
import tk.t11e.api.util.SpecificVersion;
import tk.t11e.api.util.VersionHelper;
import tk.t11e.api.util.VersionNotSupportException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class NPC {

    private String name, skin, listName, actionString;
    private Location location;
    private final UUID uuid;
    private Boolean showInTabList;
    private Action action;
    private Property property;
    private HashMap<ItemSlot, ItemStack> equipment;

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
    private final Class<?> entityEquipmentPacketClass = VersionHelper.getNMSClass("PacketPlayOutEntityEquipment");
    private final Class<?> craftItemStackClass = VersionHelper.getOBCClass("inventory.CraftItemStack");
    private final Class<?> itemStackClass = VersionHelper.getNMSClass("ItemStack");
    private final Class<?> enumItemSlotClass = VersionHelper.getNMSClass("EnumItemSlot");
    private final Class<?> entityTeleportPacketClass = VersionHelper.getNMSClass("PacketPlayOutEntityTeleport");
    private final Class<?> entityHeadRotationPacketClass = VersionHelper.getNMSClass("PacketPlayOutEntityHeadRotation");
    private final Class<?> entityLookPacketClass = VersionHelper.getNMSClass("PacketPlayOutEntity$PacketPlayOutEntityLook");
    private final Class<?> entityMoveLookPacketClass = VersionHelper.getNMSClass("PacketPlayOutEntity$PacketPlayOutRelEntityMoveLook");
    private final Class<?> animationPacketClass = VersionHelper.getNMSClass("PacketPlayOutAnimation");

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
        this.equipment = new HashMap<>();

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

    public void setProperty(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
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
                } catch (IOException exception) {
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

    public NPC removeFromTabList() {
        for (Player player : Bukkit.getOnlinePlayers())
            removeFromTabList(player);
        return this;
    }

    public NPC sendPackets() {
        for (Player player : Bukkit.getOnlinePlayers())
            sendPacket(player);
        return this;
    }

    public NPC sendPacket(Player player) {
        if (player.getWorld().getUID() != location.getWorld().getUID()) return this;
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
                Method nmsCopyMethod = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
                Constructor<?> entityEquipmentConstructor = entityEquipmentPacketClass.getConstructor(int.class,
                        enumItemSlotClass, itemStackClass);
                List<Object> equipmentPackets = new ArrayList<>();
                for (ItemSlot slot : equipment.keySet())
                    if (VersionHelper.aboveOr(slot.firstVersion))
                        equipmentPackets.add(entityEquipmentConstructor.newInstance((int) getEntityID(),
                                slot.getAsEnumItemSlot(), nmsCopyMethod.invoke(null, equipment.get(slot))));

                Object watcher = entityPlayerClass.getMethod("getDataWatcher").invoke(entity);
                Constructor<?> constructor = entityMetadataPacketClass.getConstructor(int.class,
                        dataWatcherClass, boolean.class);
                Object entityMetadataPacket = constructor.newInstance(getEntityID(), watcher, true);
                Method packetSendMethod = playerConnectionClass.getMethod("sendPacket", packetClass);

                packetSendMethod.invoke(connection, playerInfoPacket);
                packetSendMethod.invoke(connection, entitySpawnPacket);
                packetSendMethod.invoke(connection, headRotationPacket);
                packetSendMethod.invoke(connection, entityMetadataPacket);
                for (Object equipmentPacket : equipmentPackets)
                    packetSendMethod.invoke(connection, equipmentPacket);
            } catch (IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException | InstantiationException exception) {
                exception.printStackTrace();
            }

            /*DataWatcher watcher = entity.getDataWatcher();
            watcher.set(new DataWatcherObject<>(15, DataWatcherRegistry.a), (byte) 127);
            connection.sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), watcher, true));*/

            Bukkit.getScheduler().runTaskLaterAsynchronously(PaperT11EAPIMain.main, () -> {
                if (!showInTabList)
                    removeFromTabList(player);
                animate(0, player);
            }, 2);
        } else
            Bukkit.getScheduler().runTaskLater(PaperT11EAPIMain.main, () -> sendPacket(player), 20);
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

    public HashMap<ItemSlot, ItemStack> getEquipment() {
        return equipment;
    }

    public void setEquipment(HashMap<ItemSlot, ItemStack> equipment) {
        this.equipment = equipment;
    }

    public void putEquipment(ItemSlot slot, ItemStack item) {
        equipment.put(slot, item);
    }

    public void clearEquipment() {
        equipment.clear();
    }

    public NPC remove(Player player) {
        try {
            if (showInTabList)
                removeFromTabList();

            Object connection = getPlayerConnection(player);
            Constructor<?> packetConstructor = entityDestroyPacketClass.getConstructor(int[].class);
            Object packet = packetConstructor.newInstance((Object) new int[]{getEntityID()});

            playerConnectionClass.getMethod("sendPacket", packetClass).invoke(connection, packet);
        } catch (NullPointerException ignored) {
        } catch (IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public NPC remove() {
        for (Player player : Bukkit.getOnlinePlayers())
            remove(player);
        return this;
    }

    public void teleport(Location location) {
        try {
            this.location = location;
            Method locationMethod = entityClass.getMethod("setLocation",
                    double.class, double.class, double.class, float.class, float.class);
            locationMethod.invoke(entity, location.getX(), location.getY(), location.getZ(),
                    location.getYaw(), location.getPitch());
            remove().sendPackets();
        } catch (NoSuchMethodException | IllegalAccessException
                | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    public void move(Location location) {
        try {
            byte headYaw = (byte) (location.getYaw() * 256 / 360);
            short dx = (short) ((location.getX() * 32 - this.location.getX() * 32) * 128);
            short dy = (short) ((location.getY() * 32 - this.location.getY() * 32) * 128);
            short dz = (short) ((location.getZ() * 32 - this.location.getZ() * 32) * 128);
            byte yaw = (byte) location.getYaw();
            byte pitch = (byte) location.getPitch();

            Object packet = entityMoveLookPacketClass.getConstructor(int.class, short.class, short.class, short.class,
                    byte.class, byte.class, boolean.class).newInstance(getEntityID(), dx, dy, dz, yaw,
                    pitch, false);
            Object headPacket = entityHeadRotationPacketClass.getConstructor(entityClass, byte.class)
                    .newInstance(entity, headYaw);
            Method sendPacketMethod = playerConnectionClass.getMethod("sendPacket", packetClass);

            for (Player player : Bukkit.getOnlinePlayers())
                if (player != null && player.isOnline()) {
                    sendPacketMethod.invoke(getPlayerConnection(player), packet);
                    sendPacketMethod.invoke(getPlayerConnection(player), headPacket);
                }
            this.location = location;
        } catch (NoSuchMethodException | IllegalAccessException
                | InstantiationException | InvocationTargetException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public void look(float yaw, float pitch) {
        try {
            byte headYaw = (byte) (yaw * 256 / 360);

            Object lookPacket = entityLookPacketClass.getConstructor(int.class, byte.class, byte.class, boolean.class)
                    .newInstance(getEntityID(), (byte) yaw, (byte) pitch, false);
            Object headPacket = entityHeadRotationPacketClass.getConstructor(entityClass, byte.class)
                    .newInstance(entity, headYaw);
            Method sendPacketMethod = playerConnectionClass.getMethod("sendPacket", packetClass);

            for (Player player : Bukkit.getOnlinePlayers())
                if (player != null && player.isOnline()) {
                    sendPacketMethod.invoke(getPlayerConnection(player), lookPacket);
                    sendPacketMethod.invoke(getPlayerConnection(player), headPacket);
                }
        } catch (NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public void animate(int id) {
        for (Player player : Bukkit.getOnlinePlayers())
            animate(id, player);
    }

    public void animate(int id, Player player) {
        try {
            Object packet = animationPacketClass.getConstructor(entityClass, int.class).newInstance(entity, id);
            Method sendPacketMethod = playerConnectionClass.getMethod("sendPacket", packetClass);

            if (player == null || !player.isOnline()) return;
            sendPacketMethod.invoke(getPlayerConnection(player), packet);
        } catch (NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException(exception);
        }
    }

/*    public void setPose(Pose pose) {
        try {
            Object watcher = entityPlayerClass.getMethod("getDataWatcher").invoke(entity);
            Method watcherSet = null;
            for (Method method : dataWatcherClass.getMethods())
                if (method.getName().endsWith("set"))
                    watcherSet = method;
            if (watcherSet == null)
                throw new NoSuchMethodException("Method not found!");
            Object watcherObject = dataWatcherObjectClass.getConstructor(int.class,
                    dataWatcherSerializerClass).newInstance(6, dataWatcherRegistryClass
                    .getField("s").get(null));
            watcherSet.invoke(watcher, watcherObject, (byte) 127);


            WrapperPlayServerBed packet = new WrapperPlayServerBed();

            packet.setEntityID(id);
            packet.setLocation(new BlockPosition(location.toVector()));

            for (Player player : visible)
                if (player != null)
                    packet.sendPacket(player);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | InstantiationException exception) {
            throw new IllegalStateException(exception);
        }
    }*/

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

    @Deprecated
    @SpecificVersion(from = VersionHelper.Version.V1_14)
    public enum Pose {

        STANDING("STANDING", VersionHelper.Version.V1_7, 0),
        ELYTRA("FALL_FLYING", VersionHelper.Version.V1_9, 1),
        SLEEPING("SLEEPING", VersionHelper.Version.V1_7, 2),
        SWIMMING("SWIMMING", VersionHelper.Version.V1_13, 3),
        SPIN_ATTACK("SPIN_ATTACK", VersionHelper.Version.V1_9, 4),
        SNEAKING("SNEAKING", VersionHelper.Version.V1_7, 5),
        DYING("DYING", VersionHelper.Version.V1_7, 6);

        private final Class<?> entityPoseClass;
        private final String identifier;
        private final VersionHelper.Version firstVersion;
        private final int enumNumber;

        Pose(String identifier, VersionHelper.Version firstVersion, int enumNumber) {
            if (!VersionHelper.aboveOr114())
                throw new VersionNotSupportException();
            this.identifier = identifier;
            this.firstVersion = firstVersion;
            this.enumNumber = enumNumber;
            entityPoseClass = VersionHelper.getNMSClass("EntityPose");
        }

        public Object getAsEntityPose() {
            try {
                if (VersionHelper.aboveOr(firstVersion))
                    return entityPoseClass.getMethod("valueOf", String.class).invoke(null, identifier);
                else
                    throw new IllegalAccessException("Tried to access something, that (in this version)" +
                            " doesn't exits! (" + identifier + ", " + firstVersion.name() + ")");
            } catch (IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException exception) {
                throw new IllegalStateException(exception);
            }
        }

        public int getEnumNumber() {
            return enumNumber;
        }
    }

    public enum ItemSlot {

        HEAD("HEAD", VersionHelper.Version.V1_7),
        CHEST("CHEST", VersionHelper.Version.V1_7),
        LEGS("LEGS", VersionHelper.Version.V1_7),
        FEET("FEET", VersionHelper.Version.V1_7),
        MAIN_HAND("MAINHAND", VersionHelper.Version.V1_7),
        OFF_HAND("OFFHAND", VersionHelper.Version.V1_9);

        private final Class<?> enumItemSlotClass = VersionHelper.getNMSClass("EnumItemSlot");
        private final String identifier;
        private final VersionHelper.Version firstVersion;

        ItemSlot(String identifier, VersionHelper.Version firstVersion) {
            this.identifier = identifier;
            this.firstVersion = firstVersion;
        }

        public Object getAsEnumItemSlot() {
            try {
                if (VersionHelper.aboveOr(firstVersion))
                    return enumItemSlotClass.getMethod("valueOf", String.class).invoke(null, identifier);
                else
                    throw new IllegalAccessException("Tried to access something, that (in this version)" +
                            " doesn't exits! (" + identifier + ", " + firstVersion.name() + ")");
            } catch (IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException exception) {
                throw new IllegalStateException(exception);
            }
        }
    }
}