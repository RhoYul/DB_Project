package Notification;

public class NotificationDTO {
    private int id;           // 알림 ID
    private int userId;       // 알림 받을 사용자 ID
    private String noticeType; // 알림 유형 (e.g., FOLLOW, LIKE, COMMENT)
    private int postId;       // 게시글 ID (해당 알림과 관련된 게시글)
    private String message;   // 알림 메시지
    private boolean isRead;   // 읽음 여부

    // Constructor
    public NotificationDTO(int id, int userId, String noticeType, int postId, String message, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.noticeType = noticeType;
        this.postId = postId;
        this.message = message;
        this.isRead = isRead;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", noticeType='" + noticeType + '\'' +
                ", postId=" + postId +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}

