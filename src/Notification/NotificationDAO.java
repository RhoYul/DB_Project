package Notification;


import Database.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    // Create a new notification
    public void createNotification(int userId, String noticeType, int postId, String message) throws SQLException {
        String query = "INSERT INTO notification (USER_ID, NOTICE_TYPE, POST_ID, MESSAGE, READ_YN) VALUES (?, ?, ?, ?, 'N')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, noticeType);
            pstmt.setInt(3, postId);
            pstmt.setString(4, message);
            pstmt.executeUpdate();
        }
    }

    // Get unread notifications for a user
    public List<NotificationDTO> getUnreadNotifications(int userId) throws SQLException {
        String query = "SELECT * FROM notification WHERE USER_ID = ? AND READ_YN = 'N' ORDER BY ID DESC";
        List<NotificationDTO> notifications = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new NotificationDTO(
                            rs.getInt("ID"),
                            rs.getInt("USER_ID"),
                            rs.getString("NOTICE_TYPE"),
                            rs.getInt("POST_ID"),
                            rs.getString("MESSAGE"),
                            rs.getString("READ_YN").equals("Y")
                    ));
                }
            }
        }
        return notifications;
    }
    
    // Mark a specific notification as read
    public void markNotificationAsRead(int notificationId) throws SQLException {
        String query = "UPDATE notification SET READ_YN = 'Y' WHERE ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, notificationId);
            pstmt.executeUpdate();
        }
    }

    // Mark all notifications as read for a user
    public void markAllAsRead(int userId) throws SQLException {
        String query = "UPDATE notification SET READ_YN = 'Y' WHERE USER_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
}
