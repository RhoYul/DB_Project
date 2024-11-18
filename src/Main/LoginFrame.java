package Main;

import User.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private UserDAO userDAO;

    public LoginFrame(UserDAO userDAO) {
        this.userDAO = userDAO;
        setTitle("로그인");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel userIdLabel = new JLabel("아이디:");
        JTextField userIdField = new JTextField();
        JLabel passwdLabel = new JLabel("비밀번호:");
        JPasswordField passwdField = new JPasswordField();
        JButton loginButton = new JButton("로그인");
        JButton registerButton = new JButton("회원가입");

        panel.add(userIdLabel);
        panel.add(userIdField);
        panel.add(passwdLabel);
        panel.add(passwdField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String passwd = new String(passwdField.getPassword());

                boolean authenticated = userDAO.authenticateUser(userId, passwd);

                if (authenticated) {
                    JOptionPane.showMessageDialog(null, "로그인 성공!");
                } else {
                    JOptionPane.showMessageDialog(null, "로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame(userDAO).setVisible(true);
            }
        });
    }
}
