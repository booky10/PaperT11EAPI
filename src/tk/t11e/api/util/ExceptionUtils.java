package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (22:06 22.02.20)

import org.bukkit.Bukkit;

public class ExceptionUtils {

    public static void print(Exception exception) {
        Bukkit.getLogger().severe(exception.toString());
    }
}