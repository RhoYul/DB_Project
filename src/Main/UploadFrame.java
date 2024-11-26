package Main;

import javax.swing.*;
import java.awt.*;
import Post.PostDAO;
import java.sql.*;

public class UploadFrame extends JFrame {
    private HomeFrame homeFrame; // Reference to the HomeFrame
    private int authorId; // Author's ID
    private PostDAO postDAO; // DAO for handling post operations

    public UploadFrame(int userId, HomeFrame homeFrame) {
        this.homeFrame = homeFrame;
        this.postDAO = new PostDAO();

        setTitle("Upload Post");
        setSize(500, 400);
        setLocationRelativeTo(null); // Center the window on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel setup
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Top panel for heading
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center alignment
        JLabel topLabel = new JLabel("What's going on?");
        topLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Bold and larger font
        topLabel.setForeground(Color.BLACK); // Black text color
        topPanel.add(topLabel);
        panel.add(topPanel, BorderLayout.NORTH); // Add to the top of the main panel

        // Text area for writing the post
        JTextArea postContentArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(postContentArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // Two buttons side-by-side

        // Image upload button
        JButton imageUploadButton = new JButton("Image Upload");
        styleButton(imageUploadButton);

        // Upload button
        JButton uploadButton = new JButton("Upload");
        styleButton(uploadButton);

        buttonPanel.add(imageUploadButton);
        buttonPanel.add(uploadButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Action listener for image upload button (simple message for now)
        imageUploadButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Image Upload clicked!"));

        // Action listener for upload button
        uploadButton.addActionListener(e -> {
            this.authorId = userId; // Set the current user's ID
            String content = postContentArea.getText();

            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Content must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                // Create a new post and get the POST_ID
                int postId = postDAO.createPost(authorId, content);
                if (postId != -1) {
                    JOptionPane.showMessageDialog(this, "Post uploaded (ID: " + postId + ")", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the frame
                } else {
                    JOptionPane.showMessageDialog(this, "Post creation failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Method to style buttons
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(29, 161, 242));
        button.setFocusPainted(false);
    }
}
