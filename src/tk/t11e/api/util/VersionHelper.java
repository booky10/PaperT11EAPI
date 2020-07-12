package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (16:10 14.05.20)

import org.bukkit.Bukkit;

public class VersionHelper {

    private static final Version version;
    private static final Boolean paper = Bukkit.getVersion().toLowerCase().contains("paper");

    static {
        switch (Bukkit.getBukkitVersion().split("\\.")[1]) {
            case "16":
                version = Version.V1_16;
                break;
            case "15":
                version = Version.V1_15;
                break;
            case "14":
                version = Version.V1_14;
                break;
            case "13":
                version = Version.V1_13;
                break;
            case "12":
                version = Version.V1_12;
                break;
            case "11":
                version = Version.V1_11;
                break;
            case "10":
                version = Version.V1_10;
                break;
            case "9":
                version = Version.V1_9;
                break;
            case "8":
                version = Version.V1_8;
                break;
            case "7":
                version = Version.V1_7;
                break;
            default:
                version = Version.UNKOWN;
                break;
        }
    }

    public static Version getVersion() {
        return version;
    }

    public static String getNMSVersion() {
        return version.nmsVersion;
    }

    public static Boolean is17() {
        return is(Version.V1_7);
    }

    public static Boolean is18() {
        return is(Version.V1_8);
    }

    public static Boolean is19() {
        return is(Version.V1_9);
    }

    public static Boolean is110() {
        return is(Version.V1_10);
    }

    public static Boolean is111() {
        return is(Version.V1_11);
    }

    public static Boolean is112() {
        return is(Version.V1_12);
    }

    public static Boolean is113() {
        return is(Version.V1_13);
    }

    public static Boolean is114() {
        return is(Version.V1_14);
    }

    public static Boolean is115() {
        return is(Version.V1_15);
    }

    public static Boolean is116() {
        return is(Version.V1_16);
    }

    public static Boolean aboveOr17() {
        return aboveOr(Version.V1_7);
    }

    public static Boolean aboveOr18() {
        return aboveOr(Version.V1_8);
    }

    public static Boolean aboveOr19() {
        return aboveOr(Version.V1_9);
    }

    public static Boolean aboveOr110() {
        return aboveOr(Version.V1_10);
    }

    public static Boolean aboveOr111() {
        return aboveOr(Version.V1_11);
    }

    public static Boolean aboveOr112() {
        return aboveOr(Version.V1_12);
    }

    public static Boolean aboveOr113() {
        return aboveOr(Version.V1_13);
    }

    public static Boolean aboveOr114() {
        return aboveOr(Version.V1_14);
    }

    public static Boolean aboveOr115() {
        return aboveOr(Version.V1_15);
    }

    public static Boolean aboveOr116() {
        return aboveOr(Version.V1_16);
    }

    public static Boolean belowOr17() {
        return belowOr(Version.V1_7);
    }

    public static Boolean belowOr18() {
        return belowOr(Version.V1_8);
    }

    public static Boolean belowOr19() {
        return belowOr(Version.V1_9);
    }

    public static Boolean belowOr110() {
        return belowOr(Version.V1_10);
    }

    public static Boolean belowOr111() {
        return belowOr(Version.V1_11);
    }

    public static Boolean belowOr112() {
        return belowOr(Version.V1_12);
    }

    public static Boolean belowOr113() {
        return belowOr(Version.V1_13);
    }

    public static Boolean belowOr114() {
        return belowOr(Version.V1_14);
    }

    public static Boolean belowOr115() {
        return belowOr(Version.V1_15);
    }

    public static Boolean belowOr116() {
        return belowOr(Version.V1_16);
    }

    public static Boolean aboveOr(Version version) {
        return VersionHelper.version.number >= version.number;
    }

    public static Boolean is(Version version) {
        return VersionHelper.version.number.equals(version.number);
    }

    public static Boolean belowOr(Version version) {
        return VersionHelper.version.number <= version.number;
    }

    public static Boolean isPaper() {
        return paper;
    }

    public static Boolean isUnknown() {
        return version == Version.UNKOWN;
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getNMSVersion() + "." + name);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public static Class<?> getOBCClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getNMSVersion() + "." + name);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public enum Version {

        V1_16("v1_16_R1", 11, "1.16"),
        V1_15("v1_15_R1", 10, "1.15"),
        V1_14("v1_14_R1", 9, "1.14"),
        V1_13("v1_13_R2", 8, "1.13"),
        V1_12("v1_12_R1", 7, "1.12"),
        V1_11("v1_11_R1", 6, "1.11"),
        V1_10("v1_10_R1", 5, "1.10"),
        V1_9("v1_9_R2", 4, "1.9"),
        V1_8("v1_8_R3", 3, "1.8"),
        V1_7("v1_7_R4", 2, "1.7"),
        UNKOWN("UNKNOWN", 1, "Unknown");

        private final String nmsVersion;
        private final Integer number;
        private final String name;

        Version(String nmsVersion, Integer number, String name) {
            this.nmsVersion = nmsVersion;
            this.number = number;
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}