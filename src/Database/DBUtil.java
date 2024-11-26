package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

    // JDBC URL, username, and password for connecting to the database
    private static final String URL = "jdbc:mysql://localhost:3306/db_project?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    // Method to get a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD); // Establish the connection
    }
    
    // Method to close database resources (ResultSet, PreparedStatement, Connection)
    public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) { rs.close(); } // Close ResultSet if it is not null
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
        }
        try {
            if (pstmt != null) { pstmt.close(); } // Close PreparedStatement if it is not null
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
        }
        try {
            if (conn != null) { conn.close(); } // Close Connection if it is not null
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
}
