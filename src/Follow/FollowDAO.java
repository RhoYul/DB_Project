package Follow;

import java.sql.*;
import Database.DBUtil;

public class FollowDAO {
	
	public boolean followUser(int followerId, int followingId) {
        String query = "INSERT INTO follow (FOLLOW_ID, FOLLOWING_ID) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, followerId); // 팔로워 ID
            pstmt.setInt(2, followingId); // 팔로잉되는 사람 ID
            pstmt.executeUpdate();
            return true; // 성공 시 true 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 실패 시 false 반환
        }
    }
}
