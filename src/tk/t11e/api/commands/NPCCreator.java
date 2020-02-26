package tk.t11e.api.commands;
// Created by booky10 in PaperT11EAPI (20:07 24.02.20)

import com.mojang.authlib.properties.Property;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mineskin.customskins.CustomSkins;
import tk.t11e.api.main.Main;
import tk.t11e.api.npc.NPC;
import tk.t11e.api.npc.NPCRegistry;
import tk.t11e.api.util.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"EmptyMethod", "ResultOfMethodCallIgnored"})
public class NPCCreator extends CommandExecutor {

    public static final File npcFolder = new File(Main.main.getDataFolder(), "NPCs");

    public NPCCreator() {
        super(Main.main, "npc", "/npc <create|list|remove> <npc> <skin>", "npc",
                Receiver.ALL);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, Integer length) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage("--------[NPCs]--------");
                for (NPC npc : NPCRegistry.getNPCs())
                    sender.sendMessage("  - " + npc.getListName());
                sender.sendMessage("--------[NPCs]--------");
            } else
                help(sender);
        } else
            help(sender);
    }

    @Override
    public void onPlayerExecute(Player player, String[] args, Integer length) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage("§e--------§6[NPCs]§e--------");
                for (NPC npc : NPCRegistry.getNPCs()) {
                    TextComponent message = new TextComponent("§e  - " + npc.getListName());
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("§7Skin: " + npc.getSkinName() + "\n" +
                                    "§7Location:\n" +
                                    "§7  World: " + npc.getLocation().getWorld().getName() + "\n" +
                                    "§7  X:     " + npc.getLocation().getBlockX() + "\n" +
                                    "§7  Y:     " + npc.getLocation().getBlockY() + "\n" +
                                    "§7  Z:     " + npc.getLocation().getBlockZ() + "\n" +
                                    "§7  Yaw:   " + npc.getLocation().getYaw() + "\n" +
                                    "§7  Pitch: " + npc.getLocation().getPitch() + "\n" +
                                    "§r\n" +
                                    "§rClick to teleport!").create()));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                            "/minecraft:teleport @s " + npc.getLocation().getX() + " " + npc.getLocation().getY()
                                    + " " + npc.getLocation().getZ() + " " + npc.getLocation().getYaw() + " " + npc.getLocation()
                                    .getPitch()));
                    player.sendMessage(message);
                }
                player.sendMessage("§e--------§6[NPCs]§e--------");
            } else
                help(player);
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")) {
                Location location = player.getLocation();
                File skinFile = new File(CustomSkins.skinFolder, args[2] + ".json");

                if (args[1].split("").length <= 16)
                    if (skinFile.exists()) {
                        NPC npc = new NPC(args[1], args[2], args[1], false, location, NPC.Action.NOTHING);
                        npc.updateNPC().sendPackets();
                        NPCRegistry.register(npc);
                        Bukkit.getScheduler().runTaskLater(Main.main, () -> {
                            File skinSave = new File(npcFolder, npc.getUUID().toString() + ".yml");
                            FileConfiguration skinSaveConfig = YamlConfiguration.loadConfiguration(skinSave);

                            Property property = npc.getProfile().getProperties().get("textures").iterator().next();

                            skinSaveConfig.set("name", args[1]);
                            skinSaveConfig.set("skin.name", property.getName());
                            skinSaveConfig.set("skin.value", property.getValue());
                            skinSaveConfig.set("skin.signature", property.getSignature());
                            skinSaveConfig.set("listName", npc.getListName());
                            skinSaveConfig.set("actionString", npc.getActionString());
                            skinSaveConfig.set("location.world", location.getWorld().getUID().toString());
                            skinSaveConfig.set("location.x", location.getX());
                            skinSaveConfig.set("location.y", location.getY());
                            skinSaveConfig.set("location.z", location.getZ());
                            skinSaveConfig.set("location.yaw", location.getYaw());
                            skinSaveConfig.set("location.pitch", location.getPitch());
                            skinSaveConfig.set("uuid", npc.getUUID().toString());
                            skinSaveConfig.set("showInTabList", npc.getShowInTabList().toString());
                            skinSaveConfig.set("action", npc.getAction().toString().toUpperCase());
                            try {
                                skinSaveConfig.save(skinSave);
                            } catch (IOException exception) {
                                player.sendMessage(Main.PREFIX + "Error saving file!");
                                ExceptionUtils.print(exception);
                            }
                        }, 40);
                        player.sendMessage(Main.PREFIX + "§aSuccessfully created NPC!");
                    } else
                        player.sendMessage(Main.PREFIX + "The skin does not exits! Create it with " +
                                "\"/createcustomskin\"!");
                else
                    player.sendMessage(Main.PREFIX + "Names can only have 16 characters or lower!");
            } else
                help(player);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")) {
                if (args[1].equalsIgnoreCase("all")) {
                    Bukkit.getScheduler().runTaskAsynchronously(Main.main, () -> {
                        for (NPC npc : NPCRegistry.getNPCs()) {
                            npc.remove();
                            NPCRegistry.unregister(npc);
                            File file = new File(npcFolder, npc.getUUID().toString() + ".yml");
                            if (file.exists())
                                file.delete();
                        }
                        player.sendMessage(Main.PREFIX + "§aSuccessfully deleted all of the NPCs!");
                    });
                } else if (args[1].equalsIgnoreCase("world")) {
                    Bukkit.getScheduler().runTaskAsynchronously(Main.main, () -> {
                        for (NPC npc : NPCRegistry.getNPCs())
                            if (npc.getLocation().getWorld().getUID().equals(player.getWorld().getUID())) {
                                npc.remove();
                                NPCRegistry.unregister(npc);
                                File file = new File(npcFolder, npc.getUUID().toString() + ".yml");
                                if (file.exists())
                                    file.delete();
                            }
                        player.sendMessage(Main.PREFIX + "§aSuccessfully deleted all of the NPCs in" +
                                " your world!");
                    });
                } else if (args[1].equalsIgnoreCase("nearest")) {
                    Bukkit.getScheduler().runTaskAsynchronously(Main.main, () -> {
                        double distance = Double.MAX_VALUE;
                        NPC npcToDelete = null;
                        for (NPC npc : NPCRegistry.getNPCs())
                            if (npc.getLocation().getWorld().getUID().equals(player.getWorld().getUID())) {
                                double distance2 = player.getLocation().distance(npc.getLocation());
                                if (distance2 < distance) {
                                    distance = distance2;
                                    npcToDelete = npc;
                                }
                            }
                        if (npcToDelete != null) {
                            npcToDelete.remove();
                            NPCRegistry.unregister(npcToDelete);
                            File file = new File(npcFolder, npcToDelete.getUUID().toString() + ".yml");
                            if (file.exists())
                                file.delete();
                            player.sendMessage(Main.PREFIX + "§aSuccessfully deleted the nearest NPC!");
                        } else
                            player.sendMessage(Main.PREFIX + "There is no NPC nearby!");
                    });
                } else
                    help(player);
            } else
                help(player);
        } else

            help(player);

    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args, Integer length) {
        List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                list.add("create");
                list.add("list");
                list.add("remove");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove")) {
                    list.add("all");
                    list.add("world");
                    list.add("nearest");
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("create")
                    || args[0].equalsIgnoreCase("add")) {
                for (String s : Objects.requireNonNull(CustomSkins.skinFolder.list()))
                    if (s.endsWith(".json"))
                        list.add(s.substring(0, s.length() - 5));
                    else
                        list.addAll(getOnlinePlayerNames());
            }
        } else if (args.length == 1)
            list.add("list");
        return list;
    }
}