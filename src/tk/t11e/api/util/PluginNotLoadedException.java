package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (14:11 06.07.20)

import org.bukkit.Bukkit;

public class PluginNotLoadedException extends IllegalStateException {

    public PluginNotLoadedException() {
        super();
    }

    public PluginNotLoadedException(String plugin) {
        super("The Plugin \"" + plugin + "\" is not loaded or enabled! Please check it!");
    }

    public PluginNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginNotLoadedException(Throwable cause) {
        super(cause);
    }

    static final long serialVersionUID = -5457363625743296L;

    public static void checkAndThrow(String plugin) {
        if (plugin == null || plugin.isEmpty() || !Bukkit.getPluginManager().isPluginEnabled(plugin))
            throw new PluginNotLoadedException(plugin);
    }
}