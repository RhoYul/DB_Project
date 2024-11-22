package Main;

import javax.swing.*;
import java.awt.*;
import User.UserDAO;
import Follow.FollowDAO;

public class SearchFrame extends JFrame {

    private JPanel resultPanel; // 검색 결과를 표시할 패널
    private int userId; // 로그인한 사용자 ID

    public SearchFrame(int userId) {
        this.userId = userId; // 로그인된 사용자 ID 전달
        setTitle("Search User");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 현재 창만 닫기
        setLayout(new BorderLayout());

        // 검색 입력 필드와 버튼
        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Enter User ID:");
        JTextField textField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        searchPanel.add(label);
        searchPanel.add(textField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // 검색 결과를 표시할 패널 초기화
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        add(resultPanel, BorderLayout.CENTER);

        // 검색 버튼 클릭 이벤트
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

    // 검색 결과를 화면에 표시
    private void displayUserInfo(String searchedUserId) {
        UserDAO userDAO = new UserDAO();
        FollowDAO followDAO = new FollowDAO();

        // 검색된 사용자 정보 가져오기
        String userInfo = userDAO.searchUserById(searchedUserId);

        // 결과 패널 초기화
        resultPanel.removeAll();

        if (userInfo != null) {
            // 유저 정보 표시
            JLabel userInfoLabel = new JLabel(userInfo);
            userInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            resultPanel.add(userInfoLabel);

            // 팔로우 버튼 추가
            JButton followButton = new JButton("Follow");
            followButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            followButton.addActionListener(e -> {
                int currentUserId = userId; // 현재 로그인한 사용자 ID
                int targetUserId = userDAO.getUserId(searchedUserId); // 검색된 사용자 ID

                // 유효성 체크
                if (currentUserId == -1 || targetUserId == -1) {
                    JOptionPane.showMessageDialog(this, "Invalid user IDs. Cannot follow.");
                    return;
                }

                // 팔로우 동작 수행
                boolean success = followDAO.followUser(currentUserId, targetUserId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "You are now following this user!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to follow. Please try again.");
                }
            });

            resultPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 여백 추가
            resultPanel.add(followButton);

        } else {
            // 사용자 정보를 찾을 수 없는 경우
            JLabel notFoundLabel = new JLabel("User not found.");
            notFoundLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            resultPanel.add(notFoundLabel);
        }

        // 패널 갱신
        resultPanel.revalidate();
        resultPanel.repaint();
    }
}
