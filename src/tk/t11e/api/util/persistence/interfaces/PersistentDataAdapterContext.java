package tk.t11e.api.util.persistence.interfaces;

import com.sun.istack.internal.NotNull;

@Deprecated
@SuppressWarnings("ALL")
public interface PersistentDataAdapterContext {

    @NotNull
    PersistentDataContainer newPersistentDataContainer();
}
