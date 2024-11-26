package Main;

import User.UserDAO;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private UserDAO userDAO; // UserDAO instance for database operations

    public RegisterFrame(UserDAO userDAO) {
        this.userDAO = userDAO;

        // Set up the JFrame
        setTitle("Twitter Sign Up");
        setSize(400, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        Font boldFont = new Font("Arial", Font.BOLD, 14);
        Color twitterBlue = new Color(29, 161, 242);

        // Logo panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBounds(0, 0, 400, 60);
        logoPanel.setBackground(Color.BLACK);
        JLabel logoLabel = new JLabel("X");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);
        add(logoPanel);

        // Name input field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 70, 100, 30);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(boldFont);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(130, 70, 150, 30);
        add(nameField);

        // Email input field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 120, 100, 30);
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(boldFont);
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(130, 120, 150, 30);
        add(emailField);

        // User ID input field
        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(50, 190, 100, 30);
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(boldFont);
        add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(130, 190, 150, 30);
        add(idField);

        // Password input field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 260, 100, 30);
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(boldFont);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(130, 260, 150, 30);
        add(passwordField);

        // Sign Up button
        JButton signupButton = new JButton("Sign Up");
        styleButton(signupButton, Color.BLACK, Color.WHITE, twitterBlue, boldFont);
        signupButton.setBounds(100, 330, 200, 50);
        add(signupButton);

        // Action for Sign Up button
        signupButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String userId = idField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || userId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check for duplicate user ID or email
            if (isDuplicate(userId, email)) {
                return; // Exit if duplicate found
            }

            // Save user information to the database
            userDAO.insertUser(userId, password, name, email);
            JOptionPane.showMessageDialog(this, "Sign Up Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the registration frame
        });
    }

    // Check for duplicate user ID or email
    private boolean isDuplicate(String userId, String email) {
        if (userDAO.authenticateUser(userId, email)) { // Email duplicate check
            JOptionPane.showMessageDialog(this, "This email or ID is already in use.", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    // Style the buttons
    private static void styleButton(JButton button, Color bgColor, Color borderColor, Color textColor, Font font) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 1));
    }
}
