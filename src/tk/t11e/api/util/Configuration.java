package tk.t11e.api.util;
// Created by booky10 in BanSQL (14:23 15.02.20)

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@SuppressWarnings({"UnusedReturnValue", "ResultOfMethodCallIgnored"})
public abstract class Configuration {

    private File file;
    private FileConfiguration config;

    public Configuration(File file) {
        this.file = file;
        init();
    }

    public Configuration(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        this.file = new File(plugin.getDataFolder().getAbsolutePath() + "/config.yml");
        init();
    }

    public Configuration init() {
        if (!this.file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
        values();
        return this;
    }

    public Configuration init(File file) {
        this.file = file;
        init();
        return this;
    }

    public abstract void values();

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Configuration setFile(File file) {
        this.file = file;
        init();

        return this;
    }
}