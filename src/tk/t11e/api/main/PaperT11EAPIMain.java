package tk.t11e.api.main;
// Created by booky10 in PaperT11EAPI (20:28 10.02.20)

import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineskin.customskins.CustomSkins;
import org.mineskin.gallery.MineskinGallery;
import tk.t11e.api.commands.ClientCrash;
import tk.t11e.api.commands.NPCCreator;
import tk.t11e.api.commands.SetMaxHealth;
import tk.t11e.api.listener.JoinLeaveListener;
import tk.t11e.api.listener.TeleportListener;
import tk.t11e.api.notesound.utils.MidiUtil;
import tk.t11e.api.npc.InteractListener;
import tk.t11e.api.npc.NPC;
import tk.t11e.api.npc.NPCRegistry;
import tk.t11e.api.packets.PacketCore;
import tk.t11e.api.util.OtherUtils;
import tk.t11e.api.util.VersionHelper;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class PaperT11EAPIMain extends PaperPlugin {

    public static final String PREFIX = "§7[§bCraftTMB§7]§c ", NO_PERMISSION = PREFIX + "You don't have " +
            "the permissions for this!";
    public static PaperT11EAPIMain main;

    @Override
    public void preEnable() {
        main = this;
        if (VersionHelper.aboveOr18()) {
            new PacketCore(this);

            Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), this);
            Bukkit.getPluginManager().registerEvents(new TeleportListener(), this);
            Bukkit.getPluginManager().registerEvents(new InteractListener(), this);

            new NPCCreator().init();
            new ClientCrash().init();
            new SetMaxHealth().init();

            if (Bukkit.getPluginManager().isPluginEnabled("NickNamer")) {
                new CustomSkins().onEnable();
                new MineskinGallery().onEnable();
            } else
                getLogger().warning("NickNamer not found, \"/createskin\", \"/applyskin\" and \"/mineskin\"" +
                        " will not be working!");

            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            NPCRegistry.make();
            registerYaml();
        } else {
            getLogger().info(OtherUtils.generateBox("Use this plugin from 1.8 to 1.15!"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void preDisable() {
        NPCRegistry.unmake();
        MidiUtil.stop();
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