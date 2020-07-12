package tk.t11e.api.util.persistence.implementations;
// Created by booky10 in PaperT11EAPI (22:18 10.07.20)

import com.google.gson.internal.Primitives;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import tk.t11e.api.util.persistence.interfaces.PersistentDataContainer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Deprecated
public class PaperPersistentDataTypeRegistry {

    private final Function<Class<?>, TagAdapter<?, ?>> CREATE_ADAPTER = this::createAdapter;
    private final Map<Class<?>, TagAdapter<?, ?>> adapters = new HashMap<>();

    private <T> TagAdapter<?, ?> createAdapter(Class<T> type) {
        if (!Primitives.isWrapperType(type))
            type = Primitives.wrap(type);

        if (Objects.equals(Byte.class, type))
            return createAdapter(Byte.class, NBTTagByte.class, NBTTagByte::a, NBTTagByte::asByte);
        else if (Objects.equals(Short.class, type))
            return createAdapter(Short.class, NBTTagShort.class, NBTTagShort::a, NBTTagShort::asShort);
        else if (Objects.equals(Integer.class, type))
            return createAdapter(Integer.class, NBTTagInt.class, NBTTagInt::a, NBTTagInt::asInt);
        else if (Objects.equals(Long.class, type))
            return createAdapter(Long.class, NBTTagLong.class, NBTTagLong::a, NBTTagLong::asLong);
        else if (Objects.equals(Float.class, type))
            return createAdapter(Float.class, NBTTagFloat.class, NBTTagFloat::a, NBTTagFloat::asFloat);
        else if (Objects.equals(Double.class, type))
            return createAdapter(Double.class, NBTTagDouble.class, NBTTagDouble::a, NBTTagDouble::asDouble);
        else if (Objects.equals(String.class, type))
            return createAdapter(String.class, NBTTagString.class, NBTTagString::a, NBTTagString::asString);
        else if (Objects.equals(byte[].class, type))
            return createAdapter(byte[].class, NBTTagByteArray.class,
                    (array) -> new NBTTagByteArray(Arrays.copyOf(array, array.length)),
                    (n) -> Arrays.copyOf(n.getBytes(), n.size()));
        else if (Objects.equals(int[].class, type))
            return createAdapter(int[].class, NBTTagIntArray.class,
                    (array) -> new NBTTagIntArray(Arrays.copyOf(array, array.length)),
                    (n) -> Arrays.copyOf(n.getInts(), n.size()));
        else if (Objects.equals(long[].class, type))
            return createAdapter(long[].class, NBTTagLongArray.class,
                    (array) -> new NBTTagLongArray(Arrays.copyOf(array, array.length)),
                    (n) -> Arrays.copyOf(n.getLongs(), n.size()));
        else if (Objects.equals(PersistentDataContainer.class, type))
            return null;
            /*return createAdapter(PaperPersistentDataContainer.class, NBTTagCompound.class,
                    PaperPersistentDataContainer::toTagCompound, (tag) -> {
                PaperPersistentDataContainer container = new PaperPersistentDataContainer(this);
                for (String key : tag.getKeys())
                    container.put(key, tag.get(key));
                return container;
            });*/
        else
            throw new IllegalArgumentException("Could not find a valid TagAdapter implementation for the requested type " + type.getSimpleName());
    }

    private <T, Z extends NBTBase> TagAdapter<T, Z> createAdapter(Class<T> primitiveType, Class<Z> nbtBaseType, Function<T, Z> builder, Function<Z, T> extractor) {
        return new TagAdapter<>(primitiveType, nbtBaseType, builder, extractor);
    }

    public <T> NBTBase wrap(Class<T> type, T value) {
        return adapters.computeIfAbsent(type, CREATE_ADAPTER).build(value);
    }

    public <T> boolean isInstanceOf(Class<T> type, NBTBase base) {
        return adapters.computeIfAbsent(type, CREATE_ADAPTER).isInstance(base);
    }

    public <T> T extract(Class<T> type, NBTBase tag) throws ClassCastException, IllegalArgumentException {
        TagAdapter<?, ?> adapter = adapters.computeIfAbsent(type, CREATE_ADAPTER);
        Validate.isTrue(adapter.isInstance(tag), "`The found tag instance cannot store %s as it is a %s", type.getSimpleName(), tag.getClass().getSimpleName());
        Object foundValue = adapter.extract(tag);
        Validate.isInstanceOf(type, foundValue, "The found object is of the type %s. Expected type %s", foundValue.getClass().getSimpleName(), type.getSimpleName());
        return type.cast(foundValue);
    }

    private static class TagAdapter<T, Z extends NBTBase> {

        private final Function<T, Z> builder;
        private final Function<Z, T> extractor;
        private final Class<T> primitiveType;
        private final Class<Z> nbtBaseType;

        public TagAdapter(Class<T> primitiveType, Class<Z> nbtBaseType, Function<T, Z> builder,
                          Function<Z, T> extractor) {
            this.primitiveType = primitiveType;
            this.nbtBaseType = nbtBaseType;
            this.builder = builder;
            this.extractor = extractor;
        }

        T extract(NBTBase base) {
            Validate.isInstanceOf(nbtBaseType, base, "The provided NBTBase was of the type %s. Expected type %s", base.getClass().getSimpleName(), nbtBaseType.getSimpleName());
            return extractor.apply(nbtBaseType.cast(base));
        }

        Z build(Object value) {
            Validate.isInstanceOf(primitiveType, value, "The provided value was of the type %s. Expected type %s", value.getClass().getSimpleName(), primitiveType.getSimpleName());
            return builder.apply(primitiveType.cast(value));
        }

        boolean isInstance(NBTBase base) {
            return nbtBaseType.isInstance(base);
        }
    }
}