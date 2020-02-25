// 
// Decompiled by Procyon v0.5.36
// 

package org.spiget.download;

import org.spiget.ResourceInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@SuppressWarnings("InfiniteRecursion")
public class UpdateDownloader {
    public static final String RESOURCE_DOWNLOAD = "http://api.spiget.org/v2/resources/%s/download";

    public static Runnable downloadAsync(final ResourceInfo info, final File file, final String userAgent,
                                         final DownloadCallback callback) {
        return () -> {
            try {
                UpdateDownloader.download(info, file, userAgent);
                callback.finished();
            } catch (Exception e) {
                callback.error(e);
            }
        };
    }

    public static void download(final ResourceInfo info, final File file) {
        download(info, file);
    }

    public static void download(final ResourceInfo info, final File file, final String userAgent) {
        if (info.external) {
            throw new IllegalArgumentException("Cannot download external resource #" + info.id);
        }
        ReadableByteChannel channel;
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(String.format("http://api.spiget" +
                    ".org/v2/resources/%s/download", info.id)).openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Download returned status #" + connection.getResponseCode());
            }
            channel = Channels.newChannel(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Download failed", e);
        }
        try {
            final FileOutputStream output = new FileOutputStream(file);
            output.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
            output.flush();
            output.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not save file", e);
        }
    }
}
