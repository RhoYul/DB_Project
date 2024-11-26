package Main;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import Post.PostDAO;
import Post.PostDTO;
import User.UserDAO;

public class OtherUserProfileUI extends JFrame {
    private CardLayout cardLayout; // CardLayout to switch between views
    private JPanel mainPanel; // Main panel containing cards

    public OtherUserProfileUI(int userId) {
        setTitle("User Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 700);
        setLayout(new BorderLayout());

        // Panel displaying user information (ID and name)
        JPanel userInfoPanel = createOtherUserInfoPanel(userId);
        add(userInfoPanel, BorderLayout.NORTH);

        // Card layout panel
        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(createPostPanel(userId), "Post"); // Post card

        cardLayout = (CardLayout) mainPanel.getLayout();
        add(mainPanel, BorderLayout.CENTER);
    }

    // Create panel to display user information (ID and name)
    private JPanel createOtherUserInfoPanel(int userId) {
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        UserDAO userDAO = new UserDAO();
        String userName = userDAO.getUserNameLogId(userId); // Fetch user name
        String userLoginId = userDAO.getUserLogId(userId); // Fetch user login ID

        JLabel userLabel = new JLabel(userLoginId + " (" + userName + ")");
        userLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        userLabel.setForeground(new Color(29, 161, 242));

        userInfoPanel.add(userLabel);
        return userInfoPanel;
    }

    // Create panel displaying posts of a specific user
    private JPanel createPostPanel(int userId) {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(postPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        PostDAO postDAO = new PostDAO();
        try {
            List<PostDTO> posts = postDAO.getPostsByUser(userId); // Fetch posts of the user
            if (posts.isEmpty()) {
                JLabel noPostLabel = new JLabel("No posts available.");
                noPostLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
                noPostLabel.setHorizontalAlignment(SwingConstants.CENTER);
                postPanel.add(noPostLabel);
            } else {
                for (PostDTO post : posts) {
                    JPanel postItem = createPostItem(post);
                    postItem.setAlignmentX(Component.CENTER_ALIGNMENT); // Align to center
                    postPanel.add(postItem);
                    postPanel.add(Box.createVerticalStrut(15)); // Add space between posts
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading posts.");
        }

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        return containerPanel;
    }

    // Create a panel for individual post items
    private JPanel createPostItem(PostDTO post) {
        JPanel postItem = new JPanel();
        postItem.setLayout(new BoxLayout(postItem, BoxLayout.Y_AXIS));
        postItem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
        ));
        postItem.setBackground(Color.WHITE);
        postItem.setMaximumSize(new Dimension(400, 150)); // Fix size of post panel

        // Top panel: author info and timestamp
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel userInfoLabel = new JLabel("<html><b style='color: #1DA1F2;'>" + post.getLoginId() + " (" + post.getName() + ")</b></html>");
        userInfoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        JLabel timestampLabel = new JLabel(post.getRegDate());
        timestampLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timestampLabel.setForeground(Color.GRAY);

        headerPanel.add(userInfoLabel, BorderLayout.WEST);
        headerPanel.add(timestampLabel, BorderLayout.EAST);

        // Content panel: text content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel contentLabel = new JLabel("<html><div style='width: 100%; text-align: left;'>"
                + post.getContent().replace("\n", "<br>") + "</div></html>");
        contentLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        contentLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        contentPanel.add(contentLabel);

        // Bottom panel: like/dislike buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setOpaque(false);

        JButton likeButton = new JButton("Like [" + post.getLikes() + "]");
        likeButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        likeButton.setBackground(new Color(29, 161, 242));
        likeButton.setForeground(Color.WHITE);

        JButton dislikeButton = new JButton("Hate [" + post.getHates() + "]");
        dislikeButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        dislikeButton.setBackground(new Color(242, 29, 29));
        dislikeButton.setForeground(Color.WHITE);

        actionsPanel.add(likeButton);
        actionsPanel.add(dislikeButton);

        // Assemble post layout
        postItem.add(headerPanel);
        postItem.add(contentPanel);
        postItem.add(actionsPanel);

        return postItem;
    }
}
