package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (19:27 26.02.20)

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import javax.annotation.CheckReturnValue;
import java.util.*;
import java.util.stream.Collectors;

public class OtherUtils {

    @CheckReturnValue
    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @CheckReturnValue
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        Set<T> keys = getKeysByValue(map, value);
        if (keys.iterator().hasNext())
            return keys.iterator().next();
        else
            return null;
    }

    @CheckReturnValue
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }

    public static String toString(Location location) {
        if (location == null) throw new IllegalArgumentException("Location is null");
        String stringLocation = location.getClass().getName() + ":";
        stringLocation += location.getWorld().getUID() + ":";
        stringLocation += location.getX() + ":";
        stringLocation += location.getY() + ":";
        stringLocation += location.getZ() + ":";
        stringLocation += location.getYaw() + ":";
        stringLocation += Float.toString(location.getPitch());
        return stringLocation;
    }

    public static Location fromString(String location) {
        if (location == null) throw new IllegalArgumentException("Location String is null");
        String[] split = location.split(":");
        if (split.length != 7) throw new IllegalArgumentException("Location String not right (Length not 7)");
        if (!split[0].equals(Location.class.getName())) throw new IllegalArgumentException("Location String not right (Unknown Class Name)");
        World world;
        try {
            world = Bukkit.getWorld(UUID.fromString(split[1]));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Location String not right (World not found)", exception);
        }
        try {
            double x = Double.parseDouble(split[2]);
            double y = Double.parseDouble(split[3]);
            double z = Double.parseDouble(split[4]);
            float yaw = Float.parseFloat(split[5]);
            float pitch = Float.parseFloat(split[6]);
            try {
                return new Location(world, x, y, z, yaw, pitch);
            } catch (Exception exception) {
                throw new IllegalArgumentException("Something went wrong while creating Location from String", exception);
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Location String not right (Coordinate/Direction not a Number)", exception);
        }
    }

    public static void move(Entity entity, Location location) {
        Location location1 = entity.getLocation();
        location1.add(0.0d, 0.0d, 0.0d);

        double gravity = -0.008d;
        double distance = location.distance(location1);

        double vectorX =
                (1.0d + 0.07000000000000001d * distance) * (location.getX() - location1.getX()) / distance;
        double vectorY =
                (1.0d + 0.03d * distance) * (location.getY() - location1.getY()) / distance - 0.5d * gravity * distance;
        double vectorZ =
                (1.0d + 0.07000000000000001d * distance) * (location.getZ() - location1.getZ()) / distance;

        Vector vector = entity.getVelocity();
        vector.setX(vectorX);
        vector.setY(vectorY + 0.5);
        vector.setZ(vectorZ);

        entity.setVelocity(vector);
    }

    public static void moveHigh(Entity entity, Location location) {
        Location location1 = entity.getLocation();
        location1.add(0.0d, 0.0d, 0.0d);
        entity.teleport(location1);

        double gravity = -0.008d;
        double distance = location.distance(location1);

        double vectorX =
                (1.0d + 0.07000000000000001d * distance) * (location.getX() - location1.getX()) / distance;
        double vectorY =
                (1.0d + 0.03d * distance) * (location.getY() - location1.getY()) / distance - 0.5d * gravity * distance;
        double vectorZ =
                (1.0d + 0.07000000000000001d * distance) * (location.getZ() - location1.getZ()) / distance;

        Vector vector = entity.getVelocity();
        vector.setX(vectorX);
        vector.setY(vectorY + 1);
        vector.setZ(vectorZ);

        entity.setVelocity(vector);
    }

    @Deprecated
    public static <T> T[] toArray(Collection<T> a) {
        //noinspection unchecked
        T[] b = (T[]) new Object[a.size()];
        a.toArray(b);
        return b;
    }

    @CheckReturnValue
    public static <T> ArrayList<T> iterableToList(Iterable<T> iterable) {
        ArrayList<T> list = new ArrayList<>();
        for (T object : iterable)
            list.add(object);
        return list;
    }

    public static void changeChestTitle(Block chest, String name) {
        if (!VersionHelper.aboveOr111())
            throw new IllegalStateException("Chest Title change only allowed in 1.11+!");
        if (!(chest.getState() instanceof Chest)) return;

        Chest chestState = (Chest) chest.getState();
        chestState.setCustomName("My Chest Inventory Title!");
    }

    @NotNull
    @CheckReturnValue
    public static <T, E> HashMap<T, E> listsToMap(@NotNull List<T> keyList,
                                                  @NotNull List<E> valueList) {
        if (keyList.size() != valueList.size())
            return null;

        HashMap<T, E> hashMap = new HashMap<>();
        for (int i = 0; i < keyList.size(); i++)
            hashMap.put(keyList.get(i), valueList.get(i));

        return hashMap;
    }

    /*public static void playChestAction(Player player, Chest chest, boolean open) {
        /*Location location = chest.getLocation();
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
        TileEntityChest tileChest = (TileEntityChest) world.getTileEntity(position);

    }*/

    //@SuppressWarnings("deprecation")
    /*public static void changeChestState(Player player, Location location, boolean open) {
        //player.playNote(location, (byte) 1, (byte) (open ? 1 : 0));
        /*BlockPosition pos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, open ? 1 : 0);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        WrapperPlayServerBlockAction blockAction = new WrapperPlayServerBlockAction();
        blockAction.setBlockType(location.getBlock().getType());
        blockAction.setLocation(blockPosition);
        blockAction.setByte1(1);
        blockAction.setByte2(open ? 1 : 0);
        blockAction.sendPacket(player);
    }*/

    public static <T> List<T> setToList(Set<T> set) {
        return new ArrayList<>(set);
    }

    @CheckReturnValue
    public static String generateBox(CharSequence text) {
        int maxLength = -1;
        StringBuilder message = new StringBuilder();
        StringBuilder hyphens = new StringBuilder();
        StringBuilder spaces = new StringBuilder();
        List<StringBuilder> builders = new ArrayList<>();

        for (String string : text.toString().split("\n")) {
            StringBuilder builder = new StringBuilder(string);
            if (maxLength == -1)
                maxLength = builder.length();
            else
                maxLength = Math.max(maxLength, builder.length());
            builders.add(builder);
        }

        while (hyphens.length() < maxLength)
            hyphens.append("-");
        while (spaces.length() < maxLength)
            spaces.append(" ");

        message.append("\n");
        message.append("|-").append(hyphens).append("-|\n");
        message.append("| ").append(spaces).append(" |\n");

        for (StringBuilder builder : builders) {
            while (builder.length() < maxLength)
                builder.append(" ");
            message.append("| ").append(builder).append(" |\n");
        }

        message.append("| ").append(spaces).append(" |\n");
        message.append("|-").append(hyphens).append("-|\n");
        message.append("\n");
        return message.toString();
    }

    @CheckReturnValue
    public static String generateBox(CharSequence... text) {
        if (text == null) return "";
        StringBuilder builder = new StringBuilder();
        for (CharSequence sequence : text)
            builder.append(sequence).append("\n");
        return generateBox(builder.toString());
    }
}