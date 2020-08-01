package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (19:43 14.07.20)

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private File file = null;
    protected YamlConfiguration config;

    protected final void setFile(File file) {
        if (this.file != null) throw new IllegalStateException("File is already set!");
        this.file = file;
        reloadConfig();
    }

    public final void reloadConfig() {
        if (file == null) throw new IllegalStateException("File is null");
        createFile();
        config = YamlConfiguration.loadConfiguration(file);
    }

    public final void saveConfig() {
        if (file == null) throw new IllegalStateException("File is null");
        if (config == null) throw new IllegalStateException("Config is null");
        try {
            config.save(file);
        } catch (Exception exception) {
            throw new Error("Error while saving config", exception);
        }
    }

    public final void set(String path, Object value) {
        config.set(path, value);
    }

    public final Boolean contains(String path) {
        return config.contains(path);
    }

    public YamlConfiguration get() {
        return config;
    }

    private void createFile() {
        if (file == null) throw new IllegalStateException("File is null");
        try {
            new File(file + File.separator + "..").mkdirs();
            if (!file.exists())
                file.createNewFile();
        } catch (Exception exception) {
            throw new Error("Error while creating File!", exception);
        }
    }

    public final File getFile() {
        return file;
    }
}