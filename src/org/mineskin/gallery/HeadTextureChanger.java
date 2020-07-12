package org.mineskin.gallery;

import org.bukkit.inventory.meta.SkullMeta;
import org.inventivetalent.mcwrapper.auth.GameProfileWrapper;
import org.inventivetalent.mcwrapper.auth.properties.PropertyWrapper;
import org.inventivetalent.reflection.resolver.*;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
import org.inventivetalent.reflection.resolver.minecraft.OBCClassResolver;

import java.lang.reflect.Field;
import java.util.UUID;

public class HeadTextureChanger {

    private static final ClassResolver classResolver = new ClassResolver();
    private static final NMSClassResolver nmsClassResolver = new NMSClassResolver();
    private static final OBCClassResolver obcClassResolver = new OBCClassResolver();

    private static final Class<?> NBTTagCompound = nmsClassResolver.resolveSilent("NBTTagCompound");
    private static final Class<?> NBTBase = nmsClassResolver.resolveSilent("NBTBase");
    private static final Class<?> GameProfileSerializer = nmsClassResolver.resolveSilent("GameProfileSerializer");
    private static final Class<?> CraftMetaSkull = obcClassResolver.resolveSilent("inventory.CraftMetaSkull");

    private static final Class<?> GameProfile = classResolver.resolveSilent("net.minecraft.util.com.mojang.authlib" +
            ".GameProfile", "com.mojang.authlib.GameProfile");

    private static final MethodResolver NBTTagCompoundMethodResolver = new MethodResolver(NBTTagCompound);
    private static final MethodResolver GameProfileSerializerMethodResolver = new MethodResolver(GameProfileSerializer);

    private static final FieldResolver CraftMetaSkullFieldResolver = new FieldResolver(CraftMetaSkull);

    private static final ConstructorResolver CraftMetaSkullConstructorResolver = new ConstructorResolver(CraftMetaSkull);

    public static Object createProfile(String data) {
        try {
            GameProfileWrapper profileWrapper = new GameProfileWrapper(UUID.randomUUID(), "CustomBlock");
            PropertyWrapper propertyWrapper = new PropertyWrapper("textures", data);
            profileWrapper.getProperties().put("textures", propertyWrapper);

            return profileWrapper.getHandle();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    public static Object createProfile(String value, String signature) {
        if (signature == null)
            return createProfile(value);
        try {
            GameProfileWrapper profileWrapper = new GameProfileWrapper(UUID.randomUUID(), "CustomBlock");
            PropertyWrapper propertyWrapper = new PropertyWrapper("textures", value, signature);
            profileWrapper.getProperties().put("textures", propertyWrapper);

            return profileWrapper.getHandle();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static SkullMeta applyTextureToMeta(SkullMeta meta, Object profile) throws Exception {
        if (meta == null)
            throw new IllegalArgumentException("Meta cannot be null!");
        if (profile == null)
            throw new IllegalArgumentException("Profile cannot be null!");
        Object baseNBTTag = NBTTagCompound.newInstance();
        Object ownerNBTTag = NBTTagCompound.newInstance();

        GameProfileSerializerMethodResolver.resolve(new ResolverQuery("serialize", NBTTagCompound,
                GameProfile)).invoke(null, ownerNBTTag, profile);
        NBTTagCompoundMethodResolver.resolve(new ResolverQuery("set", String.class, NBTBase))
                .invoke(baseNBTTag, "SkullOwner", ownerNBTTag);

        SkullMeta newMeta = (SkullMeta) CraftMetaSkullConstructorResolver.resolve(new Class[]{NBTTagCompound})
                .newInstance(baseNBTTag);
        Field profileField = CraftMetaSkullFieldResolver.resolve("profile");
        profileField.set(meta, profile);
        profileField.set(newMeta, profile);
        return newMeta;
    }
}