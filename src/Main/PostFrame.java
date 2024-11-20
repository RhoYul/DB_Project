package Main;

import javax.swing.*;
import java.awt.*;

public class PostFrame extends JFrame {
    private int likes;
    private int dislikes;
    private String authorName;
    private String postContent;

    public PostFrame(String authorName, String postContent, int likes, int dislikes) {
        this.authorName = authorName;
        this.postContent = postContent;
        this.likes = likes;
        this.dislikes = dislikes;

        setTitle("Post Details");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 메인 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 작성자 정보
        JLabel authorLabel = new JLabel("Posted by: " + authorName);
        authorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        authorLabel.setForeground(new Color(29, 161, 242));

        // 게시글 내용
        JLabel contentLabel = new JLabel("<html>" + postContent.replace("\n", "<br>") + "</html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        contentLabel.setForeground(Color.BLACK);

        // 좋아요 및 싫어요 패널
        JPanel likeDislikePanel = new JPanel(new FlowLayout());
        JLabel likeLabel = new JLabel("Likes: " + likes);
        JLabel dislikeLabel = new JLabel("Dislikes: " + dislikes);
        JButton likeButton = new JButton("Like");
        JButton dislikeButton = new JButton("Dislike");

        styleButton(likeButton);
        styleButton(dislikeButton);

        likeDislikePanel.add(likeButton);
        likeDislikePanel.add(likeLabel);
        likeDislikePanel.add(dislikeButton);
        likeDislikePanel.add(dislikeLabel);

        // 버튼 클릭 이벤트 (단순 메시지 표시)
        likeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Liked!"));
        dislikeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Disliked!"));

        contentPanel.add(authorLabel);
        contentPanel.add(Box.createVerticalStrut(10)); // 여백
        contentPanel.add(contentLabel);
        contentPanel.add(Box.createVerticalStrut(20)); // 여백
        contentPanel.add(likeDislikePanel);

        add(contentPanel);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(29, 161, 242));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PostFrame("User", "This is a post content example.", 0, 0).setVisible(true));
    }
}
