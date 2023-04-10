package edu.clevertec.check.util;

import lombok.Data;
import lombok.SneakyThrows;
import java.sql.Connection;
import java.sql.DriverManager;

@Data
public class ConnectionManagerImpl implements ConnectionManager {

    private static final String PROPERTIES_FILE = "application.yml";
    private static final String URL = "db.url";
    private static final String USER = "db.username";
    private static final String PASSWORD = "db.password";
    private static final String DRIVER = "db.driver";

    static {
        loadDriver();
    }

    @SneakyThrows
    private static void loadDriver() {
        Class.forName(SettingsUtil.get(DRIVER));
    }

    /**
     *
     * @return Connection
     */
    @SneakyThrows
    @Override
    public Connection get() {
        return DriverManager.getConnection(
                SettingsUtil.get(URL),
                SettingsUtil.get(USER),
                SettingsUtil.get(PASSWORD));
    }
}
