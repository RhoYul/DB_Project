package Main;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import Post.PostDAO;
import Post.PostDTO;

public class HomeFrame extends JFrame {
    private JPanel postPanel; // Panel to display posts
    private JLabel noPostLabel; // Label to display when there are no posts
    private int userId; // Logged-in user's ID
    private PostDAO postDAO; // Instance of PostDAO
    private String currentSortBy = "date"; // Initial sorting criterion

    public HomeFrame(int userId) {
        this.userId = userId;
        this.postDAO = new PostDAO();

        setTitle("Home");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add search button
        JButton searchButton = new JButton("search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 12));
        searchButton.setBounds(10, 10, 70, 30); // Set position and size
        add(searchButton); // Add button to the frame

        searchButton.addActionListener(e -> {
            new SearchFrame(userId).setVisible(true); // Open the SearchFrame
        });

        // User button and post upload button panel (bottom)
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // User button
        JButton userButton = new JButton("User");
        userButton.setPreferredSize(new Dimension(250, 70));
        styleUserButton(userButton);

        // Post upload button
        JButton uploadButton = new JButton("+");
        uploadButton.setPreferredSize(new Dimension(250, 70));
        styleUploadButton(uploadButton);

        buttonPanel.add(userButton, BorderLayout.WEST);
        buttonPanel.add(uploadButton, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 225, 10));
        JLabel logoLabel = new JLabel("X");
        logoPanel.setBackground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.BLACK);
        logoPanel.add(logoLabel);
        headerPanel.add(logoPanel, BorderLayout.WEST);

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JRadioButton sortByPopularityButton = new JRadioButton("Popularity");
        JRadioButton sortByDateButton = new JRadioButton("Date");

        ButtonGroup sortButtonGroup = new ButtonGroup();
        sortButtonGroup.add(sortByPopularityButton);
        sortButtonGroup.add(sortByDateButton);

        sortByPopularityButton.setSelected(true);

