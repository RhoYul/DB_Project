//로그아웃 버튼 눌렀을 때 로그인 화면으로 넘어가게 구현, 하단 우측 게시글 업로드 버튼(=원형버튼) 눌렀을 때 게시글 작성되게 구현, 하단 좌측 알림 버튼 눌렀을 때 알림 목록 뜨게 구현부탁드립니다.

//사용자(본인)프로필 화면 
package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Follow.*;
import User.*;

public class UserProfileUI extends JFrame {
    private CardLayout cardLayout;
    private HomeFrame homeFrame;
    private JPanel mainPanel;
    private JButton popularPostsButton, favoritesButton, activityAnalysisButton; // 버튼 변수들

    public UserProfileUI(int userId, HomeFrame homeFrame) {
        setTitle("User");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 600);
        setLayout(new BorderLayout());

        // 사용자 정보 패널 생성 (userId를 전달)
        JPanel userInfoPanel = createUserInfoPanel(userId, homeFrame);
        add(userInfoPanel, BorderLayout.NORTH);

        // 버튼 패널 (게시글, 즐겨찾기, 활동분석)
        JPanel buttonPanel = createButtonPanel();

        // CardLayout을 사용한 패널 생성
        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(createPanel("Post List"), "Post"); // "게시글" 카드 추가
        mainPanel.add(createPanel("Favorite List"), "Favorite"); // "즐겨찾기" 카드 추가
        mainPanel.add(createPanel("Repost List"), "Repost List"); // "활동분석" 카드 추가

