package User;

// Data Transfer Object for the "User" entity
public class UserDTO {

    private int ID; // User ID
    private String USER_ID; // Login ID
    private String PASSWD; // Password
    private String USER_NAME; // User's name
    private String EMAIL; // User's email
    private String REGDATE; // Registration date

    // Getters and Setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getPASSWD() {
        return PASSWD;
    }

    public void setPASSWD(String PASSWD) {
        this.PASSWD = PASSWD;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getREGDATE() {
        return REGDATE;
    }

    public void setREGDATE(String REGDATE) {
        this.REGDATE = REGDATE;
    }
}
