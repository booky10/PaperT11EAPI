package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (22:58 10.07.20)

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public class NamespacedKey {

    public static final String MINECRAFT = "minecraft", BUKKIT = "bukkit";
    private static final Pattern VALID_NAMESPACE = Pattern.compile("[a-z0-9._-]+"),
            VALID_KEY = Pattern.compile("[a-z0-9/._-]+");
    private final String namespace, key;

    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public NamespacedKey(@NotNull String namespace, @NotNull String key) {
        Preconditions.checkArgument(namespace != null && VALID_NAMESPACE.matcher(namespace).matches(), "Invalid namespace. Must be [a-z0-9._-]: %s", namespace);
        Preconditions.checkArgument(key != null && VALID_KEY.matcher(key).matches(), "Invalid key. Must be [a-z0-9/._-]: %s", key);
        this.namespace = namespace;
        this.key = key;
        String string = toString();
        Preconditions.checkArgument(string.length() < 256, "NamespacedKey must be less than 256 characters", string);
    }

    public NamespacedKey(@NotNull Plugin plugin, @NotNull String key) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        Preconditions.checkArgument(key != null, "Key cannot be null");
        this.namespace = plugin.getName().toLowerCase(Locale.ROOT);
        this.key = key.toLowerCase(Locale.ROOT);
        Preconditions.checkArgument(VALID_NAMESPACE.matcher(namespace).matches(), "Invalid namespace. Must be [a-z0-9._-]: %s", this.namespace);
        Preconditions.checkArgument(VALID_KEY.matcher(key).matches(), "Invalid key. Must be [a-z0-9/._-]: %s", this.key);
        String string = toString();
        Preconditions.checkArgument(string.length() < 256, "NamespacedKey must be less than 256 characters (%s)", string);
    }

    @NotNull
    public String getNamespace() {
        return namespace;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        else if (getClass() != object.getClass())
            return false;
        else {
            NamespacedKey other = (NamespacedKey) object;
            return namespace.equals(other.namespace) && key.equals(other.key);
        }
    }

    @Override
    public String toString() {
        return namespace + ":" + key;
    }

    @Deprecated
    @NotNull
    public static NamespacedKey randomKey() {
        return new NamespacedKey("bukkit", UUID.randomUUID().toString());
    }

    @NotNull
    public static NamespacedKey minecraft(@NotNull String key) {
        return new NamespacedKey("minecraft", key);
    }
}