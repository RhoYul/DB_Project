package Main;

import javax.swing.*;
import java.awt.*;

public class HomeFrame extends JFrame {
    private JPanel postPanel; // 게시글 표시 패널
    private JLabel noPostLabel; // 게시물이 없을 때 표시할 라벨
    private String username; // 사용자 이름
    private int userId; // 로그인한 사용자의 ID

    // HomeFrame 생성자
    public HomeFrame(int userId, String username) {
        this.userId = userId; // 로그인한 사용자의 ID
        this.username = username; // 사용자 이름 초기화

        setTitle("Home");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

        // 사용자 버튼을 왼쪽에, 게시글 작성 버튼을 오른쪽에 배치
        buttonPanel.add(userButton, BorderLayout.WEST);
        buttonPanel.add(uploadButton, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);

        // 상단 패널 (로고와 정렬 버튼 포함)
        JPanel headerPanel = new JPanel(new BorderLayout());

        // 로고 패널
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 225, 10));
        JLabel logoLabel = new JLabel("X");
        logoPanel.setBackground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.BLACK);
        logoPanel.add(logoLabel);
        headerPanel.add(logoPanel, BorderLayout.WEST);

        // 정렬 버튼 패널
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        // 라디오 버튼을 사용하여 정렬 기준 선택
        JRadioButton sortByPopularityButton = new JRadioButton("Popularity");
        JRadioButton sortByDateButton = new JRadioButton("Date");

        // 라디오 버튼 그룹화
        ButtonGroup sortButtonGroup = new ButtonGroup();
        sortButtonGroup.add(sortByPopularityButton);
        sortButtonGroup.add(sortByDateButton);

        // 기본 선택값 설정
        sortByPopularityButton.setSelected(true);

        // 정렬 버튼 패널에 추가
        sortPanel.add(sortByPopularityButton);
        sortPanel.add(sortByDateButton);
        headerPanel.add(sortPanel, BorderLayout.EAST);

        // 상단 패널 추가
        add(headerPanel, BorderLayout.NORTH);

        // 게시글 패널 초기화
        postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));

        // 게시글 없음 표시
        noPostLabel = new JLabel("There is no post", SwingConstants.CENTER);
        noPostLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        noPostLabel.setForeground(Color.GRAY);
        noPostLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 수평 가운데 정렬

        postPanel.add(Box.createVerticalGlue()); // 상단 여백
        postPanel.add(noPostLabel);             // 가운데 문구
        postPanel.add(Box.createVerticalGlue()); // 하단 여백

        JScrollPane scrollPane = new JScrollPane(postPanel);
        add(scrollPane, BorderLayout.CENTER);

        // 버튼 동작 정의
        userButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "User button clicked!"));

        // Upload 버튼 동작 정의
        uploadButton.addActionListener(e -> {
            // UploadFrame 생성 및 표시
            UploadFrame uploadFrame = new UploadFrame(userId, this); // 현재 사용자의 userId 전달
            uploadFrame.setVisible(true);
        });

        // 정렬 버튼 동작 정의
        sortByPopularityButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sort by Popularity clicked!"));
        sortByDateButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sort by Date clicked!"));
    }

    // 사용자 버튼 스타일 정의
    private void styleUserButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(29, 161, 242)); // 트위터 블루 색상
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
