package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (19:27 26.02.20)

import com.comphenix.packetwrapper.WrapperPlayServerBlockAction;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.sun.istack.internal.NotNull;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class OtherUtils {

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        Set<T> keys = getKeysByValue(map, value);
        if (keys.iterator().hasNext())
            return keys.iterator().next();
        else
            return null;
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
    public static void changeChestState(Player player, Location location, boolean open) {
        //player.playNote(location, (byte) 1, (byte) (open ? 1 : 0));
        /*BlockPosition pos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, open ? 1 : 0);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);*/

        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        WrapperPlayServerBlockAction blockAction = new WrapperPlayServerBlockAction();
        blockAction.setBlockType(location.getBlock().getType());
        blockAction.setLocation(blockPosition);
        blockAction.setByte1(1);
        blockAction.setByte2(open ? 1 : 0);
        blockAction.sendPacket(player);
    }

    public static <T> List<T> setToList(Set<T> set) {
        return new ArrayList<>(set);
    }
}