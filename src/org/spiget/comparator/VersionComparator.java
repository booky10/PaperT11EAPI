// 
// Decompiled by Procyon v0.5.36
// 

package org.spiget.comparator;

public abstract class VersionComparator {
    public static final VersionComparator EQUAL;
    public static final VersionComparator SEM_VER;

    public abstract boolean isNewer(final String p0, final String p1);

    static {
        EQUAL = new VersionComparator() {
            @Override
            public boolean isNewer(final String currentVersion, final String checkVersion) {
                return !currentVersion.equals(checkVersion);
            }
        };
        SEM_VER = new VersionComparator() {
            @Override
            public boolean isNewer(String currentVersion, String checkVersion) {
                currentVersion = currentVersion.replace(".", "");
                checkVersion = checkVersion.replace(".", "");
                try {
                    final int current = Integer.parseInt(currentVersion);
                    final int check = Integer.parseInt(checkVersion);
                    return check > current;
                } catch (NumberFormatException e) {
                    System.err.println("[SpigetUpdate] Invalid SemVer versions specified [" + currentVersion +
                            "] [" + checkVersion + "]");
                    return false;
                }
            }
        };
    }
}