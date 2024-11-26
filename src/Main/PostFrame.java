package Main;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Post.*;
import User.UserDAO;
import Comments.*;

public class PostFrame extends JFrame {
    private int likes; // Number of likes
    private int dislikes; // Number of dislikes
    private JPanel commentPanel; // Panel to display comments
    private JLabel dislikeLabel; // Label to display the number of dislikes
    private String authorName; // Author's name
    private String postContent; // Content of the post
    private PostDAO postDAO;
    private PostDTO post;
    private UserDAO userDAO;
    private CommentsDAO commentsDAO;
    private int userId; // Logged-in user's ID
    private int postId; // Current post ID
    private JButton likeButton; // Like button
    private JButton dislikeButton; // Dislike button
    private JLabel commentLabel; // Label for a single comment

    public PostFrame(int userId, int postId, String id, String postContent) {
        this.postContent = postContent; // Initialize post content
        this.postDAO = new PostDAO();
        this.userDAO = new UserDAO();
        this.commentsDAO = new CommentsDAO();
        this.postId = postId;
        this.userId = userId;
        authorName = userDAO.getUserNameLogId(userId);

        setTitle("Post Details");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel for post details
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical alignment
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Display author name (left-aligned)
        JLabel authorLabel = new JLabel("Posted by: " + id);
        authorLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14)); // Smaller font size
        authorLabel.setForeground(new Color(29, 161, 242));

        // Add author name to a left-aligned panel
        JPanel authorPanel = new JPanel();
        authorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        authorPanel.add(authorLabel);
        contentPanel.add(authorPanel);

        // Display post content (left-aligned)
        JLabel contentLabel = new JLabel("<html>" + postContent.replace("\n", "<br>") + "</html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        contentLabel.setForeground(Color.BLACK);

        // Add post content to a left-aligned panel
        JPanel contentTextPanel = new JPanel();
        contentTextPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        contentTextPanel.add(contentLabel);
        contentPanel.add(contentTextPanel);

        // Like and dislike buttons panel
        JPanel likeDislikePanel = new JPanel();
        likeDislikePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Like button
        likeButton = new JButton("Like [" + postDAO.getLikesCount(postId) + "]");
        likeButton.setFont(new Font("Arial", Font.BOLD, 16));
        likeButton.setBackground(Color.WHITE); // White background
        likeButton.setForeground(new Color(29, 161, 242)); // Twitter blue text
        likeButton.setFocusPainted(false); // Remove focus border
        likeButton.setBorder(BorderFactory.createLineBorder(new Color(29, 161, 242))); // Twitter blue border
        likeButton.setPreferredSize(new Dimension(140, 50)); // Increase button size
        likeButton.addActionListener(e -> {
            try {
                postDAO.toggleLike(postId, userId); // Toggle like
                // Fetch the latest data and update the UI
                int updatedLikes = postDAO.getLikesCount(postId);
                int updatedHates = postDAO.getHatesCount(postId);
                likeButton.setText("Like [" + updatedLikes + "]");
                dislikeButton.setText("Hate [" + updatedHates + "]");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Dislike button
        dislikeButton = new JButton("Hate [" + postDAO.getHatesCount(postId) + "]");
        dislikeButton.setFont(new Font("Arial", Font.BOLD, 16));
        dislikeButton.setBackground(new Color(29, 161, 242)); // Twitter blue background
        dislikeButton.setForeground(Color.WHITE); // White text
        dislikeButton.setFocusPainted(false); // Remove focus border
        dislikeButton.setBorder(BorderFactory.createLineBorder(new Color(29, 161, 242))); // Twitter blue border
        dislikeButton.setPreferredSize(new Dimension(140, 50)); // Increase button size
        dislikeButton.addActionListener(e -> {
            try {
                postDAO.toggleHate(postId, userId); // Toggle dislike
                // Fetch the latest data and update the UI
                int updatedLikes = postDAO.getLikesCount(postId);
                int updatedHates = postDAO.getHatesCount(postId);
                likeButton.setText("Like [" + updatedLikes + "]");
                dislikeButton.setText("Hate [" + updatedHates + "]");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        contentPanel.add(likeDislikePanel);
        add(contentPanel, BorderLayout.CENTER);
        likeDislikePanel.add(likeButton);
        likeDislikePanel.add(dislikeButton);

        // Comment input and display panel
        JPanel commentInputPanel = new JPanel();
        commentInputPanel.setLayout(new BorderLayout());

        // Increase comment input field size
        JTextField commentField = new JTextField();
        commentField.setFont(new Font("Arial", Font.PLAIN, 16));
        commentField.setPreferredSize(new Dimension(250, 50));

        // Add Comment button style and size
        JButton addCommentButton = new JButton("Add Comment");
        addCommentButton.setFont(new Font("Arial", Font.BOLD, 16));
        addCommentButton.setBackground(Color.WHITE); // White background
        addCommentButton.setForeground(new Color(29, 161, 242)); // Twitter blue text
        addCommentButton.setFocusPainted(false); // Remove focus border
        addCommentButton.setBorder(BorderFactory.createLineBorder(new Color(29, 161, 242))); // Twitter blue border
        addCommentButton.setPreferredSize(new Dimension(150, 50)); // Increase button size

        commentInputPanel.add(commentField, BorderLayout.CENTER);
        commentInputPanel.add(addCommentButton, BorderLayout.EAST);

        // Panel to display comments
        commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));

        // Load initial comments into the display panel
        loadComments();

        JScrollPane commentScrollPane = new JScrollPane(commentPanel);
        commentScrollPane.setPreferredSize(new Dimension(400, 250));

        // Set up the main layout
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.NORTH);
        add(commentInputPanel, BorderLayout.SOUTH);
        add(commentScrollPane, BorderLayout.CENTER);

        // Action for Add Comment button
        addCommentButton.addActionListener(e -> {
            String comment = commentField.getText().trim();
            commentLabel = new JLabel("<html><i>- " + authorName + ": " + comment + "</i></html>");
            commentLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
            if (!comment.isEmpty()) {
                commentsDAO.addComment(postId, userId, comment); // Add comment to the database

                // Reload comments from the database
                List<CommentsDTO> commentList = commentsDAO.getCommentsByPostId(postId);
                commentPanel.removeAll();

                if (commentList.isEmpty()) {
                    JLabel noCommentLabel = new JLabel("No comments available.");
                    noCommentLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    noCommentLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    commentPanel.add(noCommentLabel);
                } else {
                    for (CommentsDTO comm : commentList) {
                        JLabel commentLabel = new JLabel(
                                String.format("<html><i>- %s (%s): %s</i></html>",
                                        comm.getUSER_NAME(),
                                        comm.getREGDATE(),
                                        comm.getCOMMENT_TEXT())
                        );
                        commentLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
                        commentPanel.add(commentLabel);
                        commentPanel.add(Box.createVerticalStrut(10)); // Add space between comments
                    }
                }
                commentField.setText(""); // Clear input field
                commentPanel.revalidate();
                commentPanel.repaint();
            }
        });
    }

    // Load comments into the comment panel
    private void loadComments() {
        commentPanel.removeAll(); // Clear existing UI
        List<CommentsDTO> commentList = commentsDAO.getCommentsByPostId(postId);

        if (commentList.isEmpty()) {
            // Show a message when there are no comments
            JLabel noCommentLabel = new JLabel("No comments available.");
            noCommentLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noCommentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            commentPanel.add(noCommentLabel);
        } else {
            // Display the list of comments
            for (CommentsDTO comm : commentList) {
                JLabel commentLabel = new JLabel(
                        String.format("<html><i>- %s (%s): %s</i></html>",
                                comm.getUSER_NAME(),
                                comm.getREGDATE(),
                                comm.getCOMMENT_TEXT())
                );
                commentLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
                commentPanel.add(commentLabel);
                commentPanel.add(Box.createVerticalStrut(10)); // Add space between comments
            }
        }

        // Refresh the UI
        commentPanel.revalidate();
        commentPanel.repaint();
    }
}
