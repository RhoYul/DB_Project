package Follow;

import java.sql.*;
import Database.DBUtil;

public class FollowDAO {

    // 팔로우/언팔로우 토글
    public boolean toggleFollow(int followerId, int followingId) {
        if (isFollowing(followerId, followingId)) {
            return unfollowUser(followerId, followingId); // 이미 팔로우 중이면 언팔로우
        } else {
            return followUser(followerId, followingId); // 팔로우하지 않았으면 팔로우
        }
    }

    // 팔로우 추가
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

    // 팔로우 삭제 (언팔로우)
    public boolean unfollowUser(int followerId, int followingId) {
        String query = "DELETE FROM follow WHERE FOLLOW_ID = ? AND FOLLOWING_ID = ?";
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

    // 특정 사용자가 다른 사용자를 팔로우하고 있는지 확인
    public boolean isFollowing(int followerId, int followingId) {
        String query = "SELECT COUNT(*) FROM follow WHERE FOLLOW_ID = ? AND FOLLOWING_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, followerId); // 팔로워 ID
            pstmt.setInt(2, followingId); // 팔로잉되는 사람 ID
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // 팔로우 관계가 존재하면 true 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 기본적으로 false 반환
    }

    // 특정 사용자의 팔로워 수 반환
    public int getFollowerCount(int userId) {
        String query = "SELECT COUNT(*) FROM follow WHERE FOLLOWING_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, userId); // 특정 사용자의 ID
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // 팔로워 수 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // 기본적으로 0 반환
    }

    // 특정 사용자의 팔로잉 수 반환
    public int getFollowingCount(int userId) {
        String query = "SELECT COUNT(*) FROM follow WHERE FOLLOW_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, userId); // 특정 사용자의 ID
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // 팔로잉 수 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // 기본적으로 0 반환
    }
}
