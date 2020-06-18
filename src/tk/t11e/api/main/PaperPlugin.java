package tk.t11e.api.main;
// Created by booky10 in PaperT11EAPI (10:11 17.06.20)

import org.bukkit.plugin.java.JavaPlugin;
import tk.t11e.api.util.OtherUtils;
import tk.t11e.api.util.VersionHelper;

import javax.annotation.CheckReturnValue;
import java.util.List;

public abstract class PaperPlugin extends JavaPlugin {

    @Override
    public final void onLoad() {
        getLogger().info("Loading " + getPluginName() + "...");
        preLoad();
        postLoad();
        getLogger().info("Loaded " + getPluginName() + "!");
    }

    @CheckReturnValue
    protected String getFormattedPrefix() {
        return getFormattedPrefix(getPluginName());
    }

    @CheckReturnValue
    protected String getFormattedPrefix(String prefix) {
        return "§7[§b" + prefix + "§7]§c ";
    }

    @SuppressWarnings("EmptyMethod")
    public void preLoad() {
    }

    @SuppressWarnings("EmptyMethod")
    public void postLoad() {
    }

    @Override
    public final void onEnable() {
        getLogger().info("Enabling " + getPluginName() + "...");
        preEnable();
        postEnable();
        getLogger().info("Enabled " + getPluginName() + "!");
    }

    @SuppressWarnings("EmptyMethod")
    public void preEnable() {
    }

    @SuppressWarnings("EmptyMethod")
    public void postEnable() {
    }

    @Override
    public final void onDisable() {
        getLogger().info("Disabling " + getPluginName() + "...");
        preDisable();
        postDisable();
        getLogger().info("Disabled " + getPluginName() + "!");
    }

    @SuppressWarnings("EmptyMethod")
    public void preDisable() {
    }

    @SuppressWarnings("EmptyMethod")
    public void postDisable() {
    }

    @CheckReturnValue
    public String getPluginName() {
        return getDescription().getName();
    }

    @CheckReturnValue
    public String getVersion() {
        return getDescription().getVersion();
    }

    @CheckReturnValue
    public List<String> getAuthors() {
        return getDescription().getAuthors();
    }

    @CheckReturnValue
    public String getAuthor() {
        return getDescription().getAuthors().get(0);
    }

    protected Boolean supportsVersion(VersionHelper.Version from, VersionHelper.Version to, Boolean print) {
        boolean supported = VersionHelper.aboveOr(from) && VersionHelper.belowOr(to);
        if (print && !supported)
            getLogger().severe(OtherUtils.generateBox("Error! You can only use",
                    getPluginName() + " v" + getVersion(),
                    "from " + from.getName() + " to " + to.getName() + "!"));
        else if (print)
            getLogger().info(OtherUtils.generateBox(getPluginName() + " v" + getVersion(),
                    "has been loaded and enabled!"));
        return supported;
    }
}