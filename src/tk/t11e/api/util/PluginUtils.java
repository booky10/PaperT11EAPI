package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (21:56 20.04.20)

import com.rylinaux.plugman.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginUtils {

    public static void reloadPlugin(String plugin) {
        reloadPlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void reloadPlugin(Plugin plugin) {
        PluginUtil.reload(plugin);
    }

    public static void reloadPlugins() {
        PluginUtil.reloadAll();
    }

    public static void enablePlugin(String plugin) {
        enablePlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void enablePlugin(Plugin plugin) {
        PluginUtil.enable(plugin);
    }

    public static void enablePlugins() {
        PluginUtil.enableAll();
    }

    public static void disablePlugin(String plugin) {
        disablePlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void disablePlugin(Plugin plugin) {
        PluginUtil.disable(plugin);
    }

    public static void disablePlugins() {
        PluginUtil.disableAll();
    }

    public static void loadPlugin(String plugin) {
        PluginUtil.load(plugin);
    }

    public static void unloadPlugin(String plugin) {
        unloadPlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void unloadPlugin(Plugin plugin) {
        PluginUtil.unload(plugin);
    }

}