package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/maze_game?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root"; // ndryshoje me përdoruesin tënd
    private static final String PASSWORD = "root"; // ndryshoje me fjalëkalimin tënd

    public static Connection getConnection() throws SQLException {
        try {
            // Print connection attempt
            System.out.println("Attempting to connect to database...");
            System.out.println("URL: " + URL);

            // Load the driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful!");
            return conn;

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            throw new SQLException("Database driver not found", e);
        } catch (SQLException e) {
            System.out.println("Failed to connect to database!");
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Message: " + e.getMessage());
            throw e;
        }
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Database connection test successful!");
        } catch (SQLException e) {
            System.out.println("Database connection test failed!");
            e.printStackTrace();
        }
    }
}