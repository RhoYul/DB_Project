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

            // Retrieve the generated ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1; // Return -1 if post creation fails
    }

    // Get all posts for a user (only posts from followed users)
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
            pstmt.setInt(1, userId); // FOLLOW_ID는 로그인된 사용자의 ID
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(new PostDTO(
                            rs.getInt("ID"),
                            rs.getString("LOGIN_ID"), // 게시글 작성자의 로그인 ID
                            rs.getString("USER_NAME"),    // 게시글 작성자의 이름
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
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                            deleteStmt.setInt(1, postId);
                            deleteStmt.setInt(2, userId);
                            deleteStmt.executeUpdate();
                        }
                    } else {
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
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                            deleteStmt.setInt(1, postId);
                            deleteStmt.setInt(2, userId);
                            deleteStmt.executeUpdate();
                        }
                    } else {
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

    // Add a comment to a post
    public boolean addComment(int postId, int userId, String comment) throws SQLException {
        String query = "INSERT INTO Comment (POST_ID, USER_ID, COMMENT) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, comment);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get all comments for a post
    public List<String> getCommentsForPost(int postId) throws SQLException {
        String query = "SELECT COMMENT FROM Comment WHERE POST_ID = ? ORDER BY REGDATE ASC";
        List<String> comments = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(rs.getString("COMMENT"));
                }
            }
        }
        return comments;
    }
}
