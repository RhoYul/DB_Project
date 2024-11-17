package User;

import Database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    // 예제: 사용자 정보 조회 메서드
	public void insertUser(String userId, String passwd, String userName, String email) {
	    String query = "INSERT INTO Users (USER_ID, PASSWD, USER_NAME, EMAIL) VALUES (?, ?, ?, ?)";

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
	
}
