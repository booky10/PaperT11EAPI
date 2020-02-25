package tk.t11e.api.main;
// Created by booky10 in PaperT11EAPI (20:28 10.02.20)

import org.bukkit.Location;
import org.mineskin.customskins.CustomSkins;
import org.mineskin.gallery.MineskinGallery;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import tk.t11e.api.commands.NPCCreator;
import tk.t11e.api.listener.JoinLeaveListener;
import tk.t11e.api.npc.NPC;
import tk.t11e.api.npc.NPCRegistry;
import tk.t11e.api.util.ExceptionUtils;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§bT11E§7]§c ", NO_PERMISSION = PREFIX + "You don't have " +
            "the permissions for this!";
    public static Main main;
    public static final Character separator = File.separatorChar;

    @Override
    public void onEnable() {
        main=this;

        Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), this);

        new MineskinGallery().onEnable();
        new CustomSkins().onEnable();
        new NPCCreator().init();

        NPCRegistry.register(new NPC("hypixel",new Location(Bukkit.getWorld("lobby"),0,100,0)));

        Bukkit.getScheduler().runTaskAsynchronously(this, NPCRegistry::make);
    }

    @Override
    public void onDisable() {
        NPCRegistry.unmake();
    }

    public FileConfiguration getSkinSaves(){
        File saves=new File(getDataFolder().getAbsolutePath()+separator+"saves.yml");
        getDataFolder().mkdirs();
        if(!saves.exists())
            saveResource("saves.yml",true);

        return YamlConfiguration.loadConfiguration(saves);
    }

    public void saveSaves(FileConfiguration config) {
        File saves=new File(getDataFolder().getAbsolutePath()+separator+"skinSaves.yml");
        try {
            config.save(saves);
        } catch (IOException exception) {
            ExceptionUtils.print(exception);
        }
    }
}