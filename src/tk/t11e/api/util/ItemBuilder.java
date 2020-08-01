package tk.t11e.api.util;
// Created by booky10 in knockIt (19:36 14.01.20)

import com.sun.istack.internal.NotNull;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("UnusedReturnValue")
public class ItemBuilder {

    private static Method setUnbreakableMethod, getUnbreakableMethod, spigotMethod;

    static {
        if (VersionHelper.belowOr18())
            try {
                Class<?> spigotClass = Class.forName("org.bukkit.inventory.meta.ItemMeta$Spigot");
                setUnbreakableMethod = spigotClass.getMethod("setUnbreakable", boolean.class);
                getUnbreakableMethod = spigotClass.getMethod("isUnbreakable");
                //noinspection JavaReflectionMemberAccess
                spigotMethod = ItemMeta.class.getMethod("spigot");
            } catch (ClassNotFoundException | NoSuchMethodException exception) {
                throw new IllegalStateException(exception);
            }
    }

    private Material material;
    private short damage;
    private int amount;
    private String name;
    private boolean unbreakable;
    private Map<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;
    private List<String> lore;
    private Integer customDataModel;

    public ItemBuilder(Material material, int amount, String name, short damage) {
        this.amount = amount;
        this.material = material;
        this.name = name;
        this.damage = damage;
        unbreakable = false;
        enchantments = new HashMap<>();
        itemFlags = new ArrayList<>();
        lore = new ArrayList<>();
        customDataModel = 0;
    }

    public ItemBuilder(Material material, int amount, short damage) {
        this(material, amount, "§f" + material.name().toLowerCase().replace('_', ' '), damage);
    }

    public ItemBuilder(Material material) {
        this(material, (short) 0);
    }

    public ItemBuilder(Material material, short damage) {
        this(material, 1, damage);
    }

    public int getAmount() {
        return amount;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public short getDamage() {
        return damage;
    }

    public ItemBuilder setDamage(short damage) {
        this.damage = damage;
        return this;
    }

    @SuppressWarnings("deprecation")
    public static ItemBuilder fromItemStack(@NotNull ItemStack from) {
        ItemBuilder builder = new ItemBuilder(from.getType(), from.getAmount(), getRealDisplayName(from), from.getDurability());
        if (VersionHelper.aboveOr111())
            builder.setUnbreakable(from.getItemMeta().isUnbreakable());
        else
            try {
                builder.setUnbreakable((Boolean) getUnbreakableMethod.invoke(spigotMethod.invoke(from.getItemMeta())));
            } catch (IllegalAccessException | InvocationTargetException exception) {
                throw new IllegalStateException(exception);
            }
        builder.setEnchantments(from.getEnchantments());
        builder.setItemFlags(OtherUtils.setToList(from.getItemMeta().getItemFlags()));
        builder.setLore(from.getItemMeta().getLore());
        if (VersionHelper.aboveOr114())
            builder.setCustomDataModel(from.getItemMeta().getCustomModelData());
        return builder;
    }

    public static String getRealDisplayName(ItemStack item) {
        if (item.getItemMeta().hasDisplayName())
            return item.getItemMeta().getDisplayName();
        else if (VersionHelper.isPaper() && VersionHelper.aboveOr112())
            return item.getI18NDisplayName();
        else
            return item.getType().name().toLowerCase().replace('_', ' ');
    }

    public ItemBuilder addAllItemFlags() {
        return addItemFlags(ItemFlag.values());
    }

    public ItemBuilder noName() {
        return setName(" ");
    }

    public Material getMaterial() {
        return material;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public ItemBuilder setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.addAll(Arrays.asList(itemFlags));
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.removeAll(Arrays.asList(itemFlags));
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        enchantments.remove(enchantment);
        return this;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public ItemBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public Integer getCustomDataModel() {
        return customDataModel;
    }

    public ItemBuilder setCustomDataModel(Integer customDataModel) {
        this.customDataModel = customDataModel;
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder clearLore() {
        lore.clear();
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        lore.add(line);
        return this;
    }

    public ItemBuilder addLoreLines(String... lines) {
        lore.addAll(Arrays.asList(lines));
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemStack build() {
        ItemStack itemStack;
        if (!VersionHelper.aboveOr113() || material.isLegacy())
            itemStack = new ItemStack(material, amount, damage);
        else
            itemStack = new ItemStack(material, amount);
        if (material != Material.AIR) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (VersionHelper.aboveOr111())
                itemMeta.setUnbreakable(unbreakable);
            else
                try {
                    setUnbreakableMethod.invoke(spigotMethod.invoke(itemMeta), unbreakable);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    throw new IllegalStateException(exception);
                }
            itemMeta.setDisplayName(name);
            if (VersionHelper.aboveOr114())
                itemMeta.setCustomModelData(customDataModel);
            itemMeta.setLore(lore);
            for (ItemFlag itemFlag : itemFlags)
                itemMeta.addItemFlags(itemFlag);

            itemStack.setItemMeta(itemMeta);
            for (Enchantment enchantment : enchantments.keySet())
                itemStack.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
        }
        return itemStack.clone();
    }
}