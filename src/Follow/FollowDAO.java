package Follow;

import java.sql.*;
import Database.DBUtil;

public class FollowDAO {

    // Toggle follow/unfollow status
    public boolean toggleFollow(int followerId, int followingId) {
        // If the user is already following, unfollow them
        if (isFollowing(followerId, followingId)) {
            return unfollowUser(followerId, followingId);
        } else {
            // Otherwise, follow the user
            return followUser(followerId, followingId);
        }
    }

    // Add a follow record
    public boolean followUser(int followerId, int followingId) {
        String query = "INSERT INTO follow (FOLLOW_ID, FOLLOWING_ID) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, followerId); // Set follower ID
            pstmt.setInt(2, followingId); // Set following ID
            pstmt.executeUpdate(); // Execute the query
            return true; // Return true if successful
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
            return false; // Return false if an error occurs
        }
    }

    // Remove a follow record (unfollow)
    public boolean unfollowUser(int followerId, int followingId) {
        String query = "DELETE FROM follow WHERE FOLLOW_ID = ? AND FOLLOWING_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, followerId); // Set follower ID
            pstmt.setInt(2, followingId); // Set following ID
            pstmt.executeUpdate(); // Execute the query
            return true; // Return true if successful
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
            return false; // Return false if an error occurs
        }
    }

    // Check if a user is following another user
    public boolean isFollowing(int followerId, int followingId) {
        String query = "SELECT COUNT(*) FROM follow WHERE FOLLOW_ID = ? AND FOLLOWING_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, followerId); // Set follower ID
            pstmt.setInt(2, followingId); // Set following ID
            ResultSet rs = pstmt.executeQuery(); // Execute the query
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if the count is greater than 0
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
        }
        return false; // Return false if no follow relationship exists
    }

    // Get the number of followers for a specific user
    public int getFollowerCount(int userId) {
        String query = "SELECT COUNT(*) FROM follow WHERE FOLLOWING_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, userId); // Set the user ID
            ResultSet rs = pstmt.executeQuery(); // Execute the query
            if (rs.next()) {
                return rs.getInt(1); // Return the count of followers
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
        }
        return 0; // Return 0 if an error occurs or no followers exist
    }

    // Get the number of users the specified user is following
    public int getFollowingCount(int userId) {
        String query = "SELECT COUNT(*) FROM follow WHERE FOLLOW_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, userId); // Set the user ID
            ResultSet rs = pstmt.executeQuery(); // Execute the query
            if (rs.next()) {
                return rs.getInt(1); // Return the count of followings
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
        }
        return 0; // Return 0 if an error occurs or no followings exist
    }
}
