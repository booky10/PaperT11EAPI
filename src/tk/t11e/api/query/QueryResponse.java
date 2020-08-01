package tk.t11e.api.query;
// Created by booky10 in MinecraftQueryAPI (21:51 18.07.20)

import java.util.List;
import java.util.StringJoiner;

public class QueryResponse {

    private final Integer maxPlayers, playerCount;
    private final String motd, software, version, status;
    private final List<String> players, plugins;

    QueryResponse(Integer maxPlayers, Integer playerCount, String motd, String software, String version,
                  String status, List<String> players, List<String> plugins) {
        this.maxPlayers = maxPlayers;
        this.playerCount = playerCount;
        this.motd = motd;
        this.software = software;
        this.version = version;
        this.status = status;
        this.players = players;
        this.plugins = plugins;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public String getMotd() {
        return motd;
    }

    public String getSoftware() {
        return software;
    }

    public String getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getPlayers() {
        return players;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QueryResponse.class.getSimpleName() + "[", "]")
                .add("maxPlayers=" + maxPlayers)
                .add("playerCount=" + playerCount)
                .add("motd='" + motd + "'")
                .add("software='" + software + "'")
                .add("version='" + version + "'")
                .add("status='" + status + "'")
                .add("players=" + players)
                .add("plugins=" + plugins)
                .toString();
    }
}