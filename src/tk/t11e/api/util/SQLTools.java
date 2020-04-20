package tk.t11e.api.util;
// Created by booky10 in BanSQL (14:37 15.02.20)

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLTools {

    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final Integer port;
    private Connection connection;

    public SQLTools(String host, Integer port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        initConnection();
    }

    public void initConnection() {
        try {
            //synchronized (this) {
                if (connection != null && !connection.isClosed())
                    return;

                Class.forName("com.mysql.jdbc.Driver");
                this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" +
                        this.port + "/" + this.database, this.username, this.password);
            //}
        } catch (SQLException | ClassNotFoundException exception) {
            ExceptionUtils.print(exception);
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public Integer getPort() {
        return port;
    }

    public Connection getConnection() {
        try {
            if (connection == null||connection.isClosed())
                initConnection();
            return connection;
        } catch (SQLException exception) {
            ExceptionUtils.print(exception);
            return connection;
        }
    }
}