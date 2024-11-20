package Post;

import java.sql.*;
import Database.DBUtil;

public class PostDAO {

    // Create a new post
    public int createPost(int authorId, String content) throws SQLException {
        String query = "INSERT INTO Post (USER_ID, CONTENT) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection(); // DB 연결
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, authorId);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

            // Retrieve the generated POST_ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the generated POST_ID
                }
            }
        }
        return -1; // Return -1 if post creation fails
    }

    // Add a like
    public boolean addLike(int userId, int postId) throws SQLException {
        String query = "INSERT INTO Like (USER_ID, POST_ID) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Remove a like
    public boolean removeLike(int userId, int postId) throws SQLException {
        String query = "DELETE FROM Like WHERE USER_ID = ? AND POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get like count for a post
    public int getLikeCount(int postId) throws SQLException {
        String query = "SELECT COUNT(*) AS like_count FROM Like WHERE POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("like_count");
                }
            }
        }
        return 0; // Default to 0 if no likes found
    }
    
 // Add a Hate
    public boolean addHate(int userId, int postId) throws SQLException {
        String query = "INSERT INTO Hate (USER_ID, POST_ID) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Remove a Hate
    public boolean removeHate(int userId, int postId) throws SQLException {
        String query = "DELETE FROM Like WHERE USER_ID = ? AND POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get Hate count for a post
    public int getHateCount(int postId) throws SQLException {
        String query = "SELECT COUNT(*) AS Hate_count FROM Hate WHERE POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("like_count");
                }
            }
        }
        return 0; // Default to 0 if no likes found
    }

    // Add a comment
    public boolean addComment(int postId, int userId, String comment) throws SQLException {
        String query = "INSERT INTO Comment (POST_ID, AUTHOR_ID, COMMENT) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, comment);
            return pstmt.executeUpdate() > 0;
        }
    }
    
}

