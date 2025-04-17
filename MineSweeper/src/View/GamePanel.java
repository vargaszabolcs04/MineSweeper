package View;

import Utils.BackgroundMusic;
import Utils.ScoreManager;
import Utils.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class GamePanel extends JPanel {
    private GameWindow gameWindow;
    private static int ROWS;
    private static int COLS;

    private static int MINES;
    private int remainingCells = 15;

    private boolean[][] mines;

    private JButton[][] buttons;

    private boolean[][] revealed;

    public static BackgroundMusic backgroundMusic;

    public GamePanel(GameWindow gameWindow) {
        backgroundMusic = new BackgroundMusic("src/sound/background_music.wav");
        backgroundMusic.start();
        switch (MainMenu.diff) {
            case 1 -> {
                MINES = 5;
                ROWS = 10;
                COLS = 10;
            }
            case 2 -> {
                MINES = 10;
                ROWS = 10;
                COLS = 10;
            }
            case 3 -> {
                MINES = 30;
                ROWS = 15;
                COLS = 15;
            }
        }
        this.gameWindow = gameWindow;
        setLayout(new GridLayout(ROWS, COLS));
        buttons = new JButton[ROWS][COLS];
        mines = new boolean[ROWS][COLS];
        revealed = new boolean[ROWS][COLS];
        remainingCells = ROWS * COLS - MINES;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50));
                button.setBackground(Color.lightGray);
                buttons[row][col] = button;
                button.addMouseListener(new ButtonClickListener(row, col));
                add(button);
            }
        }

        placeMines();
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < MINES) {
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLS);

            if (!mines[row][col]) {
                mines[row][col] = true;
                placedMines++;
            }
        }

    }

    private void revealCell(int row, int col) {
        if (revealed[row][col] || mines[row][col]) {
            return;
        }

        revealed[row][col] = true;
        buttons[row][col].setText("");
        remainingCells--;

        // Ha üres mezőt találtunk, környéket is feltárjuk
        int adjacentMines = countAdjacentMines(row, col);
        if (adjacentMines == 0) {
            // Körbejárjuk az üres mezőt
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = col - 1; c <= col + 1; c++) {
                    if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
                        buttons[r][c].setBackground(Color.WHITE);
                        revealCell(r, c);
                    }
                }
            }
        } else {
            buttons[row][col].setText(String.valueOf(adjacentMines));// Kiírjuk a szomszédos aknák számát
            buttons[row][col].setForeground(Color.BLACK);
            buttons[row][col].setBackground(Color.WHITE);
        }

    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
                    if (mines[r][c]) {
                        count++;
                    }
                }
            }
        }

        return count;
    }


    private class ButtonClickListener implements MouseListener {

        private final int row;
        private final int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!buttons[row][col].isEnabled()) {
                return; // Ha a gomb le van tiltva, ne csináljon semmit
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (buttons[row][col].getIcon() != null) {
                    buttons[row][col].setIcon(null);
                }

                SoundPlayer.playSound("src/sounds/click.wav");
                if (mines[row][col]) {
                    backgroundMusic.stop();
                    SoundPlayer.playSound("src/sounds/mine.wav");
                    ImageIcon mineIcon = new ImageIcon("mine2.png");
                    // Átméretezés a gomb méretéhez
                    Image scaledMine = mineIcon.getImage().getScaledInstance(
                            buttons[row][col].getWidth(),
                            buttons[row][col].getHeight(),
                            Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledMine);

                    // Ikon beállítása
                    buttons[row][col].setIcon(scaledIcon);
                    gameWindow.timerStop();
                    gameWindow.endGame();
                    JOptionPane.showMessageDialog(null, "Game Over!");
                    revealAllMines();
                } else {
                    revealCell(row, col); // Üres mező feltárása
                    checkWin();
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if (buttons[row][col].getIcon() != null) {
                    buttons[row][col].setIcon(null);
                } else {
                    ImageIcon mineIcon = new ImageIcon("flag.png");
                    Image scaledFlag = mineIcon.getImage().getScaledInstance(
                            buttons[row][col].getWidth(),
                            buttons[row][col].getHeight(),
                            Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledFlag);
                    buttons[row][col].setIcon(scaledIcon);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private void checkWin() {
        if (remainingCells == 0) {
            backgroundMusic.stop();
            gameWindow.timerStop();
            SoundPlayer.playSound("src/sounds/win.wav");
            JOptionPane.showMessageDialog(null, "You Win!");
            ScoreManager.addScore(String.valueOf(GameWindow.getElapsedTime()));
            revealAllMines();
        }
    }

    private void revealAllMines() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (mines[row][col]) {
                    ImageIcon originalIcon = new ImageIcon("mine2.png");
                    // Átméretezés a gomb méretéhez
                    Image scaledImage = originalIcon.getImage().getScaledInstance(
                            buttons[row][col].getWidth(),
                            buttons[row][col].getHeight(),
                            Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    buttons[row][col].setIcon(scaledIcon);
                }
                buttons[row][col].setEnabled(false);
                buttons[row][col].setDisabledIcon(buttons[row][col].getIcon());
                buttons[row][col].removeMouseListener(buttons[row][col].getMouseListeners()[0]);
            }
        }
    }
}

