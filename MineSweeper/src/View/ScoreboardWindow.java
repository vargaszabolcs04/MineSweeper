package View;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ScoreboardWindow extends JFrame {
    public ScoreboardWindow(List<String> scores) {
        setTitle("Scoreboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (String score : scores) {
            JLabel scoreLabel = new JLabel(score + " s");
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            panel.add(scoreLabel);
        }

        // Görgethető panel
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        setVisible(true);
    }
}
