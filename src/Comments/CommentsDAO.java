package Comments;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Database.DBUtil;
import Post.PostDTO;

public class CommentsDAO {

    // 댓글 추가
    public boolean addComment(int postId, int userId, String commentText) {
        String sql = "INSERT INTO comments (POST_ID, USER_ID, COMMENT_TEXT, REGDATE) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBUtil.getConnection();
        		PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setString(3, commentText);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 특정 게시글의 댓글 가져오기
    public List<CommentsDTO> getCommentsByPostId(int postId) {
        List<CommentsDTO> comments = new ArrayList<>();
        String sql = "SELECT C.*,U.USER_NAME FROM COMMENTS C INNER JOIN USERS U ON C.USER_ID = U.ID ORDER BY c.ID desc";
        try (Connection conn = DBUtil.getConnection();
        		PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(new CommentsDTO(
                            rs.getInt("ID"),
                            rs.getInt("POST_ID"),
                            rs.getInt("USER_ID"),
                            rs.getString("COMMENT_TEXT"),
                            rs.getString("REGDATE"),
                            rs.getString("USER_NAME")
                        ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    // 댓글 삭제
    public boolean deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE ID = ?";
        try (Connection conn = DBUtil.getConnection();
        		PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
