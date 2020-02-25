// 
// Decompiled by Procyon v0.5.36
// 

package org.spiget;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.spiget.comparator.VersionComparator;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("UnusedReturnValue")
public abstract class SpigetUpdateAbstract {
    public static final String RESOURCE_INFO = "http://api.spiget.org/v2/resources/%s?ut=%s";
    public static final String RESOURCE_VERSION = "http://api.spiget.org/v2/resources/%s/versions/latest?ut=%s";
    protected final int resourceId;
    protected final String currentVersion;
    protected final Logger log;
    protected String userAgent;
    protected VersionComparator versionComparator;
    protected ResourceInfo latestResourceInfo;

    public SpigetUpdateAbstract(final int resourceId, final String currentVersion, final Logger log) {
        this.userAgent = "SpigetResourceUpdater";
        this.versionComparator = VersionComparator.EQUAL;
        this.resourceId = resourceId;
        this.currentVersion = currentVersion;
        this.log = log;
    }

    public SpigetUpdateAbstract setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public SpigetUpdateAbstract setVersionComparator(final VersionComparator comparator) {
        this.versionComparator = comparator;
        return this;
    }

    public ResourceInfo getLatestResourceInfo() {
        return this.latestResourceInfo;
    }

    protected abstract void dispatch(final Runnable p0);

    public boolean isVersionNewer(final String oldVersion, final String newVersion) {
        return this.versionComparator.isNewer(oldVersion, newVersion);
    }

    public void checkForUpdate(final UpdateCallback callback) {
        this.dispatch(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(String.format("http://api.spiget" +
                        ".org/v2/resources/%s?ut=%s", SpigetUpdateAbstract.this.resourceId,
                        System.currentTimeMillis())).openConnection();
                connection.setRequestProperty("User-Agent", SpigetUpdateAbstract.this.getUserAgent());
                JsonObject jsonObject =
                        new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                SpigetUpdateAbstract.this.latestResourceInfo = new Gson().fromJson(jsonObject,
                        ResourceInfo.class);
                connection = (HttpURLConnection) new URL(String.format("http://api.spiget" +
                        ".org/v2/resources/%s/versions/latest?ut=%s", SpigetUpdateAbstract.this.resourceId,
                        System.currentTimeMillis())).openConnection();
                connection.setRequestProperty("User-Agent", SpigetUpdateAbstract.this.getUserAgent());
                jsonObject =
                        new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                SpigetUpdateAbstract.this.latestResourceInfo.latestVersion = new Gson().fromJson(jsonObject,
                        ResourceVersion.class);
                if (SpigetUpdateAbstract.this.isVersionNewer(SpigetUpdateAbstract.this.currentVersion,
                        SpigetUpdateAbstract.this.latestResourceInfo.latestVersion.name)) {
                    callback.updateAvailable(SpigetUpdateAbstract.this.latestResourceInfo.latestVersion.name
                            ,
                            "https://spigotmc.org/" + SpigetUpdateAbstract.this.latestResourceInfo.file.url,
                            !SpigetUpdateAbstract.this.latestResourceInfo.external);
                } else {
                    callback.upToDate();
                }
            } catch (Exception e) {
                SpigetUpdateAbstract.this.log.log(Level.WARNING, "Failed to get resource info from spiget" +
                        ".org", e);
            }
        });
    }
}
