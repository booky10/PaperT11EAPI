package tk.t11e.api.util.persistence.interfaces;

import com.sun.istack.internal.NotNull;

@Deprecated
@SuppressWarnings("ALL")
public interface PersistentDataType<T, Z> {

    PersistentDataType<Byte, Byte> BYTE = new PersistentDataType.PrimitivePersistentDataType<>(Byte.class);
    PersistentDataType<Short, Short> SHORT = new PersistentDataType.PrimitivePersistentDataType<>(Short.class);
    PersistentDataType<Integer, Integer> INTEGER = new PersistentDataType.PrimitivePersistentDataType<>(Integer.class);
    PersistentDataType<Long, Long> LONG = new PersistentDataType.PrimitivePersistentDataType<>(Long.class);
    PersistentDataType<Float, Float> FLOAT = new PersistentDataType.PrimitivePersistentDataType<>(Float.class);
    PersistentDataType<Double, Double> DOUBLE = new PersistentDataType.PrimitivePersistentDataType<>(Double.class);
    PersistentDataType<String, String> STRING = new PersistentDataType.PrimitivePersistentDataType<>(String.class);
    PersistentDataType<byte[], byte[]> BYTE_ARRAY = new PersistentDataType.PrimitivePersistentDataType<>(byte[].class);
    PersistentDataType<int[], int[]> INTEGER_ARRAY = new PersistentDataType.PrimitivePersistentDataType<>(int[].class);
    PersistentDataType<long[], long[]> LONG_ARRAY = new PersistentDataType.PrimitivePersistentDataType<>(long[].class);
    PersistentDataType<PersistentDataContainer, PersistentDataContainer> TAG_CONTAINER = new PersistentDataType.PrimitivePersistentDataType<>(PersistentDataContainer.class);

    @NotNull
    Class<T> getPrimitiveType();

    @NotNull
    Class<Z> getComplexType();

    @NotNull
    T toPrimitive(@NotNull Z complex, @NotNull PersistentDataAdapterContext context);

    @NotNull
    Z fromPrimitive(@NotNull T primitive, @NotNull PersistentDataAdapterContext context);

    public static class PrimitivePersistentDataType<T> implements PersistentDataType<T, T> {

        private final Class<T> primitive;

        PrimitivePersistentDataType(@NotNull Class<T> primitive) {
            this.primitive = primitive;
        }

        @NotNull
        public Class<T> getPrimitiveType() {
            return primitive;
        }

        @NotNull
        public Class<T> getComplexType() {
            return primitive;
        }

        @NotNull
        public T toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext context) {
            return complex;
        }

        @NotNull
        public T fromPrimitive(@NotNull T primitive, @NotNull PersistentDataAdapterContext context) {
            return primitive;
        }
    }
}
