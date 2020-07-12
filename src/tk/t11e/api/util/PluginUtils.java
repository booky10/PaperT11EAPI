package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (21:56 20.04.20)

import com.rylinaux.plugman.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginUtils {

    public static void reloadPlugin(String plugin) {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        reloadPlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void reloadPlugin(Plugin plugin) {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        PluginUtil.reload(plugin);
    }

    public static void reloadPlugins() {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        PluginUtil.reloadAll();
    }

    public static void enablePlugin(String plugin) {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        enablePlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void enablePlugin(Plugin plugin) {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        PluginUtil.enable(plugin);
    }

    public static void enablePlugins() {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        PluginUtil.enableAll();
    }

    public static void disablePlugin(String plugin) {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        disablePlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void disablePlugin(Plugin plugin) {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        PluginUtil.disable(plugin);
    }

    public static void disablePlugins() {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        PluginUtil.disableAll();
    }

    public static void loadPlugin(String plugin) {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        PluginUtil.load(plugin);
    }

    public static void unloadPlugin(String plugin) {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        unloadPlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void unloadPlugin(Plugin plugin) {
        PluginNotLoadedException.checkAndThrow("PlugMan");
        PluginUtil.unload(plugin);
    }
}