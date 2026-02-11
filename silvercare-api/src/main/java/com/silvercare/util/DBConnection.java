package com.silvercare.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static Properties props = new Properties();

    static {
        try (InputStream in = DBConnection.class.getResourceAsStream("/db.properties")) {
            // If loaded from WEB-INF/classes, it should be at root of classpath
            if (in == null) {
                // Fallback: try loading from context class loader if running in different
                // environment
                InputStream in2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
                if (in2 != null) {
                    props.load(in2);
                    in2.close();
                } else {
                    System.err.println("DBConnection: db.properties not found on classpath.");
                }
            } else {
                props.load(in);
            }

            if (props.containsKey("jdbc.driver")) {
                Class.forName(props.getProperty("jdbc.driver"));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize DBConnection details", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // Advanced: Use Environment Variables first (for Render/Production)
        // Fallback to db.properties for local development
        String url = System.getenv("DB_URL");
        if (url == null)
            url = props.getProperty("jdbc.url");

        String user = System.getenv("DB_USER");
        if (user == null)
            user = props.getProperty("jdbc.user");

        String password = System.getenv("DB_PASSWORD");
        if (password == null)
            password = props.getProperty("jdbc.password");

        System.out.println("DBConnection: Connecting to " + url + " as " + user);
        return DriverManager.getConnection(url, user, password);
    }
}
