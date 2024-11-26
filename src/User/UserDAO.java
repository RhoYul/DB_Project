package User;

import Database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Insert a new user into the database during registration
    public void insertUser(String userId, String passwd, String userName, String email) {
        String query = "INSERT INTO Users (USER_ID, PASSWD, USER_NAME, EMAIL) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, passwd);
            pstmt.setString(3, userName);
            pstmt.setString(4, email);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User inserted successfully!");
            }
        } catch (SQLException ex) {
            System.out.println("Error inserting user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Get the user ID for a given login ID
    public int getUserId(String userId) {
        String query = "SELECT ID FROM Users WHERE USER_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching user ID: " + ex.getMessage());
            ex.printStackTrace();
        }
        return -1; // Return -1 if the user is not found
    }

    // Get the login ID for a given user ID
    public String getUserLogId(int ID) {
        String query = "SELECT USER_ID FROM Users WHERE ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("USER_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the user is not found
    }

    // Get the name for a given user ID
    public String getUserNameLogId(int ID) {
        String query = "SELECT USER_NAME FROM Users WHERE ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("USER_NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the user is not found
    }

    // Authenticate a user with their login ID and password
    public boolean authenticateUser(String userId, String passwd) {
        String query = "SELECT * FROM Users WHERE USER_ID = ? AND PASSWD = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, passwd);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Return true if a matching record is found
            }
        } catch (SQLException ex) {
            System.out.println("Authentication error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false; // Return false if authentication fails
    }

    // Search for a user by their login ID
    public String searchUserById(String userId) {
        String query = "SELECT USER_NAME FROM Users WHERE USER_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userName = rs.getString("USER_NAME");
                return userId + " " + userName; // Return user ID and name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the user is not found
    }
}
