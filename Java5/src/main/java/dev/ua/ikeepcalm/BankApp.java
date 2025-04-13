package dev.ua.ikeepcalm;

import dev.ua.ikeepcalm.ui.AppMainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankApp extends AppMainFrame {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log(e.getMessage(), LogColor.RED);
        }

        SplashScreen splashScreen = new SplashScreen(15000);
        splashScreen.showSplash();
    }

    private static class SplashScreen extends JWindow {
        private final JProgressBar progressBar;
        private final Timer progressTimer;
        private int progress = 0;

        public SplashScreen(int duration) {
            JPanel content = new JPanel(new BorderLayout());
            content.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 122, 204), 3));
            JLabel titleLabel = new JLabel("Bank Deposit Simulation System", JLabel.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            titleLabel.setForeground(new Color(0, 122, 204));
            content.add(titleLabel, BorderLayout.NORTH);
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setBackground(Color.WHITE);

            JPanel iconPanel = createLogo();
            centerPanel.add(iconPanel, BorderLayout.CENTER);

            JLabel loadingLabel = new JLabel("Loading application...", JLabel.CENTER);
            loadingLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            centerPanel.add(loadingLabel, BorderLayout.SOUTH);

            content.add(centerPanel, BorderLayout.CENTER);

            JLabel timeLabel = new JLabel("", JLabel.CENTER);
            timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            updateTimeLabel(timeLabel);

            Timer timeTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateTimeLabel(timeLabel);
                }
            });
            timeTimer.start();

            JPanel footerPanel = new JPanel(new BorderLayout(5, 5));
            footerPanel.setBackground(Color.WHITE);
            footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

            progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            footerPanel.add(progressBar, BorderLayout.CENTER);
            footerPanel.add(timeLabel, BorderLayout.SOUTH);

            content.add(footerPanel, BorderLayout.SOUTH);

            setContentPane(content);
            setSize(500, 350);
            setLocationRelativeTo(null);

            progressTimer = new Timer(duration / 300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    progress++;
                    progressBar.setValue(progress);

                    if (progress == 30) {
                        loadingLabel.setText("Initializing banks...");
                    } else if (progress == 40) {
                        loadingLabel.setText("Preparing depositor models...");
                    } else if (progress == 50){
                        loadingLabel.setText("Feeding all stray cats...");
                    } else if (progress == 90) {
                        loadingLabel.setText("Configuring user interface...");
                    }

                    if (progress >= 100) {
                        progressTimer.stop();
                        timeTimer.stop();
                        launchMainApplication();
                        dispose();
                    }
                }
            });
        }

        private JPanel createLogo() {
            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int width = getWidth();
                    int height = getHeight();

                    g2d.setColor(new Color(220, 220, 220));
                    g2d.fillRect(width / 4, height / 3, width / 2, height / 2);

                    g2d.setColor(new Color(100, 100, 100));
                    int[] xPoints = {width / 4 - 20, width / 2, width * 3 / 4 + 20};
                    int[] yPoints = {height / 3, height / 6, height / 3};
                    g2d.fillPolygon(xPoints, yPoints, 3);

                    g2d.setColor(new Color(240, 240, 240));
                    int numColumns = 3;
                    int columnWidth = (width / 2) / (numColumns * 2 - 1);
                    for (int i = 0; i < numColumns; i++) {
                        int x = width / 4 + i * 2 * columnWidth;
                        g2d.fillRect(x, height / 3 + 10, columnWidth, height / 2 - 10);
                    }

                    g2d.setColor(new Color(139, 69, 19));
                    g2d.fillRect(width / 2 - columnWidth / 2, height / 2, columnWidth, height / 3);

                    g2d.setColor(new Color(0, 122, 204));
                    g2d.setFont(new Font("SansSerif", Font.BOLD, 60));
                    g2d.drawString("$", width / 2 - 18, height / 4);
                }
            };
            iconPanel.setPreferredSize(new Dimension(200, 150));
            iconPanel.setBackground(Color.WHITE);
            return iconPanel;
        }

        private void updateTimeLabel(JLabel timeLabel) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            timeLabel.setText("Current Time: " + formatter.format(now));
        }

        public void showSplash() {
            setVisible(true);
            startProgress();
        }

        private void startProgress() {
            progressTimer.start();
        }

        private void launchMainApplication() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    AppMainFrame mainFrame = new AppMainFrame();

                    if (shouldCreateSampleData()) {
                        startWithSampleData(mainFrame);
                    }

                    mainFrame.setVisible(true);
                }
            });
        }

        private boolean shouldCreateSampleData() {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Would you like to create sample data for demonstration?",
                    "Sample Data",
                    JOptionPane.YES_NO_OPTION);

            return result == JOptionPane.YES_OPTION;
        }

        private void startWithSampleData(AppMainFrame mainFrame) {
            try {
                mainFrame.createSampleData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error creating sample data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void log(String message, LogColor color) {
        String colorCode = switch (color) {
            case RED -> "\u001B[31m";
            case GREEN -> "\u001B[32m";
            case BLUE -> "\u001B[34m";
            case GRAY -> "\u001B[37m";
        };
        System.out.println(colorCode + message + "\u001B[0m");
    }

    public static void log(String message) {
        log(message, LogColor.GRAY);
    }

    public enum LogColor {
        RED, GREEN, BLUE, GRAY
    }

}