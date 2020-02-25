package org.mineskin.customskins;
// Created by booky10 in PaperT11EAPI (15:49 24.02.20)

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineskin.SkinOptions;
import org.mineskin.data.Skin;
import org.mineskin.data.SkinCallback;
import tk.t11e.api.commands.CommandExecutor;
import tk.t11e.api.main.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "UnusedAssignment"})
public class CreateSkin extends CommandExecutor {


    public CreateSkin() {
        super(Main.main, "createcustomskin", "/createcustomskin <Name> <URL> [private]",
                "customskins.create", Receiver.ALL,"createskin");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, Integer length) {
        try {
            if (args.length >= 2 && args.length <= 3) {
                URL url = new URL(args[0]);
                final File skinFile = new File(CustomSkins.skinFolder, args[0] + ".json");
                boolean privateUpload = false;
                if (args.length == 3 && (args[2].equalsIgnoreCase("true")
                        || args[2].equalsIgnoreCase("yes")
                        || args[2].equalsIgnoreCase("private")))
                    privateUpload = true;

                if (skinFile.exists()) {
                    sender.sendMessage("Custom skin '" + args[0] + "' already exists. Please " +
                            "choose a different name.");
                    return;
                } else {
                    skinFile.createNewFile();
                }


                CustomSkins.skinClient.generateUrl(url.toString(), SkinOptions.name(args[0]), new SkinCallback() {

                    @Override
                    public void waiting(long l) {
                        sender.sendMessage("Waiting " + (l / 1000D) + "s to upload skin...");
                    }

                    @Override
                    public void uploading() {
                        sender.sendMessage("Uploading skin...");
                    }

                    @Override
                    public void error(String s) {
                        sender.sendMessage("Error while generating skin: " + s);
                        sender.sendMessage("Please make sure the image is a valid skin texture and try again.");

                        skinFile.delete();
                    }

                    @Override
                    public void exception(Exception exception) {
                        sender.sendMessage("Exception while generating skin, see console for details: " + exception.getMessage());
                        sender.sendMessage("Please make sure the image is a valid skin texture and try again.");

                        skinFile.delete();

                        Main.main.getLogger().log(Level.WARNING, "Exception while generating skin", exception);
                    }

                    @Override
                    public void done(Skin skin) {
                        sender.sendMessage("Skin data generated.");
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", skin.data.uuid.toString());
                        jsonObject.addProperty("name", "");

                        JsonObject property = new JsonObject();
                        property.addProperty("name", "textures");
                        property.addProperty("value", skin.data.texture.value);
                        property.addProperty("signature", skin.data.texture.signature);

                        JsonArray propertiesArray = new JsonArray();
                        propertiesArray.add(property);

                        jsonObject.add("properties", propertiesArray);

                        try (Writer writer = new FileWriter(skinFile)) {
                            new Gson().toJson(jsonObject, writer);
                        } catch (IOException e) {
                            sender.sendMessage("Failed to save skin to file: " + e.getMessage());
                            Main.main.getLogger().log(Level.SEVERE, "Failed to save skin", e);
                        }
                    }
                });
            } else
                help(sender);
        } catch (MalformedURLException e) {
            sender.sendMessage("Invalid URL");
        } catch (IOException e) {
            sender.sendMessage("Unexpected IOException: " + e.getMessage());
            Main.main.getLogger().log(Level.SEVERE, "Unexpected IOException while creating skin '" + args[0]
                    + "' with source '" + args[1] + "'", e);
        }
    }

    @Override
    public void onPlayerExecute(Player player, String[] args, Integer length) {
        try {
            if (args.length >= 2 && args.length <= 3) {
                URL url = new URL(args[1]);
                final File skinFile = new File(CustomSkins.skinFolder, args[0] + ".json");
                boolean privateUpload = false;
                if (args.length == 3 && (args[2].equalsIgnoreCase("true")
                        || args[2].equalsIgnoreCase("yes")
                        || args[2].equalsIgnoreCase("private")))
                    privateUpload = true;

                if (skinFile.exists()) {
                    player.sendMessage(Main.PREFIX + "Custom skin '" + args[0] + "' already exists. Please " +
                            "choose a different name.");
                    return;
                } else {
                    skinFile.createNewFile();
                }


                CustomSkins.skinClient.generateUrl(url.toString(), SkinOptions.name(args[0]), new SkinCallback() {

                    @Override
                    public void waiting(long l) {
                        player.sendMessage(Main.PREFIX+"§7Waiting " + (l / 1000D) + "s to upload skin...");
                    }

                    @Override
                    public void uploading() {
                        player.sendMessage(Main.PREFIX+"§eUploading skin...");
                    }

                    @Override
                    public void error(String s) {
                        player.sendMessage(Main.PREFIX+"§cError while generating skin: " + s);
                        player.sendMessage(Main.PREFIX+"§cPlease make sure the image is a valid skin texture and try again.");

                        skinFile.delete();
                    }

                    @Override
                    public void exception(Exception exception) {
                        player.sendMessage(Main.PREFIX+"§cException while generating skin, see console for details: " + exception.getMessage());
                        player.sendMessage(Main.PREFIX+"§cPlease make sure the image is a valid skin texture and try again.");

                        skinFile.delete();

                        Main.main.getLogger().log(Level.WARNING, "Exception while generating skin", exception);
                    }

                    @Override
                    public void done(Skin skin) {
                        player.sendMessage(Main.PREFIX+"§aSkin data generated.");
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", skin.data.uuid.toString());
                        jsonObject.addProperty("name", "");

                        JsonObject property = new JsonObject();
                        property.addProperty("name", "textures");
                        property.addProperty("value", skin.data.texture.value);
                        property.addProperty("signature", skin.data.texture.signature);

                        JsonArray propertiesArray = new JsonArray();
                        propertiesArray.add(property);

                        jsonObject.add("properties", propertiesArray);

                        try (Writer writer = new FileWriter(skinFile)) {
                            new Gson().toJson(jsonObject, writer);
                        } catch (IOException e) {
                            player.sendMessage(Main.PREFIX+"Failed to save skin to file: " + e.getMessage());
                            Main.main.getLogger().log(Level.SEVERE, "Failed to save skin", e);
                        }
                    }
                });
            } else
                help(player);
        } catch (MalformedURLException e) {
            player.sendMessage(Main.PREFIX+"Invalid URL");
        } catch (IOException e) {
            player.sendMessage(Main.PREFIX+"Unexpected IOException: " + e.getMessage());
            Main.main.getLogger().log(Level.SEVERE, "Unexpected IOException while creating skin '" + args[0]
                    + "' with source '" + args[1] + "'", e);
        }
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args, Integer length) {
        return Collections.emptyList();
    }
}