package tool;

import javax.swing.*;
import java.awt.*;

public class MinigamePanel extends JFrame {
    public MinigamePanel() {
        // Thiết lập cửa sổ chính
        setTitle("Minigame Instructions");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.ORANGE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel chứa icon sách
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ImageIcon bookIcon = new ImageIcon("path_to_book_image.png"); // Thay bằng đường dẫn ảnh sách
        JLabel bookLabel = new JLabel(bookIcon);
        iconPanel.add(bookLabel);
        iconPanel.setOpaque(false);

        // Panel chứa tiêu đề và icon mèo
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Cách chơi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setIcon(new ImageIcon("path_to_cat_image.png")); // Thay bằng đường dẫn ảnh mèo
        titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        titleLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Panel chứa nội dung hướng dẫn
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Tạo từng dòng hướng dẫn với số thứ tự
        String[] instructions = {
                "Minigame bao gồm các câu hỏi về nghĩa của những từ mà bạn đã tra gần đây",
                "Có tổng cộng 10 câu hỏi dạng trắc nghiệm và bạn có 2 phút để hoàn thành",
                "Bạn nên sử dụng trí nhớ của mình, không nên sử dụng chức năng Tra từ và Dịch của ứng dụng"
        };

        for (int i = 0; i < instructions.length; i++) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.setOpaque(false);

            JLabel numberLabel = new JLabel(String.format("%02d", i + 1));
            numberLabel.setFont(new Font("Arial", Font.BOLD, 14));
            numberLabel.setForeground(Color.BLUE);

            JLabel textLabel = new JLabel(instructions[i]);
            textLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            itemPanel.add(numberLabel);
            itemPanel.add(textLabel);
            contentPanel.add(itemPanel);
        }

        // Thêm các panel vào panel chính
        mainPanel.add(iconPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.SOUTH);

        // Thêm panel chính vào frame
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MinigamePanel frame = new MinigamePanel();
            frame.setVisible(true);
        });
    }
}
