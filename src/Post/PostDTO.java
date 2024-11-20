package Post;

public class PostDTO {
	
	private int ID;
	private int USER_ID;
	private String CONTENT;
	private int LIKES;
	
	private int HATES;
	private String REGDATE;
	private String UPDATED_AT;
	
	public PostDTO(int ID, int USER_ID, String CONTENT, int LIKES,int HATES, String REGDATE, String UPDATED_AT) {
		this.ID = ID;
		this.USER_ID = USER_ID;
		this.CONTENT = CONTENT;
		this.LIKES = LIKES;
		this.HATES = HATES;
		this.REGDATE = REGDATE;
		this.UPDATED_AT = UPDATED_AT;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(int uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	public String getREGDATE() {
		return REGDATE;
	}
	public void setREGDATE(String rEGDATE) {
		REGDATE = rEGDATE;
	}
	public String getUPDATED_AT() {
		return UPDATED_AT;
	}
	public void setUPDATED_AT(String uPDATED_AT) {
		UPDATED_AT = uPDATED_AT;
	}
	public int getLIKES() {
		return LIKES;
	}
	public void setLIKES(int lIKES) {
		LIKES = lIKES;
	}
	public int getHATES() {
		return HATES;
	}

	public void setHATES(int hATES) {
		HATES = hATES;
	}
}
