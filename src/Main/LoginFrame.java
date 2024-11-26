package Main;

import User.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private UserDAO userDAO; // UserDAO instance for user-related database operations
    int authorId; // Author ID for the logged-in user
    
    public LoginFrame(UserDAO userDAO) {
        this.userDAO = userDAO;

        // Frame settings
        setTitle("Twitter Login");
        setSize(350, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        // Top logo panel
        JPanel logoPanel = new JPanel();
        JLabel logoLabel = new JLabel("X");
        logoPanel.setBackground(Color.BLACK);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);
        add(logoPanel, BorderLayout.NORTH);

        // Center panel for login input fields
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // ID label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(Color.WHITE);
        panel.add(idLabel, gbc);

        // ID field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        // Password label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel, gbc);

        // Password field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, Color.WHITE, Color.BLACK, Color.WHITE);
        panel.add(loginButton, gbc);

        // Signup button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton signupButton = new JButton("Sign Up");
        styleButton(signupButton, new Color(29, 161, 242), Color.BLACK, Color.WHITE);
        panel.add(signupButton, gbc);

        add(panel, BorderLayout.CENTER);

        // Bottom panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.BLACK);
        JLabel footerLabel = new JLabel("© 2024 Twitter");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        // Action for login button
        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());

            if (id.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both ID and password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (userDAO.authenticateUser(id, password)) {
                authorId = userDAO.getUserId(id);
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                openHomeFrame(id); // Move to HomeFrame after successful login
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action for signup button
        signupButton.addActionListener(e -> new RegisterFrame(userDAO).setVisible(true));
    }

    // Open HomeFrame
    private void openHomeFrame(String username) {
        HomeFrame homeFrame = new HomeFrame(authorId);
        homeFrame.setVisible(true);
        dispose(); // Close LoginFrame
    }

    // Style the buttons
    private void styleButton(JButton button, Color textColor, Color backgroundColor, Color borderColor) {
        button.setForeground(textColor);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        SwingUtilities.invokeLater(() -> new LoginFrame(userDAO).setVisible(true));
    }
}
