// 
// Decompiled by Procyon v0.5.36
// 

package org.spiget.download;

public interface DownloadCallback {
    void finished();

    void error(final Exception p0);
}
