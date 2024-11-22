package User;

import Database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // 회원가입 시 입력받은 데이터 데이터베이스에 insert하는 query문
	public void insertUser(String userId, String passwd, String userName, String email) {
	    String query = "INSERT INTO Users (USER_ID, PASSWD, USER_NAME, EMAIL) VALUES (?, ?, ?, ?)"; // query

	    try (Connection conn = DBUtil.getConnection(); // DB 연결
	         PreparedStatement pstmt = conn.prepareStatement(query)) { // 값을 받아와서 query문을 실행

	        pstmt.setString(1, userId); // 값 입력
	        pstmt.setString(2, passwd);
	        pstmt.setString(3, userName);
	        pstmt.setString(4, email);

	        int rowsInserted = pstmt.executeUpdate(); // DB 반영(Update)
	        if (rowsInserted > 0) { // Update된 항목이 1개 이상이면 insert successfully
	            System.out.println("User inserted successfully!");
	        }

	    } catch (SQLException ex) {
	        System.out.println("Error inserting user: " + ex.getMessage());
	        ex.printStackTrace();
	    }
	}
	
	public int getUserId(String userId) {
	    String query = "SELECT ID FROM Users WHERE USER_ID = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setString(1, userId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("ID"); // ID 반환
	            }
	        }
	    } catch (SQLException ex) {
	        System.out.println("Error fetching user ID: " + ex.getMessage());
	        ex.printStackTrace();
	    }
	    return -1; // ID가 없을 경우 -1 반환
	}

	  // 데이터베이스에 존재하는 user 인증하는 query문
    public boolean authenticateUser(String userId, String passwd) {
        String query = "SELECT * FROM Users WHERE USER_ID = ? AND PASSWD = ?"; // query

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, passwd);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // 결과가 있으면 true
            }

        } catch (SQLException ex) {
            System.out.println("Wrong ID/PASS : " + ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }
    
    public String searchUserById(String userId) {
        String query = "SELECT USER_NAME FROM Users WHERE USER_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, userId); // 검색할 ID 설정
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String userName = rs.getString("USER_NAME");
                return userId + " " + userName;
            } else {
                return null; // 사용자 없음
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
