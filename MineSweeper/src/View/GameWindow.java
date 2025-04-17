package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private GamePanel gamePanel;

    private JPanel backgroundPanel;
    private Image backgroundImage;

    private JLabel timerLabel;
    private Timer timer;
    private static int elapsedTime;
    private static boolean muted = false;

    public GameWindow() throws HeadlessException {
        setTitle("MineSweeper");

        setSize(1100, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        backgroundImage = new ImageIcon("town.png").getImage();
        createBackgroundPanel();

        timerLabel = new JLabel("Time: 0s");
        timerLabel.setBounds(20, 10, 150, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        timerLabel.setForeground(Color.BLACK);
        backgroundPanel.add(timerLabel);

        gamePanel = new GamePanel(this);
        gamePanel.setOpaque(false);
        gamePanel.setBounds(225, 50, 650, 650);

        JButton restartButton = new JButton("Restart");
        restartButton.setBounds(500, 800, 100, 40);
        restartButton.addActionListener(e -> restartGame());

        JButton menuButton = new JButton("Menu");
        menuButton.setBounds(500, 860, 100, 40);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePanel.backgroundMusic.stop();
                dispose();
                new MainMenu();
            }
        });

        JButton muteButton = new JButton("Mute");
        muteButton.setBounds(1000, 20, 80, 30);
        ImageIcon originalIcon = new ImageIcon("unmute.png");
        // Átméretezés a gomb méretéhez
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                muteButton.getWidth(),
                muteButton.getHeight(),
                Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        muteButton.setIcon(scaledIcon);
        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!muted) {
                    ImageIcon originalIcon = new ImageIcon("mute.png");
                    // Átméretezés a gomb méretéhez
                    Image scaledImage = originalIcon.getImage().getScaledInstance(
                            muteButton.getWidth(),
                            muteButton.getHeight(),
                            Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    muteButton.setIcon(scaledIcon);

                    muted = true;
                    GamePanel.backgroundMusic.stop();
                } else {
                    ImageIcon originalIcon = new ImageIcon("unmute.png");
                    // Átméretezés a gomb méretéhez
                    Image scaledImage = originalIcon.getImage().getScaledInstance(
                            muteButton.getWidth(),
                            muteButton.getHeight(),
                            Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    muteButton.setIcon(scaledIcon);

                    muted = false;
                    GamePanel.backgroundMusic.start();
                }
            }
        });

        backgroundPanel.add(gamePanel);
        backgroundPanel.add(restartButton);
        backgroundPanel.add(menuButton);
        backgroundPanel.add(muteButton);

        setContentPane(backgroundPanel);

        initializeTimer();

        setVisible(true);
    }

    private void initializeTimer() {
        elapsedTime = 0;
        timer = new Timer(1000, e -> {
            elapsedTime++;
            timerLabel.setText("Time: " + elapsedTime + "s");
        });
        timer.start();
    }


    public void timerStop() {
        timer.stop();
    }

    public void endGame() {
        backgroundImage = new ImageIcon("town_expl.png").getImage();
        createBackgroundPanel();
        repaint();
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

    private void restartGame() {
        timer.stop();
        elapsedTime = 0;
        timerLabel.setText("Time: 0s");
        gamePanel.setVisible(false);
        backgroundImage = new ImageIcon("town.png").getImage();
        createBackgroundPanel();

        GamePanel.backgroundMusic.stop();

        gamePanel = new GamePanel(this);
        gamePanel.setOpaque(false);
        gamePanel.setBounds(225, 50, 650, 650);
        getContentPane().add(gamePanel);

        if (muted) {
            GamePanel.backgroundMusic.stop();
        } else GamePanel.backgroundMusic.start();

        repaint();

        timer.start();
    }

    public static long getElapsedTime() {
        return elapsedTime;
    }
}
