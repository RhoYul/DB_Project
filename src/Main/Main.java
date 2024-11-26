package Main;

import User.UserDAO;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // Launch LoginFrame
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(userDAO);
            loginFrame.setVisible(true);
        });
    }
}
