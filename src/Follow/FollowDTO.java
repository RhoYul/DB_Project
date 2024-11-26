package Follow;

public class FollowDTO {
    
    private int ID; // Unique ID of the follow record
    private int FOLLOW_ID; // ID of the follower
    private int FOLLOWING_ID; // ID of the user being followed

    // Getter and setter methods for each field
    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getFOLLOW_ID() {
        return FOLLOW_ID;
    }

    public void setFOLLOW_ID(int fOLLOW_ID) {
        FOLLOW_ID = fOLLOW_ID;
    }

    public int getFOLLOWING_ID() {
        return FOLLOWING_ID;
    }

    public void setFOLLOWING_ID(int fOLLOWING_ID) {
        FOLLOWING_ID = fOLLOWING_ID;
    }
}
