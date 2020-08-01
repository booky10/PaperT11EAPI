package tk.t11e.api.query;
// Created by booky10 in MinecraftQueryAPI (21:52 18.07.20)

import java.util.HashMap;
import java.util.StringJoiner;
import java.util.UUID;

public class PingResponse {

    private final String description, favicon, version;
    private final Double latency;
    private final Integer maxPlayers, playerCount, protocol;
    private final HashMap<UUID, String> players;

    PingResponse(String description, String favicon, String version, Double latency, Integer maxPlayers,
                 Integer playerCount, Integer protocol, HashMap<UUID, String> players) {
        this.description = description;
        this.favicon = favicon;
        this.version = version;
        this.latency = latency;
        this.maxPlayers = maxPlayers;
        this.playerCount = playerCount;
        this.protocol = protocol;
        this.players = players;
    }

    public String getDescription() {
        return description;
    }

    public Double getLatency() {
        return latency;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public Integer getProtocol() {
        return protocol;
    }

    public HashMap<UUID, String> getPlayers() {
        return players;
    }

    public String getFavicon() {
        return favicon;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PingResponse.class.getSimpleName() + "[", "]")
                .add("description='" + description + "'")
                .add("favicon='" + favicon + "'")
                .add("version='" + version + "'")
                .add("latency=" + latency)
                .add("maxPlayers=" + maxPlayers)
                .add("playerCount=" + playerCount)
                .add("protocol=" + protocol)
                .add("players=" + players)
                .toString();
    }
}