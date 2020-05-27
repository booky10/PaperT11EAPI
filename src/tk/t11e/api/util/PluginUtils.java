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
        if (VersionHelper.aboveOr110())
            PluginUtil.reload(plugin);
        else
            throw new IllegalStateException("Tried to use PlugMan while in version " + VersionHelper.getVersion());
    }

    public static void reloadPlugins() {
        if (VersionHelper.aboveOr110())
            PluginUtil.reloadAll();
        else
            throw new IllegalStateException("Tried to use PlugMan while in version " + VersionHelper.getVersion());
    }

    public static void enablePlugin(String plugin) {
        enablePlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void enablePlugin(Plugin plugin) {
        if (VersionHelper.aboveOr110())
            PluginUtil.enable(plugin);
        else
            throw new IllegalStateException("Tried to use PlugMan while in version " + VersionHelper.getVersion());
    }

    public static void enablePlugins() {
        if (VersionHelper.aboveOr110())
            PluginUtil.enableAll();
        else
            throw new IllegalStateException("Tried to use PlugMan while in version " + VersionHelper.getVersion());
    }

    public static void disablePlugin(String plugin) {
        disablePlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void disablePlugin(Plugin plugin) {
        if (VersionHelper.aboveOr110())
            PluginUtil.disable(plugin);
        else
            throw new IllegalStateException("Tried to use PlugMan while in version " + VersionHelper.getVersion());
    }

    public static void disablePlugins() {
        if (VersionHelper.aboveOr110())
            PluginUtil.disableAll();
        else
            throw new IllegalStateException("Tried to use PlugMan while in version " + VersionHelper.getVersion());
    }

    public static void loadPlugin(String plugin) {
        if (VersionHelper.aboveOr110())
            PluginUtil.load(plugin);
        else
            throw new IllegalStateException("Tried to use PlugMan while in version " + VersionHelper.getVersion());
    }

    public static void unloadPlugin(String plugin) {
        unloadPlugin(Bukkit.getPluginManager().getPlugin(plugin));
    }

    public static void unloadPlugin(Plugin plugin) {
        if (VersionHelper.aboveOr110())
            PluginUtil.unload(plugin);
        else
            throw new IllegalStateException("Tried to use PlugMan while in version " + VersionHelper.getVersion());
    }

}