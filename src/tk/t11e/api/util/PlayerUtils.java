package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (22:39 22.02.20)

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerUtils {

    public static List<String> getOnlinePlayerNames() {
        List<String> names=new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            names.add(player.getName());
        return names;
    }

    public static List<String> convertTab(String[] args, List<String> completions) {
        List<String> list = new ArrayList<>();

        if (args.length < 1)
            return Collections.emptyList();
        String word = args[args.length - 1];
        if (word.equalsIgnoreCase(""))
            return completions;

        for (String entry : completions)
            if (entry.startsWith(word)&& !entry.equals(word))
                list.add(entry);

        return list;
    }
}