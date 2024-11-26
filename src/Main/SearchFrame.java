package Main;

import javax.swing.*;
import java.awt.*;
import User.UserDAO;
import Follow.FollowDAO;

public class SearchFrame extends JFrame {

    private JPanel resultPanel; // Panel to display search results
    private int userId; // Logged-in user's ID

    public SearchFrame(int userId) {
        this.userId = userId; // Pass the logged-in user's ID
        setTitle("Search User");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close the current frame
        setLayout(new BorderLayout());

        // Search input field and button
        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Enter User ID:");
        JTextField textField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        searchPanel.add(label);
        searchPanel.add(textField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Initialize the result panel for displaying search results
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        add(resultPanel, BorderLayout.CENTER);

        // Action for the search button
        searchButton.addActionListener(e -> {
            String searchQuery = textField.getText().trim();
            if (!searchQuery.isEmpty()) {
                displayUserInfo(searchQuery);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.");
            }
        });

        setVisible(true);
    }

    // Display user information based on search query
    private void displayUserInfo(String searchedUserId) {
        UserDAO userDAO = new UserDAO();
        FollowDAO followDAO = new FollowDAO();

        // Fetch the searched user's information
        String userInfo = userDAO.searchUserById(searchedUserId);

        // Clear the result panel
        resultPanel.removeAll();

        if (userInfo != null) {
            // Display user information
            JLabel userInfoLabel = new JLabel(userInfo);
            userInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            resultPanel.add(userInfoLabel);

            // Add Follow button
            JButton followButton = new JButton("Follow");
            followButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            followButton.addActionListener(e -> {
                int currentUserId = userId; // Current logged-in user's ID
                int targetUserId = userDAO.getUserId(searchedUserId); // ID of the searched user

                // Validate IDs
                if (currentUserId == -1 || targetUserId == -1) {
                    JOptionPane.showMessageDialog(this, "Invalid user IDs. Cannot follow.");
                    return;
                }

                // Perform follow action
                boolean success = followDAO.toggleFollow(currentUserId, targetUserId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "You are now following this user!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to follow. Please try again.");
                }
            });

            // Add Info button
            JButton infoButton = new JButton("Info");
            infoButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            infoButton.addActionListener(e -> {
                int targetUserId = userDAO.getUserId(searchedUserId); // ID of the searched user

                if (targetUserId == -1) {
                    JOptionPane.showMessageDialog(this, "User not found. Cannot open profile.");
                } else {
                    // Open OtherUserProfileUI for the searched user
                    new OtherUserProfileUI(targetUserId).setVisible(true);
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(followButton);
            buttonPanel.add(infoButton);

            resultPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing
            resultPanel.add(buttonPanel);

        } else {
            // If user information is not found
            JLabel notFoundLabel = new JLabel("User not found.");
            notFoundLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            resultPanel.add(notFoundLabel);
        }

        // Refresh the panel
        resultPanel.revalidate();
        resultPanel.repaint();
    }
}
