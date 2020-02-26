package tk.t11e.api.main;
// Created by booky10 in PaperT11EAPI (20:28 10.02.20)

import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.lightlevel.LightLevel;
import org.mineskin.customskins.CustomSkins;
import tk.t11e.api.commands.ClientCrash;
import tk.t11e.api.commands.NPCCreator;
import tk.t11e.api.listener.JoinLeaveListener;
import tk.t11e.api.listener.TeleportListener;
import tk.t11e.api.npc.InteractListener;
import tk.t11e.api.npc.NPC;
import tk.t11e.api.npc.NPCRegistry;
import tk.t11e.api.packets.PacketCore;
import tk.t11e.api.util.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§bT11E§7]§c ", NO_PERMISSION = PREFIX + "You don't have " +
            "the permissions for this!";
    public static Main main;
    public static final Character separator = File.separatorChar;

    @Override
    public void onEnable() {
        main = this;

        new PacketCore(this);

        Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new TeleportListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        new NPCCreator().init();
        new ClientCrash().init();

        new LightLevel().onEnable();
        new CustomSkins().onEnable();

        NPCRegistry.register(new NPC("hypixel", "hypixel", "hypixel", false,
                new Location(Bukkit.getWorld("lobby"), 0, 100, 0), NPC.Action.EXECUTE_COMMAND)
                .setActionString("Hello World!"));

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getScheduler().runTaskAsynchronously(this, NPCRegistry::make);
        registerYaml();
    }

    @Override
    public void onDisable() {
        NPCRegistry.unmake();
    }

    public FileConfiguration getSkinSaves() {
        File saves = new File(getDataFolder().getAbsolutePath() + separator + "saves.yml");
        getDataFolder().mkdirs();
        if (!saves.exists())
            saveResource("saves.yml", true);

        return YamlConfiguration.loadConfiguration(saves);
    }

    public void saveSaves(FileConfiguration config) {
        File saves = new File(getDataFolder().getAbsolutePath() + separator + "skinSaves.yml");
        try {
            config.save(saves);
        } catch (IOException exception) {
            ExceptionUtils.print(exception);
        }
    }

    private void registerYaml() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
            if (NPCCreator.npcFolder.listFiles() != null)
                for (File file : Objects.requireNonNull(NPCCreator.npcFolder.listFiles())) {
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    String name = config.getString("name");
                    String skinName = config.getString("skin.name");
                    String skinValue = config.getString("skin.value");
                    String skinSignature = config.getString("skin.signature");
                    String listName = config.getString("listName");
                    String actionString = config.getString("actionString");
                    World world = Bukkit.getWorld(UUID.fromString(Objects.requireNonNull(config.getString(
                            "location" +
                            ".world"))));
                    double x = config.getDouble("location.x");
                    double y = config.getDouble("location.y");
                    double z = config.getDouble("location.z");
                    float yaw = Float.parseFloat(Objects.requireNonNull(config.getString("location.yaw")));
                    float pitch = Float.parseFloat(Objects.requireNonNull(config.getString("location.pitch")));
                    UUID uuid = UUID.fromString(Objects.requireNonNull(config.getString("uuid")));
                    Boolean showInTabList = config.getBoolean("showInTabList");
                    NPC.Action action =
                            NPC.Action.valueOf(Objects.requireNonNull(config.getString("action")).toUpperCase());

                    Property property = new Property(skinName, skinValue, skinSignature);
                    Location location = new Location(world, x, y, z, yaw, pitch);

                    NPC npc = new NPC(name, skinName, listName, uuid, showInTabList, location, action, property);
                    npc.setActionString(actionString).updateNPC().sendPackets();
                    NPCRegistry.register(npc);
                }
        }, 100);
    }
}