// 
// Decompiled by Procyon v0.5.36
// 

package org.spiget;


import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.spiget.comparator.VersionComparator;
import org.spiget.download.DownloadCallback;
import org.spiget.download.UpdateDownloader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;

@SuppressWarnings("RedundantArrayCreation")
public class SpigetUpdate extends SpigetUpdateAbstract {
    protected final Plugin plugin;
    protected DownloadFailReason failReason;

    public SpigetUpdate(final Plugin plugin, final int resourceId) {
        super(resourceId, plugin.getDescription().getVersion(), plugin.getLogger());
        this.failReason = DownloadFailReason.UNKNOWN;
        this.plugin = plugin;
        this.setUserAgent("SpigetResourceUpdater/Bukkit");
    }

    @Override
    public SpigetUpdate setUserAgent(final String userAgent) {
        super.setUserAgent(userAgent);
        return this;
    }

    @Override
    public SpigetUpdate setVersionComparator(final VersionComparator comparator) {
        super.setVersionComparator(comparator);
        return this;
    }

    @Override
    protected void dispatch(final Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    public boolean downloadUpdate() {
        if (this.latestResourceInfo == null) {
            this.failReason = DownloadFailReason.NOT_CHECKED;
            return false;
        }
        if (!this.isVersionNewer(this.currentVersion, this.latestResourceInfo.latestVersion.name)) {
            this.failReason = DownloadFailReason.NO_UPDATE;
            return false;
        }
        if (this.latestResourceInfo.external) {
            this.failReason = DownloadFailReason.NO_DOWNLOAD;
            return false;
        }
        final File pluginFile = this.getPluginFile();
        if (pluginFile == null) {
            this.failReason = DownloadFailReason.NO_PLUGIN_FILE;
            return false;
        }
        final File updateFolder = Bukkit.getUpdateFolderFile();
        if (!updateFolder.exists() && !updateFolder.mkdirs()) {
            this.failReason = DownloadFailReason.NO_UPDATE_FOLDER;
            return false;
        }
        final File updateFile = new File(updateFolder, pluginFile.getName());
        final Properties properties = this.getUpdaterProperties();
        final boolean allowExternalDownload =
                properties != null && properties.containsKey("externalDownloads") && Boolean.parseBoolean(properties.getProperty("externalDownloads"));
        if (!allowExternalDownload && this.latestResourceInfo.external) {
            this.failReason = DownloadFailReason.EXTERNAL_DISALLOWED;
            return false;
        }
        this.log.info("[SpigetUpdate] Downloading update...");
        this.dispatch(UpdateDownloader.downloadAsync(this.latestResourceInfo, updateFile, this.getUserAgent(),
                new DownloadCallback() {
            @Override
            public void finished() {
                SpigetUpdate.this.log.info("[SpigetUpdate] Update saved as " + updateFile.getPath());
            }

            @Override
            public void error(final Exception exception) {
                SpigetUpdate.this.log.log(Level.WARNING, "[SpigetUpdate] Could not download update", exception);
            }
        }));
        return true;
    }

    public DownloadFailReason getFailReason() {
        return this.failReason;
    }

    public Properties getUpdaterProperties() {
        final File file = new File(Bukkit.getUpdateFolderFile(), "spiget.properties");
        final Properties properties = new Properties();
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return null;
                }
                properties.setProperty("externalDownloads", "false");
                properties.store(new FileWriter(file), "Configuration for the Spiget auto-updater. " +
                        "https://spiget.org | https://github.com/InventivetalentDev/SpigetUpdater\nUse " +
                        "'externalDownloads' if you want to auto-download resources hosted on external sites\n");
            } catch (Exception ignored) {
                return null;
            }
        }
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            return null;
        }
        return properties;
    }

    private File getPluginFile() {
        if (!(this.plugin instanceof JavaPlugin)) {
            return null;
        }
        try {
            final Method method = JavaPlugin.class.getDeclaredMethod("getFile", new Class[0]);
            method.setAccessible(true);
            return (File) method.invoke(this.plugin, new Object[0]);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not get plugin file", e);
        }
    }

    public enum DownloadFailReason {
        NOT_CHECKED,
        NO_UPDATE,
        NO_DOWNLOAD,
        NO_PLUGIN_FILE,
        NO_UPDATE_FOLDER,
        EXTERNAL_DISALLOWED,
        UNKNOWN
    }
}
