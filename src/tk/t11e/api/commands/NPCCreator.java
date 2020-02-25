package tk.t11e.api.commands;
// Created by booky10 in PaperT11EAPI (20:07 24.02.20)

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("EmptyMethod")
public class NPCCreator extends CommandExecutor {

    final List<NPC> NPCs = new ArrayList<>();
    public static final File npcFolder= new File(Main.main.getDataFolder(),"NPCs");

    public NPCCreator() {
        super(Main.main, "npc", "/npc <create|list|remove> <npc> <skin>", "npc",
                Receiver.ALL);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args, Integer length) {

    }

    @Override
    public void onPlayerExecute(Player player, String[] args, Integer length) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage("§e--------§6[NPCs]§e--------");
                for (NPC npc : NPCs) {
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
                            "/minecraft:teleport @s " + npc.getLocation().getX() + npc.getLocation().getY()
                                    + npc.getLocation().getZ() + npc.getLocation().getYaw() + npc.getLocation()
                                    .getPitch()));
                    player.sendMessage(message);
                }
                player.sendMessage("§e--------§6[NPCs]§e--------");
            } else
                help(player);
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("create")){
                Location location = new Location(player.getWorld(),player.getLocation().getX(),
                        player.getLocation().getY(),player.getLocation().getZ(),player.getLocation().getYaw(),
                        player.getLocation().getPitch());
                File skinFile = new File(CustomSkins.skinFolder,args[2]+".json");
                if(skinFile.exists()){
                    NPC npc = new NPC(args[1],args[2],args[1],location);
                    npc.updateNPC().sendPackets();
                    NPCRegistry.register(npc);
                    Bukkit.getScheduler().runTaskLater(Main.main, () -> {
                        File skinSave=new File(npcFolder,npc.getUUID().toString()+".yml");
                        FileConfiguration skinSaveConfig= YamlConfiguration.loadConfiguration(skinFile);
                        //TODO
                    },40);
                    player.sendMessage(Main.PREFIX+"§aSuccessfully created NPC!");
                }else
                    player.sendMessage(Main.PREFIX+"The skin does not exits! Create it with " +
                            "\"/createcustomskin\"!");
            }
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
                    for (NPC npc : NPCs) {
                        list.add(npc.getListName());
                    }
                    list.add("all");
                    list.add("world");
                }
            }else if(args.length==3&&args[0].equalsIgnoreCase("create")
                    ||args[0].equalsIgnoreCase("add")){
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