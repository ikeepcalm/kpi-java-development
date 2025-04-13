package dev.ua.ikeepcalm.app.simulator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DepositsPlease extends JPanel {

    DrawableClient player;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int CLIENT_WIDTH = 60;
    private static final int CLIENT_HEIGHT = 120;
    private static final int DESK_WIDTH = 300;
    private static final int DESK_HEIGHT = 150;
    private static final int QUEUE_START_X = 50;
    private static final int QUEUE_Y = 400;
    private static final int QUEUE_SPACING = 70;
    private static final int DESK_X = 500;
    private static final int DESK_Y = 250;
    private static final int MAX_QUEUE_SIZE = 5;
    private static final int ENTRANCE_X = -CLIENT_WIDTH;

    private Timer clientGeneratorTimer;
    private boolean deskOpen = false;
    private boolean simulationRunning = false;
    private final Random random = new Random();

    private int clientsServed = 0;
    private int clientsRejected = 0;
    private int money = 1000;
    private int reputation = 50;

    private final List<DrawableClient> queuedDrawableClients = new ArrayList<>();
    private final List<SmokeEffect> smokeEffects = new ArrayList<>();
    private DrawableClient currentDrawableClient = null;

    private JPanel gamePanel;
    private JPanel controlPanel;
    private JPanel infoPanel;
    private JTextArea clientInfoArea;
    private JButton acceptButton;
    private JButton rejectButton;
    private JButton openDeskButton;
    private JLabel statsLabel;

    public DepositsPlease() {
        this.player = new DrawableClient("Player", 21, "Gamer", "Manager", "-", 0, 0, "MVP");

        setLayout(new BorderLayout());

        setupGamePanel();
        setupControlPanel();
        setupInfoPanel();

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(infoPanel, BorderLayout.EAST);

        setupTimers();
        updateButtons();
        startSimulation();
    }

    private void setupGamePanel() {
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBankOffice(g);
                drawDesk(g);
                drawAllClients(g);
                drawStats(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        gamePanel.setBackground(Color.WHITE);
    }

    private void setupInfoPanel() {
        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(400, HEIGHT));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        clientInfoArea = new JTextArea(20, 20);
        clientInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(clientInfoArea);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        acceptButton = new JButton("Accept Request");
        rejectButton = new JButton("Reject Request");

        acceptButton.addActionListener(e -> handleClientRequest(true));
        rejectButton.addActionListener(e -> handleClientRequest(false));

        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

        statsLabel = new JLabel("Money: $" + money + " | Reputation: " + reputation + "%");
        statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoPanel.add(scrollPane, BorderLayout.CENTER);
        infoPanel.add(buttonPanel, BorderLayout.SOUTH);
        infoPanel.add(statsLabel, BorderLayout.NORTH);

        updateClientButtons(false);
    }

    private void setupControlPanel() {
        controlPanel = new JPanel(new FlowLayout());

        openDeskButton = new JButton("Open Desk");
        openDeskButton.addActionListener(e -> toggleDesk());

        controlPanel.add(openDeskButton);
    }

    private void setupTimers() {
        clientGeneratorTimer = new Timer(3000, e -> generateNewClient());

        Timer animationTimer = new Timer(15, e -> {
            updateClientPositions();
            gamePanel.repaint();
        });

        animationTimer.start();
    }

    private void updateButtons() {
        openDeskButton.setText(deskOpen ? "Close Desk" : "Open Desk");
    }

    private void updateClientButtons(boolean enabled) {
        acceptButton.setEnabled(enabled && currentDrawableClient != null);
        rejectButton.setEnabled(enabled && currentDrawableClient != null);
    }

    private void startSimulation() {
        if (!simulationRunning) {
            simulationRunning = true;
            clientGeneratorTimer.start();
            updateButtons();
        }
    }

    private void toggleDesk() {
        deskOpen = !deskOpen;
        openDeskButton.setText(deskOpen ? "Close Desk" : "Open Desk");

        if (deskOpen && currentDrawableClient == null && !queuedDrawableClients.isEmpty() &&
                !queuedDrawableClients.getFirst().movingToDesk) {
            moveFirstClientToDesk();
        }
    }

    private void generateNewClient() {
        if (deskOpen && queuedDrawableClients.size() < MAX_QUEUE_SIZE) {

            for (DrawableClient drawableClient : queuedDrawableClients) {
                Point currentPosition = drawableClient.getCurrentPosition();
                drawableClient.setTargetPosition(new Point(currentPosition.x + QUEUE_SPACING, currentPosition.y));
            }

            DrawableClient newDrawableClient = generateRandomClient();

            newDrawableClient.currentPosition = new Point(ENTRANCE_X, QUEUE_Y);
            newDrawableClient.targetPosition = new Point(QUEUE_START_X, QUEUE_Y);

            queuedDrawableClients.add(newDrawableClient);

            if (currentDrawableClient == null && !queuedDrawableClients.getFirst().movingToDesk) {
                moveFirstClientToDesk();
            }
        }
    }

    private void moveFirstClientToDesk() {
        if (!queuedDrawableClients.isEmpty() && currentDrawableClient == null) {
            DrawableClient lastDrawableClient = queuedDrawableClients.getFirst();
            lastDrawableClient.targetPosition = new Point(DESK_X - CLIENT_WIDTH - 20, DESK_Y + 10);
            lastDrawableClient.movingToDesk = true;
            log("Sending client to desk!");
        }
    }

    private void updateClientPositions() {
        DrawableClient arrivalCandidate = null;

        List<SmokeEffect> finishedEffects = new ArrayList<>();
        for (SmokeEffect effect : smokeEffects) {
            effect.update();
            if (effect.isFinished()) {
                finishedEffects.add(effect);
            }
        }
        smokeEffects.removeAll(finishedEffects);

        for (DrawableClient drawableClient : queuedDrawableClients) {
            if (drawableClient.currentPosition == null || drawableClient.targetPosition == null) {
                continue;
            }

            double dx = drawableClient.targetPosition.x - drawableClient.currentPosition.x;
            double dy = drawableClient.targetPosition.y - drawableClient.currentPosition.y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 2) {
                double speed = 5.0;
                double ratio = speed / distance;
                drawableClient.currentPosition.x += (int) (dx * ratio);
                drawableClient.currentPosition.y += (int) (dy * ratio);
            } else {
                drawableClient.currentPosition.x = drawableClient.targetPosition.x;
                drawableClient.currentPosition.y = drawableClient.targetPosition.y;

                if (drawableClient.movingToDesk) {
                    arrivalCandidate = drawableClient;
                }
            }

        }

        if (arrivalCandidate != null) {
            queuedDrawableClients.remove(arrivalCandidate);

            currentDrawableClient = arrivalCandidate;
            currentDrawableClient.movingToDesk = false;

            updateClientInfo();
            updateClientButtons(true);

            log("Client is ready to be served!");
        }
    }

    private void fadeOutClient() {
        currentDrawableClient = null;
        Point position = new Point(DESK_X - CLIENT_WIDTH - 20, DESK_Y + 10);
        SmokeEffect effect = new SmokeEffect(position.x, position.y, 10);
        smokeEffects.add(effect);

        Timer fadeTimer = new Timer(1500, e -> {
            smokeEffects.remove(effect);
            ((Timer) e.getSource()).stop();
        });
        fadeTimer.setRepeats(false);
        fadeTimer.start();
    }

    private void handleClientRequest(boolean accepted) {
        if (currentDrawableClient == null) return;

        if (accepted) {
            clientsServed++;
            processBankingTransaction(currentDrawableClient);
        } else {
            clientsRejected++;
            reputation -= 5;
        }

        fadeOutClient();
        updateClientInfo();
        updateClientButtons(false);
        updateStats();

        if (deskOpen && !queuedDrawableClients.isEmpty() && !queuedDrawableClients.getFirst().isMovingToDesk()) {
            moveFirstClientToDesk();
        }
    }

    private void processBankingTransaction(DrawableClient drawableClient) {
        switch (drawableClient.requestType) {
            case "Deposit" -> {
                money += drawableClient.amount;
                reputation += 2;
            }
            case "Loan" -> {
                if (money >= drawableClient.amount) {
                    money -= drawableClient.amount;
                    money += (int) (drawableClient.amount * 0.1);
                    reputation += 3;
                } else {
                    money -= 100;
                    reputation -= 10;
                }
            }
            case "Consultation" -> {
                money += 50;
                reputation += 1;
            }
        }
    }

    private void updateClientInfo() {
        if (currentDrawableClient != null) {
            clientInfoArea.setText(
                    "Name: " + currentDrawableClient.name + "\n" +
                            "Age: " + currentDrawableClient.age + "\n" +
                            "Job: " + currentDrawableClient.job + "\n" +
                            "Client Type: " + currentDrawableClient.clientType + "\n" +
                            "Request: " + currentDrawableClient.requestType + "\n" +
                            "Amount: $" + currentDrawableClient.amount + "\n\n" +
                            "Credit Score: " + currentDrawableClient.creditScore + "/100"
            );
        } else {
            clientInfoArea.setText("No client at desk.");
        }
    }

    private void updateStats() {
        statsLabel.setText("Money: $" + money + " | Reputation: " +
                Math.min(100, Math.max(0, reputation)) + "% | " +
                "Served: " + clientsServed + " | Rejected: " + clientsRejected);
    }

    private void drawBankOffice(Graphics g) {
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, HEIGHT / 2, WIDTH, HEIGHT / 2);

        g.setColor(new Color(150, 150, 150));
        g.fillRect(0, QUEUE_Y - 40, 30, 160);
    }

    private void drawDesk(Graphics g) {
        g.setColor(new Color(139, 69, 19));
        g.fillRect(DESK_X, DESK_Y, DESK_WIDTH, DESK_HEIGHT);

        g.setColor(new Color(160, 82, 45));
        g.fillRect(DESK_X + 10, DESK_Y + 10, DESK_WIDTH - 20, DESK_HEIGHT - 20);

        g.setColor(deskOpen ? Color.GREEN : Color.RED);
        g.fillRect(DESK_X + DESK_WIDTH - 60, DESK_Y - 30, 50, 20);
        g.setColor(Color.BLACK);
        g.drawRect(DESK_X + DESK_WIDTH - 60, DESK_Y - 30, 50, 20);
        g.drawString(deskOpen ? "Open" : "Closed", DESK_X + DESK_WIDTH - 55, DESK_Y - 15);

        drawClient(g, player, DESK_X + (DESK_WIDTH / 2) - (CLIENT_WIDTH / 2), DESK_Y + (DESK_HEIGHT / 2) - (CLIENT_HEIGHT / 2));
    }

    private void drawAllClients(Graphics g) {
        if (currentDrawableClient != null) {
            drawClient(g, currentDrawableClient, DESK_X - CLIENT_WIDTH - 20, DESK_Y + 10);
        }

        for (DrawableClient drawableClient : queuedDrawableClients) {
            if (drawableClient.currentPosition != null) {
                drawClient(g, drawableClient, drawableClient.currentPosition.x, drawableClient.currentPosition.y);
            }
        }

        for (SmokeEffect effect : smokeEffects) {
            effect.draw(g);
        }
    }

    private void drawClient(Graphics g, DrawableClient drawableClient, int x, int y) {
        Color bodyColor = switch (drawableClient.clientType) {
            case "Preferred" -> new Color(255, 215, 0);
            case "Bank Officer" -> new Color(65, 105, 225);
            default -> new Color(100, 149, 237);
        };

        g.setColor(bodyColor);
        g.fillRect(x, y, CLIENT_WIDTH, CLIENT_HEIGHT - 30);
        g.setColor(Color.BLACK);
        g.drawString(drawableClient.position, x + 20, y + (CLIENT_HEIGHT / 2));
        g.setColor(bodyColor);

        g.setColor(new Color(255, 222, 173));
        g.fillOval(x + 10, y - 30, CLIENT_WIDTH - 20, 40);

        g.setColor(Color.BLACK);
        g.fillOval(x + 20, y - 20, 5, 5);
        g.fillOval(x + 35, y - 20, 5, 5);

        g.drawArc(x + 20, y - 15, 20, 10, 0, -180);

        g.setColor(new Color(bodyColor.getRed(), bodyColor.getGreen(), bodyColor.getBlue()));

        Color requestColor = switch (drawableClient.requestType) {
            case "Deposit" -> Color.GREEN;
            case "Loan" -> Color.RED;
            default -> Color.YELLOW;
        };

        g.setColor(requestColor);
        g.fillOval(x + CLIENT_WIDTH - 15, y + 5, 10, 10);
    }

    private void drawStats(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Bank Simulator", 10, 20);

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Clients in queue: " + queuedDrawableClients.size(), 10, 40);
    }

    private DrawableClient generateRandomClient() {
        String[] firstNames = {"John", "Emma", "Michael", "Sophia", "David", "Olivia", "James", "Ava", "Robert", "Isabella"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};
        String[] jobs = {"Teacher", "Engineer", "Doctor", "Lawyer", "Artist", "Programmer", "Chef", "Accountant", "Nurse", "Writer"};
        String[] requestTypes = {"Deposit", "Loan", "Consultation"};
        String[] clientTypes = {"Plain", "Preferred", "Bank Officer"};

        String name = firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];
        int age = 20 + random.nextInt(50);
        String job = jobs[random.nextInt(jobs.length)];
        String requestType = requestTypes[random.nextInt(requestTypes.length)];
        String clientType = clientTypes[random.nextInt(clientTypes.length)];

        int amount;
        if (requestType.equals("Deposit")) {
            amount = 500 + random.nextInt(5000);
        } else if (requestType.equals("Loan")) {
            amount = 1000 + random.nextInt(10000);
        } else {
            amount = 0;
        }

        int creditScore = 50 + random.nextInt(51);

        return new DrawableClient(name, age, job, clientType, requestType, amount, creditScore, String.valueOf(queuedDrawableClients.size() + 1));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bank Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new DepositsPlease());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
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
        log(message, LogColor.BLUE);
    }

    public enum LogColor {
        RED, GREEN, BLUE, GRAY
    }

    private class SmokeEffect {
        private final int x;
        private final int y;
        private int lifetime;
        private final int maxLifetime;
        private final List<Point> particles = new ArrayList<>();

        public SmokeEffect(int x, int y, int particleCount) {
            this.x = x;
            this.y = y;
            this.lifetime = 0;
            this.maxLifetime = 30;

            for (int i = 0; i < particleCount; i++) {
                int offsetX = random.nextInt(80) - 40;
                int offsetY = random.nextInt(80) - 40;
                particles.add(new Point(offsetX, offsetY));
            }
        }

        public void update() {
            lifetime++;
        }

        public void draw(Graphics g) {
            if (lifetime >= maxLifetime) return;

            float alpha = 1.0f - ((float) lifetime / maxLifetime);
            int size = 20 + (int) (lifetime * 1.5);

            g.setColor(new Color(0.9f, 0.9f, 0.9f, alpha));

            for (Point p : particles) {
                int growX = p.x * (1 + lifetime / 8);
                int growY = p.y * (1 + lifetime / 10);
                int particleX = x + CLIENT_WIDTH / 2 + growX;
                int particleY = y + CLIENT_HEIGHT / 2 + growY;

                g.fillOval(particleX - size / 2, particleY - size / 2, size, size);
            }

            if (lifetime < 15) {
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(new Color(0.1f, 0.1f, 0.1f, 1.0f - lifetime / 15.0f));
                g.drawString("POOF!", x + 10, y - 10);
            }
        }

        public boolean isFinished() {
            return lifetime >= maxLifetime;
        }
    }
}