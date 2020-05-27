package tk.t11e.api.main;
// Created by booky10 in PaperT11EAPI (20:28 10.02.20)

import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineskin.customskins.CustomSkins;
import tk.t11e.api.commands.ClientCrash;
import tk.t11e.api.commands.NPCCreator;
import tk.t11e.api.commands.SetMaxHealth;
import tk.t11e.api.listener.JoinLeaveListener;
import tk.t11e.api.listener.TeleportListener;
import tk.t11e.api.npc.InteractListener;
import tk.t11e.api.npc.NPC;
import tk.t11e.api.npc.NPCRegistry;
import tk.t11e.api.packets.PacketCore;
import tk.t11e.api.util.VersionHelper;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§bCraftTMB§7]§c ", NO_PERMISSION = PREFIX + "You don't have " +
            "the permissions for this!";
    public static Main main;

    @Override
    public void onEnable() {
        if (!VersionHelper.aboveOr18()) {
            getLogger().severe("");
            getLogger().severe("|----------------------------------------|");
            getLogger().severe("| This API only supports 1.8 up to 1.15! |");
            getLogger().severe("|----------------------------------------|");
            getLogger().severe("");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            main = this;

            new PacketCore(this);

            Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), this);
            Bukkit.getPluginManager().registerEvents(new TeleportListener(), this);
            Bukkit.getPluginManager().registerEvents(new InteractListener(), this);

            new NPCCreator().init();
            new ClientCrash().init();
            new SetMaxHealth().init();

            new CustomSkins().onEnable();
            /*if (VersionHelper.aboveOr113())
                new LightLevel().onEnable();*/

            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            Bukkit.getScheduler().runTaskAsynchronously(this, NPCRegistry::make);
            registerYaml();
        }
    }

    @Override
    public void onDisable() {
        NPCRegistry.unmake();
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