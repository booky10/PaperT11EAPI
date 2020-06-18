package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (16:10 14.05.20)

import org.bukkit.Bukkit;
import tk.t11e.api.main.PaperT11EAPIMain;

public class VersionHelper {

    public static Version getVersion() {
        switch (Bukkit.getBukkitVersion().split("\\.")[1]) {
            case "15":
                return Version.V1_15;
            case "14":
                return Version.V1_14;
            case "13":
                return Version.V1_13;
            case "12":
                return Version.V1_12;
            case "11":
                return Version.V1_11;
            case "10":
                return Version.V1_10;
            case "9":
                return Version.V1_9;
            case "8":
                return Version.V1_8;
            case "7":
                return Version.V1_7;
            default:
                return Version.UNKOWN;
        }
    }

    public static String getNMSVersion() {
        return getVersion().nmsVersion;
    }

    public static Boolean is17() {
        return getVersion().equals(Version.V1_7);
    }

    public static Boolean is18() {
        return getVersion().equals(Version.V1_8);
    }

    public static Boolean is19() {
        return getVersion().equals(Version.V1_9);
    }

    public static Boolean is110() {
        return getVersion().equals(Version.V1_10);
    }

    public static Boolean is111() {
        return getVersion().equals(Version.V1_11);
    }

    public static Boolean is112() {
        return getVersion().equals(Version.V1_12);
    }

    public static Boolean is113() {
        return getVersion().equals(Version.V1_13);
    }

    public static Boolean is114() {
        return getVersion().equals(Version.V1_14);
    }

    public static Boolean is115() {
        return getVersion().equals(Version.V1_15);
    }

    public static Boolean aboveOr17() {
        return aboveOr18() || is17();
    }

    public static Boolean aboveOr18() {
        return aboveOr19() || is18();
    }

    public static Boolean aboveOr19() {
        return aboveOr110() || is19();
    }

    public static Boolean aboveOr110() {
        return aboveOr111() || is110();
    }

    public static Boolean aboveOr111() {
        return aboveOr112() || is111();
    }

    public static Boolean aboveOr112() {
        return aboveOr113() || is112();
    }

    public static Boolean aboveOr113() {
        return aboveOr114() || is113();
    }

    public static Boolean aboveOr114() {
        return aboveOr115() || is114();
    }

    public static Boolean aboveOr115() {
        return is115();
    }

    public static Boolean aboveOr(Version version) {
        return getVersion().number >= version.number;
    }

    public static Boolean is(Version version) {
        return getVersion().number.equals(version.number);
    }

    public static Boolean belowOr(Version version){
        return getVersion().number<version.number;
    }

    public static Boolean isPaper() {
        return Bukkit.getVersion().toLowerCase().contains("paper");
    }

    public static Boolean isUnknown() {
        return getVersion() == Version.UNKOWN;
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getNMSVersion() + "." + name);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            return PaperT11EAPIMain.main.getClass();
        }
    }

    public static Class<?> getOBCClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getNMSVersion() + "." + name);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            return PaperT11EAPIMain.main.getClass();
        }
    }

    public enum Version {

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