        // 기본적으로 '게시글' 패널을 보여주도록 설정
        cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "Post"); // 기본 화면 '게시글'

        // 버튼 스타일 초기화
        popularPostsButton.setBackground(Color.WHITE);
        popularPostsButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        favoritesButton.setBackground(Color.LIGHT_GRAY);
        favoritesButton.setBorder(null);
        activityAnalysisButton.setBackground(Color.LIGHT_GRAY);
        activityAnalysisButton.setBorder(null);

        // 버튼 패널과 카드 레이아웃 패널을 담을 컨테이너 패널 생성
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(buttonPanel, BorderLayout.NORTH);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        // 하단 버튼 패널 생성
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        add(containerPanel, BorderLayout.CENTER);

        setVisible(true);
    }


 // 사용자 정보 패널 생성
    private JPanel createUserInfoPanel(int userId, HomeFrame homeframe) {
        JPanel userInfoPanel = new JPanel(new BorderLayout());
        FollowDAO followDAO = new FollowDAO();
        UserDAO userDAO = new UserDAO();

        // 사용자 이름 가져오기
        String userLogId = userDAO.getUserLogId(userId);
        String userName = userDAO.searchUserById(userLogId);

        // 왼쪽: 프로필 이미지 및 이름, 팔로잉/팔로워 패널
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // 프로필 이미지 및 이름 패널
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 프로필 아이콘 설정
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/images/profile.png"));
        Image profileImage = profileIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        profileIcon = new ImageIcon(profileImage);
        JLabel profileLabel = new JLabel(profileIcon);

        // 사용자 이름 레이블
        JLabel userLabel = new JLabel(userName != null ? userName : "Unknown User", SwingConstants.LEFT);
        userLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        profilePanel.add(profileLabel);
        profilePanel.add(userLabel);

        // 팔로잉/팔로워 패널
        JPanel followingPanel = createFollowingPanel(userId);

        leftPanel.add(profilePanel);
        leftPanel.add(followingPanel);

        // 오른쪽: 로그아웃 버튼 패널
        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton logoutButton = new JButton();
        // 로그아웃 아이콘 설정
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/logout.png"));
        Image image = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);

        logoutButton.setIcon(icon);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Redirecting to Login Screen..."));

        logoutPanel.add(logoutButton);

        userInfoPanel.add(leftPanel, BorderLayout.CENTER);
        userInfoPanel.add(logoutPanel, BorderLayout.EAST);

        return userInfoPanel;
    }

    // 팔로잉/팔로워 정보 패널 생성
    private JPanel createFollowingPanel(int userId) {
        JPanel followingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        FollowDAO followDAO = new FollowDAO();

        // 사용자 ID 가져오기
        int userDbId = userId;

        // 팔로워/팔로잉 수 가져오기
        int followerCount = followDAO.getFollowerCount(userDbId);
        int followingCount = followDAO.getFollowingCount(userDbId);

        JLabel followingText = new JLabel(followingCount + " Following ");
        followingText.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel followerText = new JLabel(followerCount + " Followers");
        followerText.setFont(new Font("Arial", Font.PLAIN, 14));

        followingPanel.add(followingText);
        followingPanel.add(followerText);

        return followingPanel;
    }


    // 상단 버튼 패널 생성
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // 상단 버튼 생성
        popularPostsButton = createButton("Post");
        favoritesButton = createButton("Favorite");
        activityAnalysisButton = createButton("Repost");

        // 버튼 클릭 시 화면 전환
        popularPostsButton.addActionListener(e -> switchPanel("Post"));
        favoritesButton.addActionListener(e -> switchPanel("Favorite"));
        activityAnalysisButton.addActionListener(e -> switchPanel("Repost"));

        buttonPanel.add(popularPostsButton);
        buttonPanel.add(favoritesButton);
        buttonPanel.add(activityAnalysisButton);

        return buttonPanel;
    }

    // 버튼 생성
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(120, 30));
        button.setBackground(Color.LIGHT_GRAY);
        return button;
    }

    // 카드 패널 생성
    private JPanel createPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // 버튼 색상 변경 및 카드 전환
    private void switchPanel(String panelName) {
        resetButtonStyles();

        if (panelName.equals("Post")) {
            popularPostsButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
            popularPostsButton.setBackground(Color.WHITE);
        } else if (panelName.equals("Favorite")) {
            favoritesButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
            favoritesButton.setBackground(Color.WHITE);
        } else if (panelName.equals("Repost")) {
            activityAnalysisButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
            activityAnalysisButton.setBackground(Color.WHITE);
        }

        cardLayout.show(mainPanel, panelName);
    }

    // 버튼 스타일 초기화
    private void resetButtonStyles() {
        popularPostsButton.setBackground(Color.LIGHT_GRAY);
        favoritesButton.setBackground(Color.LIGHT_GRAY);
        activityAnalysisButton.setBackground(Color.LIGHT_GRAY);

        popularPostsButton.setBorder(null);
        favoritesButton.setBorder(null);
        activityAnalysisButton.setBorder(null);
    }

    // 하단 버튼 패널 생성
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        
        bottomPanel.setLayout(new BorderLayout());

        // 게시글 업로드 버튼 생성
        JButton postButton = new JButton();
        // 알림 버튼 생성
        JButton notificationButton = new JButton();

        // 게시글 업로드 버튼 아이콘 설정
        ImageIcon postIcon = new ImageIcon(getClass().getResource("/images/add.png"));
        Image postImage = postIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        postIcon = new ImageIcon(postImage);
        
        postButton.setIcon(postIcon);
        postButton.setContentAreaFilled(false);
        postButton.setBorderPainted(false);
        postButton.setFocusable(false);
        
        // 알림 버튼 아이콘 설정
        ImageIcon notificationIcon = new ImageIcon(getClass().getResource("/images/notification.png"));
        Image notificationImage = notificationIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        notificationIcon = new ImageIcon(notificationImage);
        
        notificationButton.setIcon(notificationIcon); 
        notificationButton.setContentAreaFilled(false);
        notificationButton.setBorderPainted(false);
        notificationButton.setFocusable(false);

        bottomPanel.add(postButton, BorderLayout.EAST);
        bottomPanel.add(notificationButton, BorderLayout.WEST);

        return bottomPanel;
    }
}
