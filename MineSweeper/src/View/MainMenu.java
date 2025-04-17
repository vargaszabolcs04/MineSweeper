package View;

import Utils.ScoreManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class MainMenu extends JFrame {
    private JPanel backgroundPanel;
    private Image backgroundImage;
    public static int diff = 1;

    public MainMenu() {
        setTitle("MineSweeper - Main Menu");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        backgroundImage = new ImageIcon("town.png").getImage();
        createBackgroundPanel();

        JButton startButton = new JButton("Start");
        startButton.setBounds(350, 100, 200, 80);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameWindow();
            }
        });

        JButton scoreboardButton = new JButton("Scoreboard");
        scoreboardButton.setBounds(350, 250, 200, 80);
        scoreboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> scores = ScoreManager.getScores();
                new ScoreboardWindow(scores);
            }
        });

        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficultyLevels);
        difficultyComboBox.setBounds(350, 400, 200, 80);
        difficultyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                if (Objects.equals(selectedDifficulty, "Easy")) {
                    diff = 1;
                } else if (Objects.equals(selectedDifficulty, "Medium")) {
                    diff = 2;
                } else diff = 3;

            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(350, 550, 200, 80);
        exitButton.addActionListener(e -> System.exit(0));

        backgroundPanel.add(startButton);
        backgroundPanel.add(scoreboardButton);
        backgroundPanel.add(difficultyComboBox);
        backgroundPanel.add(exitButton);

        add(backgroundPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createBackgroundPanel() {
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
    }
    public static class Main {
        public static void main(String[] args) {
            new MainMenu();
        }
    }
}


