package tk.t11e.api.util.persistence.interfaces;

import com.sun.istack.internal.NotNull;
import org.bukkit.NamespacedKey;

import javax.annotation.Nullable;

@Deprecated
@SuppressWarnings("ALL")
public interface PersistentDataContainer {

    <T, Z> void set(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z value);

    <T, Z> boolean has(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type);

    @Nullable
    <T, Z> Z get(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type);

    @NotNull
    <T, Z> Z getOrDefault(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue);

    void remove(@NotNull NamespacedKey key);

    boolean isEmpty();

    @NotNull
    PersistentDataAdapterContext getAdapterContext();
}
