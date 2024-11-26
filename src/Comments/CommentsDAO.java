package Comments;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Database.DBUtil;
import Post.PostDTO;

public class CommentsDAO {

    // Add a new comment
    public boolean addComment(int postId, int userId, String commentText) {
        // SQL query to insert a comment into the database
        String sql = "INSERT INTO comments (POST_ID, USER_ID, COMMENT_TEXT, REGDATE) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBUtil.getConnection(); // Get a database connection
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
            stmt.setInt(1, postId); // Set the POST_ID parameter
            stmt.setInt(2, userId); // Set the USER_ID parameter
            stmt.setString(3, commentText); // Set the COMMENT_TEXT parameter
            return stmt.executeUpdate() > 0; // Execute the query and return true if successful
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace if an error occurs
        }
        return false; // Return false if the operation fails
    }

    // Get all comments for a specific post
    public List<CommentsDTO> getCommentsByPostId(int postId) {
        List<CommentsDTO> comments = new ArrayList<>(); // Initialize a list to store comments
        // SQL query to retrieve comments along with the user's name
        String sql = "SELECT C.*, U.USER_NAME FROM COMMENTS C INNER JOIN USERS U ON C.USER_ID = U.ID ORDER BY C.ID DESC";
        try (Connection conn = DBUtil.getConnection(); // Get a database connection
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
            try (ResultSet rs = stmt.executeQuery()) { // Execute the query and process the result set
                while (rs.next()) { // Iterate through the result set
                    comments.add(new CommentsDTO(
                            rs.getInt("ID"), // Retrieve the comment ID
                            rs.getInt("POST_ID"), // Retrieve the post ID
                            rs.getInt("USER_ID"), // Retrieve the user ID
                            rs.getString("COMMENT_TEXT"), // Retrieve the comment text
                            rs.getString("REGDATE"), // Retrieve the registration date
                            rs.getString("USER_NAME") // Retrieve the user's name
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace if an error occurs
        }
        return comments; // Return the list of comments
    }

    // Delete a comment by its ID
    public boolean deleteComment(int commentId) {
        // SQL query to delete a comment from the database
        String sql = "DELETE FROM comments WHERE ID = ?";
        try (Connection conn = DBUtil.getConnection(); // Get a database connection
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
            stmt.setInt(1, commentId); // Set the ID parameter
            return stmt.executeUpdate() > 0; // Execute the query and return true if successful
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace if an error occurs
        }
        return false; // Return false if the operation fails
    }
}
