package edu.clevertec.check.util;

import lombok.Data;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

@Data
public class ConnectionManagerTest implements ConnectionManager{

    private static final String PROPERTIES_FILE = "application-test.yml";

    private static final String URL = "db.url";
    private static final String USER = "db.username";
    private static final String PASSWORD = "db.password";
    private static final String DRIVER = "db.driver";

     static {
        loadDriver();
    }

    @SneakyThrows
    private static void loadDriver() {
        Class.forName(SettingsUtilTest.get(DRIVER));
    }

    @SneakyThrows
    public  Connection get() {
        return DriverManager.getConnection(
                SettingsUtilTest.get(URL),
                SettingsUtilTest.get(USER),
                SettingsUtilTest.get(PASSWORD));
    }
}
