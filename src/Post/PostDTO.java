package Post;

// Data Transfer Object for the "Post" entity
public class PostDTO {
    private int id; // Post ID
    private String loginId; // Login ID of the user who created the post
    private String name; // Name of the user who created the post
    private String content; // Content of the post
    private int likes; // Number of likes
    private int hates; // Number of hates
    private String regDate; // Post creation date
    private String updatedAt; // Post last updated date

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