        sortPanel.add(sortByPopularityButton);
        sortPanel.add(sortByDateButton);
        headerPanel.add(sortPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Initialize the post panel
        postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(postPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Define button actions
        userButton.addActionListener(e -> {
            UserProfileUI UserProfileUI = new UserProfileUI(userId, this); // Pass the current user's userId
            UserProfileUI.setVisible(true);
        });

        uploadButton.addActionListener(e -> {
            UploadFrame uploadFrame = new UploadFrame(userId, this); // Pass the current user's userId
            uploadFrame.setVisible(true);
        });
        
        sortByPopularityButton.addActionListener(e -> {
            currentSortBy = "popularity";
            updatePostList(currentSortBy);
        });
        sortByDateButton.addActionListener(e -> {
            currentSortBy = "date";
            updatePostList(currentSortBy);
        });

        // Load initial posts
        updatePostList("date");
    }

    // Update the post list
    private void updatePostList(String sortBy) {
        postPanel.removeAll(); // Remove existing posts

        try {
            // Use PostDAO to fetch posts
            List<PostDTO> posts = postDAO.getPostsForUser(userId);

            // Sort posts based on the sorting criterion
            posts.sort((p1, p2) -> {
                if ("popularity".equals(sortBy)) {
                    return Integer.compare(p2.getLikes(), p1.getLikes());
                } else {
                    return p2.getRegDate().compareTo(p1.getRegDate());
                }
            });

            // Add posts to the panel
            if (posts.isEmpty()) {
                JLabel noPostLabel = new JLabel("No posts available.");
                noPostLabel.setFont(new Font("Arial", Font.BOLD, 16));
                noPostLabel.setHorizontalAlignment(SwingConstants.CENTER);
                postPanel.add(noPostLabel);
            } else {
                for (PostDTO post : posts) {
                    JPanel postItem = createPostItem(post);
                    postPanel.add(postItem);
                    postPanel.add(Box.createVerticalStrut(10)); // Add space between posts
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load posts.");
        }

        postPanel.revalidate();
        postPanel.repaint();
    }

    // Create a post item panel
    private JPanel createPostItem(PostDTO post) {
    	// Post container
    	JPanel postContainer = new JPanel();
    	postContainer.setLayout(new BoxLayout(postContainer, BoxLayout.Y_AXIS));
    	postContainer.setBorder(BorderFactory.createCompoundBorder(
    	        BorderFactory.createEmptyBorder(10, 10, 10, 10), // Add padding
    	        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1) // Set border thickness to 1
    	    ));
    	postContainer.setBackground(Color.WHITE);

    	// Fix horizontal size, no limit on vertical size
    	postContainer.setMaximumSize(new Dimension(500, Integer.MAX_VALUE)); // Fixed width, no height limit
    	postContainer.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left in the parent panel

    	// Header panel: author info and timestamp
    	JPanel headerPanel = new JPanel(new BorderLayout());
    	headerPanel.setOpaque(false); // Transparent background
    	headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left in the parent panel
    	headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add top margin

    	JLabel userInfoLabel = new JLabel(post.getLoginId() + " (" + post.getName() + ")");
    	userInfoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
    	userInfoLabel.setForeground(new Color(29, 161, 242)); // Set author name color

    	JLabel timestampLabel = new JLabel(post.getRegDate());
    	timestampLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    	timestampLabel.setForeground(Color.GRAY);

    	headerPanel.add(userInfoLabel, BorderLayout.WEST);
    	headerPanel.add(timestampLabel, BorderLayout.EAST);

    	// Content panel: text and image
    	JPanel contentPanel = new JPanel();
    	contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    	contentPanel.setOpaque(false); // Transparent background
    	contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left in the parent panel
    	contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add content margin

    	// Post content: process \n for line breaks
    	String formattedContent = "<html><div style='width: 100%; text-align: left;'>" + post.getContent().replace("\n", "<br>") + "</div></html>";
    	JLabel contentLabel = new JLabel(formattedContent);
    	contentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    	contentLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add text padding
    	contentLabel.setForeground(Color.BLACK);
    	contentLabel.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left within JLabel
    	contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Ensure left alignment in BoxLayout

    	// Add JLabel to content panel
    	contentPanel.add(contentLabel);

    	// Actions panel: like/dislike buttons
    	JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	actionsPanel.setOpaque(false); // Transparent background
    	actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left in the parent panel
    	actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Add bottom margin

    	// Like button
    	JButton likeButton = new JButton("Like [" + postDAO.getLikesCount(post.getId()) + "]");
    	likeButton.setFont(new Font("Arial", Font.PLAIN, 12));
    	likeButton.setBackground(new Color(29, 161, 242)); // Twitter blue
    	likeButton.setForeground(Color.WHITE);
    	likeButton.setFocusPainted(false);
    	likeButton.addActionListener(e -> {
    	    try {
    	        postDAO.toggleLike(post.getId(), userId); // Toggle like
    	        updatePostList("date"); // Update UI
    	    } catch (SQLException ex) {
    	        ex.printStackTrace();
    	    }
    	});

    	// Dislike button
    	JButton dislikeButton = new JButton("Hate [" + postDAO.getHatesCount(post.getId()) + "]");
    	dislikeButton.setFont(new Font("Arial", Font.PLAIN, 12));
    	dislikeButton.setBackground(new Color(242, 29, 29)); // Red
    	dislikeButton.setForeground(Color.WHITE);
    	dislikeButton.setFocusPainted(false);
    	dislikeButton.addActionListener(e -> {
    	    try {
    	        postDAO.toggleHate(post.getId(), userId); // Toggle dislike
    	        updatePostList("date"); // Update UI
    	    } catch (SQLException ex) {
    	        ex.printStackTrace();
    	    }
    	});

    	// Add like/dislike buttons
    	actionsPanel.add(likeButton);
    	actionsPanel.add(dislikeButton);

    	// Assemble post layout
    	postContainer.add(userInfoLabel);
    	postContainer.add(headerPanel); // Author info and timestamp
    	postContainer.add(contentPanel); // Post content
    	postContainer.add(actionsPanel); // Like/dislike buttons

    	postContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Open PostFrame and pass post details
                new PostFrame(userId, post.getId(), post.getName(), post.getContent()).setVisible(true);
            }
        });
    	
    	return postContainer;
    }

    public void initializeUI() {
        postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS)); // Vertical alignment
        postPanel.setBackground(new Color(245, 245, 245)); // Twitter-style background color

        JScrollPane scrollPane = new JScrollPane(postPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Set scroll speed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling

        add(scrollPane, BorderLayout.CENTER); // Add scroll panel
    }

    // Define user button style
    private void styleUserButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(29, 161, 242));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }

    // Define post upload button style
    private void styleUploadButton(JButton button) {
        button.setBackground(new Color(29, 161, 242));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 30));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(29, 161, 242), 1));
    }
}
