package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (17:48 26.02.20)

import org.bukkit.entity.Player;
import tk.t11e.api.main.Main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeUtils {

    public static void sendPlayerToServer(Player player,String server) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            outputStream.writeUTF("Connect");
            outputStream.writeUTF(server);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        player.sendPluginMessage(Main.main, "BungeeCord",byteArrayOutputStream.toByteArray());
    }
}