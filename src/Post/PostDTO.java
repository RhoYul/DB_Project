package Post;

public class PostDTO {
    private int id;
    private String loginId; // 로그인 ID (user.USER_ID)
    private String name;    // 게시글 작성자의 이름 (user.NAME)
    private String content; // 게시글 내용
    private int likes;      // 좋아요 수
    private int hates;      // 싫어요 수
    private String regDate; // 게시글 등록일
    private String updatedAt; // 게시글 수정일

    // Constructor
    public PostDTO(int id, String loginId, String name, String content, int likes, int hates, String regDate, String updatedAt) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.content = content;
        this.likes = likes;
        this.hates = hates;
        this.regDate = regDate;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getHates() {
        return hates;
    }

    public void setHates(int hates) {
        this.hates = hates;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}