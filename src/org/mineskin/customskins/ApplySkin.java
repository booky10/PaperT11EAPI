package org.mineskin.customskins;
// Created by booky10 in PaperT11EAPI (16:13 24.02.20)

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.inventivetalent.nicknamer.api.NickNamerAPI;
import tk.t11e.api.commands.CommandExecutor;
import tk.t11e.api.main.Main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class ApplySkin extends CommandExecutor {


    public ApplySkin() {
        super(Main.main, "applyskin", "/applyskin <Name> [Player]", "customskins.apply",
                Receiver.ALL, "applycustomskin", "setcustomskin");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, Integer length) {
        if(args.length==2){
            Player target;
            if (args[1] != null) {
                target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    sender.sendMessage("Player not found!");
                    return;
                }
            } else {
                help(sender);
                return;
            }

            File skinFile = new File(CustomSkins.skinFolder, args[0] + ".json");
            if (!skinFile.exists()) {
                sender.sendMessage("Skin \"" + args[0] + "\" does not exist");
                    sender.sendMessage("Please use \"/createcustomskin\" first!");
                return;
            }
            JsonObject skinData;
            try {
                skinData = new JsonParser().parse(new FileReader(skinFile)).getAsJsonObject();
            } catch (IOException e) {
                sender.sendMessage("Failed to load skin from file: " + e.getMessage());
                Main.main.getLogger().log(Level.SEVERE, "Failed to load skin", e);
                return;
            }

            if (!CustomSkins.loadedSkins.contains(args[0])) {
                NickNamerAPI.getNickManager().loadCustomSkin("cs_" + args[0], skinData);
                CustomSkins.loadedSkins.add(args[0]);
            }

            NickNamerAPI.getNickManager().setCustomSkin(target.getUniqueId(), "cs_" + args[0]);
            sender.sendMessage("Custom skin changed to " + args[0]);
        }else
            help(sender);
    }

    @Override
    public void onPlayerExecute(Player player, String[] args, Integer length) {
        if(args.length>=1&&args.length<=2){
            Player target;
            if (args.length==2) {
                if (!player.hasPermission("customskins.apply.other")) {
                    player.sendMessage(Main.NO_PERMISSION);
                    return;
                }
                target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage(Main.PREFIX+"Player not found!");
                    return;
                }
            } else
                target=player;

            File skinFile = new File(CustomSkins.skinFolder, args[0] + ".json");
            if (!skinFile.exists()) {
                player.sendMessage(Main.PREFIX+"Skin \"" + args[0] + "\" does not exist");
                if (player.hasPermission("customskins.create"))
                    player.sendMessage(Main.PREFIX+"Please use \"/createcustomskin\" first!");
                return;
            }
            JsonObject skinData;
            try {
                skinData = new JsonParser().parse(new FileReader(skinFile)).getAsJsonObject();
            } catch (IOException e) {
                player.sendMessage(Main.PREFIX+"Failed to load skin from file: " + e.getMessage());
                Main.main.getLogger().log(Level.SEVERE, "Failed to load skin", e);
                return;
            }

            if (!CustomSkins.loadedSkins.contains(args[0])) {
                NickNamerAPI.getNickManager().loadCustomSkin("cs_" + args[0], skinData);
                CustomSkins.loadedSkins.add(args[0]);
            }

            NickNamerAPI.getNickManager().setCustomSkin(target.getUniqueId(), "cs_" + args[0]);
            player.sendMessage(Main.PREFIX+"§aCustom skin changed to " + args[0]);
        }else
            help(player);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args, Integer length) {
        ArrayList<String> list = new ArrayList<>();
        if (args[0] == null || args[0].isEmpty() || !new File(CustomSkins.skinFolder,
                args[0] + ".json").exists())
            for (String s : Objects.requireNonNull(CustomSkins.skinFolder.list()))
                if (s.endsWith(".json"))
                    list.add(s.substring(0, s.length() - 5));
                else
                    list.addAll(getOnlinePlayerNames());
        return list;
    }
}