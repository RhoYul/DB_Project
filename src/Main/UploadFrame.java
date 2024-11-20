package Main;

import javax.swing.*;
import java.awt.*;
import Post.PostDAO;
import java.sql.*;

public class UploadFrame extends JFrame {
	private HomeFrame homeFrame;
	private int authorId;
	private PostDAO postDAO;
	
    public UploadFrame(int userId, HomeFrame homeFrame) {
    	this.homeFrame = homeFrame;
    	this.postDAO = new PostDAO();
    	
        setTitle("Upload Post");
        setSize(500, 400);
        setLocationRelativeTo(null); // 화면 중앙에 위치
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 전체 패널 설정
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 상단 패널 생성 및 문구 삽입
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // 중앙 정렬
        JLabel topLabel = new JLabel("What's going on?");
        topLabel.setFont(new Font("Arial", Font.BOLD, 18)); // 글씨 크기와 굵게 설정
        topLabel.setForeground(Color.BLACK); // 글씨 색상 설정
        topPanel.add(topLabel);
        panel.add(topPanel, BorderLayout.NORTH); // 상단 패널을 전체 패널에 추가

        // 게시물 작성 텍스트 필드
        JTextArea postContentArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(postContentArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // 좌우로 두 버튼 배치

        // 이미지 업로드 버튼
        JButton imageUploadButton = new JButton("Image Upload");
        styleButton(imageUploadButton);

        // 업로드 버튼
        JButton uploadButton = new JButton("Upload");
        styleButton(uploadButton);

        buttonPanel.add(imageUploadButton);
        buttonPanel.add(uploadButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // 버튼 클릭 이벤트 (단순 메시지 표시)
        imageUploadButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Image Upload clicked!"));
        uploadButton.addActionListener(e -> {
        	this.authorId = userId;
            String content = postContentArea.getText();
            
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Content must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                // 게시글 생성 및 POST_ID 가져오기
                int postId = postDAO.createPost(authorId, content);
                if (postId != -1) {
                    JOptionPane.showMessageDialog(this, "Post uploaded (ID: " + postId + ")", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // 창 닫기
                } else {
                    JOptionPane.showMessageDialog(this, "Post creation failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(29, 161, 242));
        button.setFocusPainted(false);
    }
}
