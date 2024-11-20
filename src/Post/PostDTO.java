package Post;

public class PostDTO {
	
	private int ID;
	private String USER_ID;
	private String TITLE;
	private String CONTENT;
	private String REGDATE;
	private String UPDATED_AT;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
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
	
}
