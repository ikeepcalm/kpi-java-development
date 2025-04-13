package dev.ua.ikeepcalm.app.conway;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

public class ConwayGameOfLife extends JPanel {

    private int[][] grid;
    private static final Random rnd = new Random();
    private static final int SIZE = 6;
    private int generationCounter;
    private final Timer timer;
    private boolean running = false;
    private final JPanel gamePanel;
    private JPanel controlPanel;

    public ConwayGameOfLife(int width, int height) {
        setLayout(new BorderLayout());

        this.grid = new int[height / SIZE][width / SIZE];

        gamePanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(grid[0].length * SIZE, grid.length * SIZE);
            }

            @Override
            protected void paintComponent(Graphics gg) {
                Graphics2D g = (Graphics2D) gg;
                super.paintComponent(g);
                g.setBackground(Color.WHITE);
                g.drawString("Generation: " + generationCounter, 0, 10);
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        if (grid[i][j] == 1) {
                            g.setColor(Color.BLUE);
                            g.fillRect(j * SIZE, i * SIZE, SIZE, SIZE);
                        }
                    }
                }
            }
        };

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!running) {
                    int x = e.getX() / SIZE;
                    int y = e.getY() / SIZE;
                    int R = 5;

                    for (int i = -R; i <= R; i++) {
                        for (int j = -R; j <= R; j++) {
                            int nx = x + j;
                            int ny = y + i;

                            if (nx >= 0 && nx < grid[0].length && ny >= 0 && ny < grid.length) {
                                grid[ny][nx] = grid[ny][nx] == 0 ? 1 : 0;
                            }
                        }
                    }
                    gamePanel.repaint();
                }
            }
        });

        setupControlPanel();

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        timer = new Timer(100, (ActionEvent e) -> {
            updateGrid();
            gamePanel.repaint();
        });
    }

    private void setupControlPanel() {
        controlPanel = new JPanel(new FlowLayout());

        JButton randomButton = new JButton("Random");
        randomButton.addActionListener(e -> {
            stopSimulation();
            setupRandomGrid();
            generationCounter = 0;
            gamePanel.repaint();
        });

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            running = true;
            generationCounter = 0;
            timer.start();
        });

        JButton pauseResumeButton = new JButton("Pause");
        pauseResumeButton.addActionListener(e -> {
            if (running) {
                timer.stop();
                pauseResumeButton.setText("Resume");
            } else {
                timer.start();
                pauseResumeButton.setText("Pause");
            }
            running = !running;
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            stopSimulation();
            clearGrid();
            generationCounter = 0;
            gamePanel.repaint();
        });

        controlPanel.add(randomButton);
        controlPanel.add(startButton);
        controlPanel.add(pauseResumeButton);
        controlPanel.add(resetButton);
    }

    private void stopSimulation() {
        timer.stop();
        running = false;
    }

    private void setupRandomGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (rnd.nextDouble() < 0.1) {
                    grid[i][j] = rnd.nextInt(2);
                } else {
                    grid[i][j] = 0;
                }
            }
        }
    }

    private void clearGrid() {
        for (int[] ints : grid) {
            Arrays.fill(ints, 0);
        }
    }

    public void updateGrid() {
        int[][] newGrid = new int[grid.length][grid[0].length];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int neighbors = countNeighbors(i, j);

                if (grid[i][j] == 1) {
                    newGrid[i][j] = (neighbors < 2 || neighbors > 3) ? 0 : 1;
                } else {
                    newGrid[i][j] = (neighbors == 3) ? 1 : 0;
                }
            }
        }

        grid = newGrid;
        generationCounter++;

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int countNeighbors(int i, int j) {
        int count = 0;

        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) continue;

                int ni = i + di;
                int nj = j + dj;

                if (ni >= 0 && ni < grid.length && nj >= 0 && nj < grid[0].length) {
                    count += grid[ni][nj];
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        final ConwayGameOfLife c = new ConwayGameOfLife(800, 400);
        JFrame frame = new JFrame("Conway's Game of Life");
        frame.getContentPane().add(c);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}