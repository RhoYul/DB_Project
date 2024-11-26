package Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Database.DBUtil;

public class PostDAO {

    // Create a new post
    public int createPost(int userId, String content) throws SQLException {
        String query = "INSERT INTO Post (USER_ID, CONTENT) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

            // Retrieve the generated post ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return generated post ID
                }
            }
        }
        return -1; // Return -1 if post creation fails
    }

    // Get posts from followed users for the logged-in user
    public List<PostDTO> getPostsForUser(int userId) throws SQLException {
        String query = """
                SELECT DISTINCT
        			p.ID,
        			u.USER_ID AS LOGIN_ID,
        			u.USER_NAME,
        			p.CONTENT,
        			(SELECT COUNT(*) FROM `likes` WHERE POST_ID = p.ID) AS LIKES,
        			(SELECT COUNT(*) FROM `hates` WHERE POST_ID = p.ID) AS HATES,
        			p.REGDATE,
        			p.UPDATED_AT
        		FROM
        			post p
        		JOIN
        			follow f ON f.FOLLOWING_ID = p.USER_ID
        		JOIN
        			USERS u ON u.ID = p.USER_ID
        		WHERE
        			f.FOLLOW_ID = ?
        		ORDER BY
        			p.REGDATE DESC;
                """;

        List<PostDTO> posts = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId); // Filter by logged-in user's ID
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(new PostDTO(
                            rs.getInt("ID"),
                            rs.getString("LOGIN_ID"), // Login ID of the post creator
                            rs.getString("USER_NAME"), // Name of the post creator
                            rs.getString("CONTENT"),
                            rs.getInt("LIKES"),
                            rs.getInt("HATES"),
                            rs.getString("REGDATE"),
                            rs.getString("UPDATED_AT")
                    ));
                }
            }
        }
        return posts;
    }
    
    // Get posts created by a specific user
    public List<PostDTO> getPostsByUser(int userId) throws SQLException {
        String query = """
                SELECT 
                    p.ID,
                    u.USER_ID AS LOGIN_ID,
                    u.USER_NAME,
                    p.CONTENT,
                    (SELECT COUNT(*) FROM `likes` WHERE POST_ID = p.ID) AS LIKES,
                    (SELECT COUNT(*) FROM `hates` WHERE POST_ID = p.ID) AS HATES,
                    p.REGDATE,
                    p.UPDATED_AT
                FROM
                    post p
                JOIN
                    users u ON u.ID = p.USER_ID
                WHERE
                    p.USER_ID = ?
                ORDER BY
                    p.REGDATE DESC;
                """;

        List<PostDTO> posts = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId); // Filter by the given user ID
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(new PostDTO(
                            rs.getInt("ID"),
                            rs.getString("LOGIN_ID"),
                            rs.getString("USER_NAME"),
                            rs.getString("CONTENT"),
                            rs.getInt("LIKES"),
                            rs.getInt("HATES"),
                            rs.getString("REGDATE"),
                            rs.getString("UPDATED_AT")
                    ));
                }
            }
        }
        return posts;
    }

    // Toggle a like for a post
    public void toggleLike(int postId, int userId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            String checkQuery = "SELECT * FROM `likes` WHERE POST_ID = ? AND USER_ID = ?";
            String insertQuery = "INSERT INTO `likes` (POST_ID, USER_ID, REGDATE) VALUES (?, ?, NOW())";
            String deleteQuery = "DELETE FROM `likes` WHERE POST_ID = ? AND USER_ID = ?";
            String deleteHateQuery = "DELETE FROM `hates` WHERE POST_ID = ? AND USER_ID = ?";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, postId);
                checkStmt.setInt(2, userId);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        // Remove like if it already exists
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                            deleteStmt.setInt(1, postId);
                            deleteStmt.setInt(2, userId);
                            deleteStmt.executeUpdate();
                        }
                    } else {
                        // Remove hate if it exists and add like
                        try (PreparedStatement deleteHateStmt = conn.prepareStatement(deleteHateQuery)) {
                            deleteHateStmt.setInt(1, postId);
                            deleteHateStmt.setInt(2, userId);
                            deleteHateStmt.executeUpdate();
                        }
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, postId);
                            insertStmt.setInt(2, userId);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
            conn.commit();
        }
    }

    // Toggle a hate for a post
    public void toggleHate(int postId, int userId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            String checkQuery = "SELECT * FROM `hates` WHERE POST_ID = ? AND USER_ID = ?";
            String insertQuery = "INSERT INTO `hates` (POST_ID, USER_ID, REGDATE) VALUES (?, ?, NOW())";
            String deleteQuery = "DELETE FROM `hates` WHERE POST_ID = ? AND USER_ID = ?";
            String deleteLikeQuery = "DELETE FROM `likes` WHERE POST_ID = ? AND USER_ID = ?";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, postId);
                checkStmt.setInt(2, userId);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        // Remove hate if it already exists
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                            deleteStmt.setInt(1, postId);
                            deleteStmt.setInt(2, userId);
                            deleteStmt.executeUpdate();
                        }
                    } else {
                        // Remove like if it exists and add hate
                        try (PreparedStatement deleteLikeStmt = conn.prepareStatement(deleteLikeQuery)) {
                            deleteLikeStmt.setInt(1, postId);
                            deleteLikeStmt.setInt(2, userId);
                            deleteLikeStmt.executeUpdate();
                        }
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, postId);
                            insertStmt.setInt(2, userId);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
            conn.commit();
        }
    }

    // Get the count of likes for a specific post
    public int getLikesCount(int postId) {
        String query = "SELECT COUNT(*) FROM likes WHERE POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the count of likes
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if an error occurs
    }

    // Get the count of hates for a specific post
    public int getHatesCount(int postId) {
        String query = "SELECT COUNT(*) FROM hates WHERE POST_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the count of hates
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if an error occurs
    }
}
