package tk.t11e.api.util.persistence.interfaces;

import com.sun.istack.internal.NotNull;

@Deprecated
public interface PersistentDataHolder {

    @NotNull
    PersistentDataContainer getPersistentDataContainer();
}
