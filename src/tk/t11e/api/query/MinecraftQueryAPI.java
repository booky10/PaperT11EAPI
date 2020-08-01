package tk.t11e.api.query;
// Created by booky10 in MinecraftQueryAPI (21:47 18.07.20)

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class MinecraftQueryAPI {

    private static final String queryURL = "https://api.minetools.eu/query/%address%/%port%";
    private static final String pingURL = "https://api.minetools.eu/ping/%address%/%port%";

    public static QueryResponse queryServer(String address, Integer port) {
        try {
            String localURL = queryURL;
            localURL = localURL.replace("%address%", address);
            localURL = localURL.replace("%port%", port.toString());
            URL url = new URL(localURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "MinecraftQueryAPI - by booky10 (Query)");
            if (connection.getResponseCode() == 200) {
                JsonObject response = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                if (response.get("status").getAsString().equals("ERR"))
                    throw new Exception("Error: Status returned \"ERR\"! (" + response.get("error").getAsString() + ")");
                else if (response.get("status").getAsString().equals("OK")) {
                    Integer maxPlayers = response.get("MaxPlayers").getAsJsonPrimitive().getAsInt(),
                            playerCount = response.get("Players").getAsJsonPrimitive().getAsInt();
                    String motd = response.get("Motd").getAsJsonPrimitive().getAsString(),
                            software = response.get("Software").getAsJsonPrimitive().getAsString(),
                            version = response.get("Version").getAsJsonPrimitive().getAsString(),
                            status = response.get("status").getAsJsonPrimitive().getAsString();
                    Iterator<JsonElement> playerIterator = response.get("Playerlist").getAsJsonArray().iterator(),
                            pluginsIterator = response.get("Plugins").getAsJsonArray().iterator();

                    List<String> players = new ArrayList<>(), plugins = new ArrayList<>();
                    while (playerIterator.hasNext())
                        players.add(playerIterator.next().getAsString());
                    while (pluginsIterator.hasNext())
                        plugins.add(playerIterator.next().getAsString());

                    return new QueryResponse(maxPlayers, playerCount, motd, software, version, status, players, plugins);
                } else
                    throw new Exception("Error: Unknown status \"" + response.get("status").getAsString() + "\"!");
            } else
                throw new Exception("Response Code: " + connection.getResponseCode() + " (" +
                        connection.getResponseMessage() + ")");
        } catch (Exception exception) {
            throw new Error(exception);
        }
    }

    public static QueryResponse queryServer(String address) {
        return queryServer(address, 25565);
    }

    public static PingResponse pingServer(String address, Integer port) {
        try {
            String localURL = pingURL;
            localURL = localURL.replace("%address%", address);
            localURL = localURL.replace("%port%", port.toString());
            URL url = new URL(localURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "MinecraftQueryAPI - by booky10 (Query)");
            if (connection.getResponseCode() == 200) {
                JsonObject response = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                if (response.has("error"))
                    throw new Exception("Error: " + response.get("error").getAsString() + "!");
                else {
                    String description = response.get("description").getAsJsonPrimitive().getAsString(),
                            favicon = response.get("favicon").isJsonPrimitive() ? response.get("favicon")
                                    .getAsJsonPrimitive().getAsString() : "-",
                            version = response.get("version").getAsJsonObject().get("name").getAsJsonPrimitive().getAsString();
                    Double latency = response.get("latency").getAsJsonPrimitive().getAsDouble();
                    Integer maxPlayers = response.get("players").getAsJsonObject().get("max").getAsJsonPrimitive().getAsInt(),
                            playerCount = response.get("players").getAsJsonObject().get("online").getAsJsonPrimitive().getAsInt(),
                            protocol = response.get("version").getAsJsonObject().get("protocol").getAsJsonPrimitive().getAsInt();
                    Iterator<JsonElement> playerIterator = response.get("players").getAsJsonObject().get("sample").getAsJsonArray()
                            .iterator();

                    HashMap<UUID, String> players = new HashMap<>();
                    while (playerIterator.hasNext()) {
                        JsonObject object = playerIterator.next().getAsJsonObject();
                        UUID uuid = UUID.fromString(object.get("id").getAsJsonPrimitive().getAsString());
                        String name = object.get("name").getAsJsonPrimitive().getAsString();
                        players.put(uuid, name);
                    }
                    return new PingResponse(description, favicon, version, latency, maxPlayers, playerCount, protocol, players);
                }
            } else
                throw new Exception("Response Code: " + connection.getResponseCode() + " (" +
                        connection.getResponseMessage() + ")");
        } catch (Exception exception) {
            throw new Error(exception);
        }
    }

    public static PingResponse pingServer(String address) {
        return pingServer(address, 25565);
    }
}