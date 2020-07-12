package tk.t11e.api.util.persistence.implementations;
// Created by booky10 in PaperT11EAPI (22:19 10.07.20)

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import tk.t11e.api.util.nbt.NBTTag;
import tk.t11e.api.util.persistence.interfaces.PersistentDataAdapterContext;
import tk.t11e.api.util.persistence.interfaces.PersistentDataContainer;
import tk.t11e.api.util.persistence.interfaces.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
@Deprecated
public class PaperPersistentDataContainer implements PersistentDataContainer {

    private final Map<String, NBTTag> dataTags = new HashMap<>();
    private final PaperPersistentDataTypeRegistry registry;
    private final PaperPersistentDataAdapterContext adapterContext;

    public PaperPersistentDataContainer(Map<String, NBTTag> tags, PaperPersistentDataTypeRegistry registry) {
        this(registry);
        dataTags.putAll(tags);
    }

    public PaperPersistentDataContainer(PaperPersistentDataTypeRegistry registry) {
        this.registry = registry;
        adapterContext = new PaperPersistentDataAdapterContext(registry);
    }

    @Override
    public <T, Z> void set(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        Validate.notNull(key, "Key is null!");
        Validate.notNull(type, "Type is null!");
        Validate.notNull(value, "Value is null!");

        //dataTags.put(key.toString(), registry.wrap(type.getPrimitiveType(), type.toPrimitive(value, adapterContext)));
    }

    @Override
    public <T, Z> boolean has(NamespacedKey key, PersistentDataType<T, Z> type) {
        Validate.notNull(key, "Key is null!");
        Validate.notNull(type, "Type is null!");

        NBTTag value = dataTags.get(key.toString());
        if (value == null)
            return false;
        else
            return false;
            //return registry.isInstanceOf(type.getPrimitiveType(), value);
    }

    @Override
    public <T, Z> Z get(NamespacedKey key, PersistentDataType<T, Z> type) {
        Validate.notNull(key, "Key is null!");
        Validate.notNull(type, "Type is null!");

        NBTTag value = dataTags.get(key.toString());
        if (value == null)
            return null;
        else
            return null;
            //return type.fromPrimitive(registry.extract(type.getPrimitiveType(), value), adapterContext);
    }

    @Override
    public <T, Z> Z getOrDefault(NamespacedKey key, PersistentDataType<T, Z> type, Z defaultValue) {
        Z value = get(key, type);
        return value != null ? value : defaultValue;
    }

    public Set<NamespacedKey> getKeys() {
        Set<NamespacedKey> keys = new HashSet<>();

       dataTags.keySet().forEach(key -> {
            String[] keyData = key.split(":", 2);
            if (keyData.length == 2) {
                keys.add(new NamespacedKey(keyData[0], keyData[1]));
            }
        });

        return keys;
    }

    @Override
    public void remove(NamespacedKey key) {
        Validate.notNull(key, "Key is null!");

        dataTags.remove(key.toString());
    }

    @Override
    public boolean isEmpty() {
        return dataTags.isEmpty();
    }

    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        return adapterContext;
    }
}