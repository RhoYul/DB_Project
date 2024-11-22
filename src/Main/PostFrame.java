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
    private int likes; // 좋아요 개수
    private int dislikes; // 싫어요 개수
    private JPanel commentPanel; // 댓글 표시 패널
    private JLabel dislikeLabel; // 싫어요 개수 표시 라벨
    private String authorName; // 작성자 이름
    private String postContent; // 게시글 내용
    private PostDAO postDAO;
    private PostDTO post;
    private UserDAO userDAO;
    private CommentsDAO commentsDAO;
    private int userId;
    private int postId;
    private JButton likeButton;
    private JButton dislikeButton;
    private JLabel commentLabel;

    public PostFrame(int userId, int postId, String id, String postContent) {
        this.postContent = postContent; // 게시글 내용 초기화
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

        // 게시물 세부 내용 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // 세로로 정렬
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 작성자 이름 표시 (왼쪽 정렬)
        JLabel authorLabel = new JLabel("Posted by: " + id);
        authorLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14)); // 글씨 크기 줄이기
        authorLabel.setForeground(new Color(29, 161, 242));

        // 작성자 이름을 왼쪽 정렬 패널에 추가
        JPanel authorPanel = new JPanel();
        authorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        authorPanel.add(authorLabel);
        contentPanel.add(authorPanel);

        // 게시물 내용 표시 (왼쪽 정렬)
        JLabel contentLabel = new JLabel("<html>" + postContent.replace("\n", "<br>") + "</html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        contentLabel.setForeground(Color.BLACK);

        // 게시물 내용을 왼쪽 정렬 패널에 추가
        JPanel contentTextPanel = new JPanel();
        contentTextPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        contentTextPanel.add(contentLabel);
        contentPanel.add(contentTextPanel);

        // 좋아요 및 싫어요 버튼 패널
        JPanel likeDislikePanel = new JPanel();
        likeDislikePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // 좋아요 버튼
        likeButton = new JButton("Like [" + postDAO.getLikesCount(postId) + "]");
        likeButton.setFont(new Font("Arial", Font.BOLD, 16));
        likeButton.setBackground(Color.WHITE); // 버튼 배경 흰색
        likeButton.setForeground(new Color(29, 161, 242)); // 버튼 글씨 트위터 블루
        likeButton.setFocusPainted(false); // 클릭 시 테두리 제거
        likeButton.setBorder(BorderFactory.createLineBorder(new Color(29, 161, 242))); // 외곽선 트위터 블루
        likeButton.setPreferredSize(new Dimension(140, 50)); // 버튼 크기 증가
        likeButton.addActionListener(e -> {
        	try {
                postDAO.toggleLike(postId, userId); // 좋아요 토글
                // 최신 데이터 가져오기 및 UI 업데이트
                int updatedLikes = postDAO.getLikesCount(postId);
                int updatedHates = postDAO.getHatesCount(postId);
                likeButton.setText("Like [" + updatedLikes + "]");
                dislikeButton.setText("Hate [" + updatedHates + "]");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    	});

        // 싫어요 버튼
        dislikeButton = new JButton("Hate [" + postDAO.getHatesCount(postId) + "]");
        dislikeButton.setFont(new Font("Arial", Font.BOLD, 16));
        dislikeButton.setBackground(new Color(29, 161, 242)); // 버튼 배경 트위터 블루
        dislikeButton.setForeground(Color.WHITE); // 버튼 글씨 흰색
        dislikeButton.setFocusPainted(false); // 클릭 시 테두리 제거
        dislikeButton.setBorder(BorderFactory.createLineBorder(new Color(29, 161, 242))); // 외곽선 트위터 블루
        dislikeButton.setPreferredSize(new Dimension(140, 50)); // 버튼 크기 증가
        dislikeButton.addActionListener(e -> {
        	try {
                postDAO.toggleHate(postId, userId); // 싫어요 토글
                // 최신 데이터 가져오기 및 UI 업데이트
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

        // 댓글 입력 및 표시 패널
        JPanel commentInputPanel = new JPanel();
        commentInputPanel.setLayout(new BorderLayout());

        // 댓글 입력 필드 크기 증가
        JTextField commentField = new JTextField();
        commentField.setFont(new Font("Arial", Font.PLAIN, 16));
        commentField.setPreferredSize(new Dimension(250, 50));

        // Add Comment 버튼 스타일 및 크기 증가
        JButton addCommentButton = new JButton("Add Comment");
        addCommentButton.setFont(new Font("Arial", Font.BOLD, 16));
        addCommentButton.setBackground(Color.WHITE); // 배경 흰색
        addCommentButton.setForeground(new Color(29, 161, 242)); // 글씨 트위터 블루
        addCommentButton.setFocusPainted(false); // 클릭 시 테두리 제거
        addCommentButton.setBorder(BorderFactory.createLineBorder(new Color(29, 161, 242))); // 외곽선 트위터 블루
        addCommentButton.setPreferredSize(new Dimension(150, 50)); // 버튼 크기 증가

        commentInputPanel.add(commentField, BorderLayout.CENTER);
        commentInputPanel.add(addCommentButton, BorderLayout.EAST);

        // 댓글 표시 패널
        commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));

        // 댓글 표시 패널 초기 댓글 로드
        loadComments();

        JScrollPane commentScrollPane = new JScrollPane(commentPanel);
        commentScrollPane.setPreferredSize(new Dimension(400, 250));

        // 메인 레이아웃 설정
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.NORTH);
        add(commentInputPanel, BorderLayout.SOUTH);
        add(commentScrollPane, BorderLayout.CENTER);

        // 댓글 추가 버튼 동작
        addCommentButton.addActionListener(e -> {
            String comment = commentField.getText().trim();
            commentLabel = new JLabel("<html><i>- " + authorName + ": " + comment + "</i></html>");
            commentLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
            if (!comment.isEmpty()) {
                commentsDAO.addComment(postId, userId, comment);
                
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
                        commentPanel.add(Box.createVerticalStrut(10)); // 댓글 간 간격 추가
                    }
                }
                commentField.setText(""); // 입력 필드 초기화

                commentPanel.revalidate();
                commentPanel.repaint();
            }
        });
    }
    
    private void loadComments() {
        commentPanel.removeAll(); // 기존 댓글 UI 초기화
        List<CommentsDTO> commentList = commentsDAO.getCommentsByPostId(postId);

        if (commentList.isEmpty()) {
            // 댓글이 없을 때 안내 메시지 표시
            JLabel noCommentLabel = new JLabel("No comments available.");
            noCommentLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noCommentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            commentPanel.add(noCommentLabel);
        } else {
            // 댓글 목록 표시
            for (CommentsDTO comm : commentList) {
                JLabel commentLabel = new JLabel(
                    String.format("<html><i>- %s (%s): %s</i></html>",
                                  comm.getUSER_NAME(),
                                  comm.getREGDATE(),
                                  comm.getCOMMENT_TEXT())
                );
                commentLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
                commentPanel.add(commentLabel);
                commentPanel.add(Box.createVerticalStrut(10)); // 댓글 간격 추가
            }
        }

        // UI 갱신
        commentPanel.revalidate();
        commentPanel.repaint();
    }

}
