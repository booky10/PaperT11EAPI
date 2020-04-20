package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (19:27 26.02.20)

import com.rylinaux.plugman.util.PluginUtil;
import com.sun.istack.internal.NotNull;
import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftChest;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
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
        return getKeysByValue(map, value).iterator().next();
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

    public <T> T[] toArray(List<T> a) {
        //noinspection unchecked
        T[] b = (T[]) new Object[a.size()];
        a.toArray(b);
        return b;
    }

    public static void changeChestTitle(Block chest, String name) {
        if (!(chest.getState() instanceof CraftChest)) return;
        CraftChest craftChest = (CraftChest) chest.getState();
        IChatBaseComponent iName = new ChatMessage(name);
        craftChest.getTileEntity().setCustomName(iName);
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
}