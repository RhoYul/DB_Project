package Comments;

public class CommentsDTO {
    private int ID; // Comment ID
    private int POST_ID; // Post ID associated with the comment
    private int USER_ID; // User ID who made the comment
    private String COMMENT_TEXT; // The text content of the comment
    private String REGDATE; // The date the comment was registered
    private String USER_NAME; // The name of the user who made the comment

    // Constructor to initialize all fields
    public CommentsDTO(int ID, int POST_ID, int USER_ID, String COMMENT_TEXT, String REGDATE, String USER_NAME) {
        this.ID = ID;
        this.POST_ID = POST_ID;
        this.USER_ID = USER_ID;
        this.COMMENT_TEXT = COMMENT_TEXT;
        this.REGDATE = REGDATE;
        this.USER_NAME = USER_NAME;
    }

    // Getter and setter methods for each field
    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getPOST_ID() {
        return POST_ID;
    }

    public void setPOST_ID(int pOST_ID) {
        POST_ID = pOST_ID;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int uSER_ID) {
        USER_ID = uSER_ID;
    }

    public String getCOMMENT_TEXT() {
        return COMMENT_TEXT;
    }

    public void setCOMMENT_TEXT(String cOMMENT_TEXT) {
        COMMENT_TEXT = cOMMENT_TEXT;
    }

    public String getREGDATE() {
        return REGDATE;
    }

    public void setREGDATE(String rEGDATE) {
        REGDATE = rEGDATE;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String uSER_NAME) {
        USER_NAME = uSER_NAME;
    }
}
