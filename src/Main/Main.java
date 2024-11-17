package Main;

import User.UserDAO;

public class Main {
    public static void main(String[] args) {
        // UserDAO 객체 생성
        UserDAO userDAO = new UserDAO();

        // 사용자 정보 삽입
        String userId = "shdbf0813";
        String passwd = "12345!";
        String userName = "노율";
        String email = "shdbf0813@gmail.com";

        // insertUser 메서드 호출
        userDAO.insertUser(userId, passwd, userName, email);
    }
}
