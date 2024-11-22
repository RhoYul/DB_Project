package Main;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import Post.PostDAO;
import Post.PostDTO;

public class HomeFrame extends JFrame {
    private JPanel postPanel; // 게시글 표시 패널
    private JLabel noPostLabel; // 게시물이 없을 때 표시할 라벨
    private String username; // 사용자 이름
    private int userId; // 로그인한 사용자의 ID
    private PostDAO postDAO; // PostDAO 인스턴스

    public HomeFrame(int userId, String username) {
        this.userId = userId;
        this.username = username;
        this.postDAO = new PostDAO();

        setTitle("Home");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 검색 버튼 추가
        JButton searchButton = new JButton("search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 12));
        searchButton.setBounds(10, 10, 70, 30); // 위치와 크기 설정
        add(searchButton); // 프레임에 버튼 추가

        searchButton.addActionListener(e -> {
            new SearchFrame(userId); // SearchFrame 창 열기
        });

        // 사용자 버튼, 게시글 작성 버튼 패널 (하단)
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // 사용자 버튼
        JButton userButton = new JButton("User");
        userButton.setPreferredSize(new Dimension(250, 70));
        styleUserButton(userButton);

        // 게시글 작성 버튼
        JButton uploadButton = new JButton("+");
        uploadButton.setPreferredSize(new Dimension(250, 70));
        styleUploadButton(uploadButton);

        buttonPanel.add(userButton, BorderLayout.WEST);
        buttonPanel.add(uploadButton, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);

        // 상단 패널
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

        // 게시글 패널 초기화
        postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(postPanel);
        add(scrollPane, BorderLayout.CENTER);

        // 버튼 동작 정의
        userButton.addActionListener(e -> {
            UserProfileUI UserProfileUI = new UserProfileUI(userId, this); // 현재 사용자의 userId 전달
            UserProfileUI.setVisible(true);
        });

        uploadButton.addActionListener(e -> {
            UploadFrame uploadFrame = new UploadFrame(userId, this); // 현재 사용자의 userId 전달
            uploadFrame.setVisible(true);
        });

        sortByPopularityButton.addActionListener(e -> updatePostList("popularity"));
        sortByDateButton.addActionListener(e -> updatePostList("date"));

        // 초기 게시글 로드
        updatePostList("popularity");
    }

    // 게시글 목록 업데이트
    private void updatePostList(String sortBy) {
        postPanel.removeAll(); // 기존 게시글 제거

        try {
            // PostDAO를 사용하여 게시글 가져오기
            List<PostDTO> posts = postDAO.getPostsForUser(userId);

            // 정렬 기준에 따라 정렬
            posts.sort((p1, p2) -> {
                if ("popularity".equals(sortBy)) {
                    return Integer.compare(p2.getLikes(), p1.getLikes());
                } else {
                    return p2.getRegDate().compareTo(p1.getRegDate());
                }
            });

            // 게시글 추가
            if (posts.isEmpty()) {
                JLabel noPostLabel = new JLabel("No posts available.");
                noPostLabel.setFont(new Font("Arial", Font.BOLD, 16));
                noPostLabel.setHorizontalAlignment(SwingConstants.CENTER);
                postPanel.add(noPostLabel);
            } else {
                for (PostDTO post : posts) {
                    JPanel postItem = createPostItem(post);
                    postPanel.add(postItem);
                    postPanel.add(Box.createVerticalStrut(10)); // 게시글 간 간격 추가
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load posts.");
        }

        postPanel.revalidate();
        postPanel.repaint();
    }

    // 게시글 항목 생성
    private JPanel createPostItem(PostDTO post) {
    	// 게시물 컨테이너
    	JPanel postContainer = new JPanel();
    	postContainer.setLayout(new BoxLayout(postContainer, BoxLayout.Y_AXIS));
    	postContainer.setBorder(BorderFactory.createCompoundBorder(
    	        BorderFactory.createEmptyBorder(10, 10, 10, 10), // 상하좌우 여백 추가
    	        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1) // 테두리 두께를 1로 설정
    	    ));
    	postContainer.setBackground(Color.WHITE);

    	// 게시물 가로 크기 고정, 세로 크기는 제한하지 않음
    	postContainer.setMaximumSize(new Dimension(500, Integer.MAX_VALUE)); // 가로 고정, 세로는 제한 없음
    	postContainer.setAlignmentX(Component.LEFT_ALIGNMENT); // 부모 패널에서 왼쪽 정렬

    	// 상단 패널: 작성자 정보와 시간
    	JPanel headerPanel = new JPanel(new BorderLayout());
    	headerPanel.setOpaque(false); // 배경 투명
    	headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 부모 패널에서 왼쪽 정렬
    	headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 상단 간격

    	JLabel userInfoLabel = new JLabel(post.getLoginId() + " (" + post.getName() + ")");
    	userInfoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
    	userInfoLabel.setForeground(new Color(29, 161, 242)); // 작성자 이름 색상 지정

    	JLabel timestampLabel = new JLabel(post.getRegDate());
    	timestampLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    	timestampLabel.setForeground(Color.GRAY);

    	headerPanel.add(userInfoLabel, BorderLayout.WEST);
    	headerPanel.add(timestampLabel, BorderLayout.EAST);

    	// 본문 패널: 텍스트와 이미지
    	JPanel contentPanel = new JPanel();
    	contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    	contentPanel.setOpaque(false); // 배경 투명
    	contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 부모 패널에서 왼쪽 정렬
    	contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 본문 간격 추가

    	// 텍스트 내용: \n 줄바꿈 처리를 위해 <br>로 변환
    	String formattedContent = "<html><div style='width: 100%; text-align: left;'>" + post.getContent().replace("\n", "<br>") + "</div></html>";
    	JLabel contentLabel = new JLabel(formattedContent);
    	contentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    	contentLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 텍스트 간격
    	contentLabel.setForeground(Color.BLACK);
    	contentLabel.setHorizontalAlignment(SwingConstants.LEFT); // JLabel 내부 텍스트 왼쪽 정렬
    	contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // BoxLayout에서 왼쪽 정렬 보장

    	// 본문 패널에 JLabel 추가
    	contentPanel.add(contentLabel);

    	// 하단 패널: 좋아요/싫어요 버튼
    	JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	actionsPanel.setOpaque(false); // 배경 투명
    	actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 부모 패널에서 왼쪽 정렬
    	actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 하단 간격 추가

    	// 좋아요 버튼
    	JButton likeButton = new JButton("Like [" + post.getLikes() + "]");
    	likeButton.setFont(new Font("Arial", Font.PLAIN, 12));
    	likeButton.setBackground(new Color(29, 161, 242)); // 트위터 블루
    	likeButton.setForeground(Color.WHITE);
    	likeButton.setFocusPainted(false);
    	likeButton.addActionListener(e -> {
    	    try {
    	        postDAO.toggleLike(post.getId(), userId); // 좋아요 토글
    	        updatePostList("popularity"); // UI 업데이트
    	    } catch (SQLException ex) {
    	        ex.printStackTrace();
    	    }
    	});

    	// 싫어요 버튼
    	JButton dislikeButton = new JButton("Hate [" + post.getHates() + "]");
    	dislikeButton.setFont(new Font("Arial", Font.PLAIN, 12));
    	dislikeButton.setBackground(new Color(242, 29, 29)); // 빨간색
    	dislikeButton.setForeground(Color.WHITE);
    	dislikeButton.setFocusPainted(false);
    	dislikeButton.addActionListener(e -> {
    	    try {
    	        postDAO.toggleHate(post.getId(), userId); // 싫어요 토글
    	        updatePostList("popularity"); // UI 업데이트
    	    } catch (SQLException ex) {
    	        ex.printStackTrace();
    	    }
    	});

    	// 좋아요/싫어요 버튼 추가
    	actionsPanel.add(likeButton);
    	actionsPanel.add(dislikeButton);

    	// 게시글 레이아웃 구성
    	postContainer.add(headerPanel); // 작성자 정보와 시간
    	postContainer.add(contentPanel); // 게시글 내용
    	postContainer.add(actionsPanel); // 좋아요/싫어요 버튼

    	return postContainer;
    }

    public void initializeUI() {
        postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS)); // 수직 정렬
        postPanel.setBackground(new Color(245, 245, 245)); // 트위터 스타일 배경색

        JScrollPane scrollPane = new JScrollPane(postPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 가로 스크롤 비활성화

        add(scrollPane, BorderLayout.CENTER); // 스크롤 패널 추가
    }

    // 사용자 버튼 스타일 정의
    private void styleUserButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(29, 161, 242));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }

    // 게시물 작성 버튼 스타일 정의
    private void styleUploadButton(JButton button) {
        button.setBackground(new Color(29, 161, 242));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 30));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(29, 161, 242), 1));
    }
}
