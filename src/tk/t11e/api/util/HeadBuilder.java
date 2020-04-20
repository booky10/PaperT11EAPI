package tk.t11e.api.util;
// Created by booky10 in knockIt (19:36 14.01.20)

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class HeadBuilder {

    private int amount;
    private String name, skinName, textureProperty;
    private boolean unbreakable;
    private HashMap<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;
    private Integer customDataModel;
    private UUID skinUUID;

    public HeadBuilder(int amount, String name) {
        this.amount = amount;
        this.name = name;
        unbreakable = false;
        enchantments = new HashMap<>();
        itemFlags = new ArrayList<>();
        customDataModel = 0;
        skinName = null;
        skinUUID = null;
        textureProperty = null;
    }

    public String getSkinName() {
        return skinName;
    }

    public UUID getSkinUUID() {
        return skinUUID;
    }

    public HeadBuilder setSkinName(String skinName) {
        this.skinName = skinName;
        return this;
    }

    public HeadBuilder setSkinUUID(UUID skinUUID) {
        this.skinUUID = skinUUID;
        return this;
    }

    public HeadBuilder(int amount) {
        this(amount, "§fPlayer Head");
    }

    public HeadBuilder() {
        this(1);
    }

    public int getAmount() {
        return amount;
    }

    public HeadBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public HeadBuilder addAllItemFlags() {
        return addItemFlags(ItemFlag.values());
    }

    public HeadBuilder noName() {
        return setName(" ");
    }

    public HeadBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public HeadBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public HeadBuilder setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public HeadBuilder addItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.addAll(Arrays.asList(itemFlags));
        return this;
    }

    public HeadBuilder removeItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.removeAll(Arrays.asList(itemFlags));
        return this;
    }

    public HeadBuilder addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public HeadBuilder removeEnchantment(Enchantment enchantment) {
        enchantments.remove(enchantment);
        return this;
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public HeadBuilder setEnchantments(HashMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public Integer getCustomDataModel() {
        return customDataModel;
    }

    public HeadBuilder setCustomDataModel(Integer customDataModel) {
        this.customDataModel = customDataModel;
        return this;
    }

    public static HeadBuilder getSkull(String encodedTexture) {
        HeadBuilder builder = new HeadBuilder(1, "§fPlayer Head");
        builder.setTextureProperty(encodedTexture);
        builder.addAllItemFlags();
        return builder;
        /*ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", encodedTexture));
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
            Bukkit.getLogger().severe("Error during loading a player head!");
            Bukkit.getLogger().severe(exception.toString());
        }

        head.setItemMeta(headMeta);
        return head;*/
    }

    public String getTextureProperty() {
        return textureProperty;
    }

    public void setTextureProperty(String textureProperty) {
        this.textureProperty = textureProperty;
    }

    @SuppressWarnings("deprecation")
    public ItemStack build() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

        itemMeta.setUnbreakable(unbreakable);
        itemMeta.setDisplayName(name);
        itemMeta.setCustomModelData(customDataModel);
        if (textureProperty != null) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);

            profile.getProperties().put("textures", new Property("textures", textureProperty));
            Field profileField;
            try {
                profileField = itemMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(itemMeta, profile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
                Bukkit.getLogger().severe("Error during loading a player head!");
                Bukkit.getLogger().severe(exception.toString());
            }
        } else if (skinUUID != null)
            itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(skinUUID));
        else if (skinName != null)
            itemMeta.setOwner(skinName);

        for (ItemFlag itemFlag : itemFlags)
            itemMeta.addItemFlags(itemFlag);

        itemStack.setItemMeta(itemMeta);
        for (Enchantment enchantment : enchantments.keySet())
            itemStack.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
        return itemStack;
    }

    public static String getHeadTexture(String key) {
        switch (key.toLowerCase()) {
            case "up":
            case "arrow up":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThmZTI1MWE0" +
                        "MGU0MTY3ZDM1ZDA4MWMyNzg2OWFjMTUxYWY5NmI2YmQxNmRkMjgzNGQ1ZGM3MjM1ZjQ3NzkxZCJ9fX0=";
            case "down":
            case "arrow down":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI3Y2U2ODNk" +
                        "MDg2OGFhNDM3OGFlYjYwY2FhNWVhODA1OTZiY2ZmZGFiNmI1YWYyZDEyNTk1ODM3YTg0ODUzIn19fQ==";
            case "right":
            case "arrow right":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmM2EyZGZj" +
                        "ZTBjM2RhYjdlZTEwZGIzODVlNTIyOWYxYTM5NTM0YThiYTI2NDYxNzhlMzdjNGZhOTNiIn19fQ==";
            case "left":
            case "arrow left":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIwZjZlOGFm" +
                        "NDZhYzZmYWY4ODkxNDE5MWFiNjZmMjYxZDY3MjZhNzk5OWM2MzdjZjJlNDE1OWZlMWZjNDc3In19fQ==";
            case "?":
            case "question":
            case "question mark":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FhYjI3Mjg0" +
                        "MGQ3OTBjMmVkMmJlNWM4NjAyODlmOTVkODhlMzE2YjY1YzQ1NmZmNmEzNTE4MGQyZTViZmY2In19fQ==";
            case "!":
            case "exclamation":
            case "exclamation mark":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdkMTlhYWJm" +
                        "Y2ZkOTlmZmFiYTQyMTRjYWVmMjk5NTE2Y2U1MmU2ZDEzYmYyZGRhMTI1OTg1ZTQ4MWI3MmY5In19fQ==";
            case ".":
            case "dot":
            case "dot 1":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZmOTlmZjI3" +
                        "OWEyY2YyNWRlYjRiZDViNjZjMzU3NmI4MjRjYzk2YzM2NzgxMDI3YWY3MjdlZDNhNGMxMzA4ZSJ9fX0=";
            case "/":
            case "slash":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQ1OTNmMDk0" +
                        "NWNiYjg1YThlMGJlN2Q5YTUyNjAxMGVlNzc0ODEwZjJiYzQyOGNkNGEyM2U0ZDIzMmVmZjgifX19";

            case "a":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFjNThiMWEz" +
                        "YjUzYjk0ODFlMzE3YTFlYTRmYzVlZWQ2YmFmY2E3YTI1ZTc0MWEzMmU0ZTNjMjg0MTI3OGMifX19";
            case "b":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDRjNzExNTcx" +
                        "ZTdlMjE0ZWU3OGRmZTRlZTBlMTI2M2I5MjUxNmU0MThkZThmYzhmMzI1N2FlMDkwMTQzMSJ9fX0=";
            case "c":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZmNWFhYmVh" +
                        "ZDZmZWFmYWFlY2Y0NDIyY2RkNzgzN2NiYjM2YjAzYzk4NDFkZDFiMWQyZDNlZGI3ODI1ZTg1MSJ9fX0=";
            case "d":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkzZTYyMmI1" +
                        "ODE5NzU3OTJmN2MxMTllYzZmNDBhNGYxNmU1NTJiYjk4Nzc2YjBjN2FlMmJkZmQ0MTU0ZmU3In19fQ==";
            case "e":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE1N2Q2NWIx" +
                        "OTkyMWM3NjBmZjQ5MTBiMzQwNDQ1NWI5YzJlZTM2YWZjMjAyZDg1MzhiYWVmZWM2NzY5NTMifX19";
            case "f":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzU0Y2YyNjFi" +
                        "MmNkNmFiNTRiMGM2MjRmOGY2ZmY1NjVhN2I2M2UyOGUzYjUwYzZkYmZiNTJiNWYwZDdjZjlmIn19fQ==";
            case "g":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDNjOWY4YTc0" +
                        "Y2EwMWJhOGM1NGRlMWVkYzgyZTFmYzA3YTgzOTIzZTY2NTc0YjZmZmU2MDY5MTkyNDBjNiJ9fX0=";
            case "h":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjhjNThjNTA5" +
                        "MDM0NjE3YmY4MWVlMGRiOWJlMGJhM2U4NWNhMTU1NjgxNjM5MTRjODc2NjllZGIyZmQ3In19fQ==";
            case "i":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI0NjMyM2M5" +
                        "ZmIzMTkzMjZlZTJiZjNmNWI2M2VjM2Q5OWRmNzZhMTI0MzliZjBiNGMzYWIzMmQxM2ZkOSJ9fX0=";
            case "j":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzU4NDU2Y2Q5" +
                        "YmI4YTdlOTc4NTkxYWUwY2IyNmFmMWFhZGFkNGZhN2ExNjcyNWIyOTUxNDVlMDliZWQ4MDY0In19fQ==";
            case "k":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY0OWZiNzA4" +
                        "MzY5ZTdiYzI5NDRhZDcwNjk2M2ZiNmFjNmNlNmQ0YzY3MDgxZGRhZGVjZmU1ZGE1MSJ9fX0=";
            case "l":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM4NGY3NTQx" +
                        "NmU4NTNhNzRmNmM3MGZjN2UxMDkzZDUzOTYxODc5OTU1YjQzM2JkOGM3YzZkNWE2ZGYifX19";
            case "m":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFmZGU5MWIx" +
                        "OWI5MzA5OTEzNzI0ZmVhOWU4NTMxMTI3MWM2N2JjYjc4NTc4ZDQ2MWJmNjVkOTYxMzA3NCJ9fX0=";
            case "n":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3Yzk3MmU2" +
                        "Nzg1ZDZiMGFjZWI3NzlhYmRkNzcwMmQ5ODM0MWMyNGMyYTcxZTcwMjkzMGVjYTU4MDU1In19fQ==";
            case "o":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODA3M2JiNDRm" +
                        "OTM0NWY5YmIzMWE2NzkwMjdlNzkzOWU0NjE4NDJhOGMyNzQ4NmQ3YTZiODQyYzM5ZWIzOGM0ZSJ9fX0=";
            case "p":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRiMjMxYThk" +
                        "NTU4NzBjZmI1YTlmNGU2NWRiMDZkZDdmOGUzNDI4MmYxNDE2Zjk1ODc4YjE5YWNjMzRhYzk1In19fQ==";
            case "q":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZlZGQ2Zjll" +
                        "ZmRiMTU2Yjg2OTM1Njk5YjJiNDgzNGRmMGY1ZDIxNDUxM2MwMWQzOGFmM2JkMDMxY2JjYzkyIn19fQ==";
            case "r":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAzYTFjZDU4" +
                        "M2NiYmZmZGUwOGY5NDNlNTZhYzNlM2FmYWZlY2FlZGU4MzQyMjFhODFlNmRiNmM2NDY2N2Y3ZCJ9fX0=";
            case "s":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY1NzJlNjU1" +
                        "NzI1ZDc4Mzc1YTk4MTdlYjllZThiMzc4MjljYTFmZWE5M2I2MDk1Y2M3YWExOWU1ZWFjIn19fQ==";
            case "t":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzA4YzllZjNh" +
                        "Mzc1MWUyNTRlMmFmMWFkOGI1ZDY2OGNjZjVjNmVjM2VhMjY0MTg3N2NiYTU3NTgwN2QzOSJ9fX0=";
            case "u":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTVhNmUzYWU1" +
                        "YWU2MjU5MjM1MjQ4MzhmYWM5ZmVmNWI0MjUyN2Y1MDI3YzljYTE0OWU2YzIwNzc5MmViIn19fQ==";
            case "v":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTc1MTIxZjdk" +
                        "OWM2OGRhMGU1YjZhOTZhYzYxNTI5OGIxMmIyZWU1YmQxOTk4OTQzNmVlNjQ3ODc5ZGE1YiJ9fX0=";
            case "w":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjdlMTY1YzNl" +
                        "ZGM1NTQxZDQ2NTRjNDcyODg3MWU2OTA4ZjYxM2ZjMGVjNDZlODIzYzk2ZWFjODJhYzYyZTYyIn19fQ==";
            case "x":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTkxOWQxNTk0" +
                        "YmY4MDlkYjdiNDRiMzc4MmJmOTBhNjlmNDQ5YTg3Y2U1ZDE4Y2I0MGViNjUzZmRlYzI3MjIifX19";
            case "y":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM1NDI0YmI4" +
                        "NjMwNWQ3NzQ3NjA0YjEzZTkyNGQ3NGYxZWZlMzg5MDZlNGU0NThkZDE4ZGNjNjdiNmNhNDgifX19";
            case "z":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU5MTIwMGRm" +
                        "MWNhZTUxYWNjMDcxZjg1YzdmN2Y1Yjg0NDlkMzliYjMyZjM2M2IwYWE1MWRiYzg1ZDEzM2UifX19";

            case "1":
            case "one":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFhOTQ2M2Zk" +
                        "M2M0MzNkNWUxZDlmZWM2ZDVkNGIwOWE4M2E5NzBiMGI3NGRkNTQ2Y2U2N2E3MzM0OGNhYWIifX19";
            case "2":
            case "two":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWNiNDE5ZDk4" +
                        "NGQ4Nzk2MzczYzk2NDYyMzNjN2EwMjY2NGJkMmNlM2ExZDM0NzZkZDliMWM1NDYzYjE0ZWJlIn19fQ==";
            case "3":
            case "three":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjhlYmFiNTdi" +
                        "NzYxNGJiMjJhMTE3YmU0M2U4NDhiY2QxNGRhZWNiNTBlOGY1ZDA5MjZlNDg2NGRmZjQ3MCJ9fX0=";
            case "4":
            case "four":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjJiZmNmYjQ4" +
                        "OWRhODY3ZGNlOTZlM2MzYzE3YTNkYjdjNzljYWU4YWMxZjlhNWE4YzhhYzk1ZTRiYTMifX19";
            case "5":
            case "five":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWY0ZWNmMTEw" +
                        "YjBhY2VlNGFmMWRhMzQzZmIxMzZmMWYyYzIxNjg1N2RmZGE2OTYxZGVmZGJlZTdiOTUyOCJ9fX0=";
            case "6":
            case "six":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMzMWE2YTZm" +
                        "Y2Q2OTk1YjYyMDg4ZDM1M2JmYjY4ZDliODlhZTI1ODMyNWNhZjNmMjg4NjQ2NGY1NGE3MzI5In19fQ==";
            case "7":
            case "seven":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDRiYTZhYzA3" +
                        "ZDQyMjM3N2E4NTU3OTNmMzZkZWEyZWQyNDAyMjNmNTJmZDE2NDgxODE2MTJlY2QxYTBjZmQ1In19fQ==";
            case "8":
            case "eight":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzYxYThhNjQx" +
                        "NDM3YmU5YWVhMjA3MjUzZGQzZjI1NDQwZDk1NGVhMmI1ODY2YzU1MmYzODZiMjlhYzRkMDQ5In19fQ==";
            case "9":
            case "nine":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE5MjhlMWJm" +
                        "ZDg2YTliNzkzOTdjNGNiNGI2NWVmOTlhZjQ5YjdkNWY3OTU3YWQ2MmMwYzY5OWE2MjJjZmJlIn19fQ==";
            case "0":
            case "zero":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTVhMjI0ODA3" +
                        "NjkzOTc4ZWQ4MzQzNTVmOWU1MTQ1ZjljNTZlZjY4Y2Y2ZjJjOWUxNzM0YTQ2ZTI0NmFhZTEifX19";

            case ":":
            case "dot 2":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ4OThjNDBl" +
                        "NDdjNWQyZDc2OTI0MDY1MzYwNzY4MDY1ZDYyNGVlNWI5ZWUwYmU5ZTEyYjk4ZmI3N2M3NiJ9fX0=";
            case "ä":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGM5YzJiYmQ3" +
                        "YjdmNzIwNGRjZWI1NzI5YTZmYmE3ZmQ0NWQ2ZjE5M2YzNzYwZWM1OWE2ODA3NTMzZTYzYiJ9fX0=";
            case "ü":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2FlYzUzZTRh" +
                        "NmQyMjFhZmQ3Mjk3YjY1ZTU1YmU4NzkxM2NmOWNiN2Y0ZjQ1NDdmNzE4NjEyMDcwMWQ4ZCJ9fX0=";
            case "ö":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzgzZDQyYmNi" +
                        "OWI4ZTY2YzE2MTY2Y2NmMjYxZTJmOWY3OGM2OGVlNzg4NmRhMjI1ZTQzODk1Y2RiY2FmNWYifX19";

            case "-":
            case "minus":
            case "minus sign":
            case "subtract":
            case "subtract sign":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg2YzljOTc3" +
                        "YTM2ZDg5ZjVmNGU5ZDJhNmQ4YjhkNTg5YTc1MTE4YjFjZjllMzZjNGVhYzdlOWE2MTE2YSJ9fX0=";
            case "+":
            case "plus":
            case "plus sign":
            case "add":
            case "add sign":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7ImlkIjoiNjVmNTUzZGU2YzY2NDcyY2JhMTk4MjNiZmJhYjZjNjMiLCJ0eXBlIjoiU0tJTiIs" +
                        "InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmMwMjg3ODExZDY3OTkwMTIxZjY4OTc4NGUxNzN" +
                        "iYjA2MGQ1NzE3NDNmMzQ3ZjdlMmU4NjVlZjVlMGZkOWEyNCIsInByb2ZpbGVJZCI6ImQyMGM4YjNiOWY5ZTQ4ZGE5NGM0MmM2MT" +
                        "RkZDc2ZTI5IiwidGV4dHVyZUlkIjoiMmMwMjg3ODExZDY3OTkwMTIxZjY4OTc4NGUxNzNiYjA2MGQ1NzE3NDNmMzQ3ZjdlMmU4N" +
                        "jVlZjVlMGZkOWEyNCJ9fSwic2tpbiI6eyJpZCI6IjY1ZjU1M2RlNmM2NjQ3MmNiYTE5ODIzYmZiYWI2YzYzIiwidHlwZSI6IlNL" +
                        "SU4iLCJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJjMDI4NzgxMWQ2Nzk5MDEyMWY2ODk3ODR" +
                        "lMTczYmIwNjBkNTcxNzQzZjM0N2Y3ZTJlODY1ZWY1ZTBmZDlhMjQiLCJwcm9maWxlSWQiOiJkMjBjOGIzYjlmOWU0OGRhOTRjND" +
                        "JjNjE0ZGQ3NmUyOSIsInRleHR1cmVJZCI6IjJjMDI4NzgxMWQ2Nzk5MDEyMWY2ODk3ODRlMTczYmIwNjBkNTcxNzQzZjM0N2Y3Z" +
                        "TJlODY1ZWY1ZTBmZDlhMjQifSwiY2FwZSI6bnVsbH0=";
            case "rl":
            case "reload":
            case "rl sign":
            case "reload sign":
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjk3ZDZkN2Jl" +
                        "OTg1ZDA2MjJhNDhlOTA2OThlOTA3M2Y3ZmY4ODEzMjkyODEyZWJkMTczMGRiYTBlMDFjZjE4ZiJ9fX0=";

            default:
                return "MHF_Mineskin";
        }
    }

    public static HeadBuilder getHeadItem(String key) {
        return getSkull(getHeadTexture(key));
    }
}