package org.mineskin.customskins;

import org.bukkit.event.Listener;
import org.mineskin.MineskinClient;
import tk.t11e.api.main.PaperT11EAPIMain;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class CustomSkins implements Listener {

    public static final File skinFolder = new File(PaperT11EAPIMain.main.getDataFolder(), "skins");
    public static final Set<String> loadedSkins = new HashSet<>();

    static MineskinClient skinClient;

    public void onEnable() {
        new ApplySkin().init();
        new CreateSkin().init();

        if (!skinFolder.exists())
            skinFolder.mkdirs();

        skinClient = new MineskinClient();
    }
}