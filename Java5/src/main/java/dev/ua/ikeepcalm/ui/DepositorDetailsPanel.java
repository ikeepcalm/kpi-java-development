package dev.ua.ikeepcalm.ui;

import dev.ua.ikeepcalm.app.depositors.Depositor;
import dev.ua.ikeepcalm.app.depositors.impls.BankOfficer;
import dev.ua.ikeepcalm.app.depositors.impls.PlainDepositor;
import dev.ua.ikeepcalm.app.depositors.impls.PreferredDepositor;
import dev.ua.ikeepcalm.app.utils.AnalyzerUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class DepositorDetailsPanel extends AppMainFrame {

    private final JLabel idLabel;
    private final JLabel nameLabel;
    private final JLabel typeLabel;
    private final JLabel bankLabel;
    private final JLabel depositedLabel;
    private final JLabel obtainedLabel;
    private final JLabel profitMarginLabel;
    private final JLabel bonusLabel;
    private final JPanel extraInfoPanel;

    JButton refreshButton;
    private final AppMainFrame mainFrame;
    private Depositor currentDepositor;

    public DepositorDetailsPanel(AppMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Depositor Details"));

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        infoPanel.add(new JLabel("ID:"));
        idLabel = new JLabel();
        infoPanel.add(idLabel);

        infoPanel.add(new JLabel("Name:"));
        nameLabel = new JLabel();
        infoPanel.add(nameLabel);

        infoPanel.add(new JLabel("Type:"));
        typeLabel = new JLabel();
        infoPanel.add(typeLabel);

        infoPanel.add(new JLabel("Bank:"));
        bankLabel = new JLabel();
        infoPanel.add(bankLabel);

        infoPanel.add(new JLabel("Deposited:"));
        depositedLabel = new JLabel();
        infoPanel.add(depositedLabel);

        infoPanel.add(new JLabel("Obtained:"));
        obtainedLabel = new JLabel();
        infoPanel.add(obtainedLabel);

        infoPanel.add(new JLabel("Profit Margin:"));
        profitMarginLabel = new JLabel();
        infoPanel.add(profitMarginLabel);

        infoPanel.add(new JLabel("Bonus:"));
        bonusLabel = new JLabel();
        infoPanel.add(bonusLabel);

        add(infoPanel, BorderLayout.NORTH);

        extraInfoPanel = new JPanel(new BorderLayout());
        extraInfoPanel.setBorder(BorderFactory.createTitledBorder("Special Features"));
        add(extraInfoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshButton = new JButton("Refresh Details");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentDepositor != null) {
                    updateDetails(currentDepositor);
                }
            }
        });
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void clearDetails() {
        this.currentDepositor = null;

        idLabel.setText("");
        nameLabel.setText("");
        typeLabel.setText("");
        bankLabel.setText("");
        depositedLabel.setText("");
        obtainedLabel.setText("");
        profitMarginLabel.setText("");
        bonusLabel.setText("");

        extraInfoPanel.removeAll();
        extraInfoPanel.revalidate();
        extraInfoPanel.repaint();
    }

    public void updateDetails(Depositor depositor) {
        this.currentDepositor = depositor;

        idLabel.setText(String.valueOf(depositor.getId()));
        nameLabel.setText(depositor.getName());
        typeLabel.setText(depositor.getClass().getSimpleName());
        bankLabel.setText(depositor.getBank().getClass().getSimpleName());
        depositedLabel.setText(String.valueOf(depositor.getDeposited()));
        obtainedLabel.setText(String.valueOf(depositor.getObtained()));

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double profitMargin = AnalyzerUtil.calculateProfitMargin(depositor);
        profitMarginLabel.setText(decimalFormat.format(profitMargin) + "%");

        bonusLabel.setText(depositor.getBonus() + " (" + (depositor.getBonus() * 100) + "%)");

        extraInfoPanel.removeAll();

        switch (depositor) {
            case PlainDepositor plainDepositor -> setupPlainDepositorPanel(plainDepositor);
            case PreferredDepositor preferredDepositor -> setupPreferredDepositorPanel(preferredDepositor);
            case BankOfficer bankOfficer -> setupBankOfficerPanel(bankOfficer);
            default -> {
            }
        }

        extraInfoPanel.revalidate();
        extraInfoPanel.repaint();
    }

    private void setupPlainDepositorPanel(PlainDepositor depositor) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel priorityLabel = new JLabel("Priority: " + depositor.getPriority());
        panel.add(priorityLabel, BorderLayout.NORTH);

        JButton brouhahaButton = new JButton("Start Brouhaha");
        brouhahaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                brouhahaButton.setEnabled(false);
                refreshButton.setEnabled(false);
                mainFrame.startBrouhaha();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        depositor.startBrouhaha();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                brouhahaButton.setEnabled(true);
                                refreshButton.setEnabled(true);
                                mainFrame.stopBrouhaha();
                            }
                        });
                    }
                }).start();
            }
        });
        panel.add(brouhahaButton, BorderLayout.CENTER);

        extraInfoPanel.add(panel, BorderLayout.CENTER);
    }

    private void setupPreferredDepositorPanel(PreferredDepositor depositor) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel priorityLabel = new JLabel("Priority: " + depositor.getPriority());
        panel.add(priorityLabel, BorderLayout.NORTH);

        JLabel infoLabel = new JLabel("<html>Preferred depositors receive enhanced interest rates<br>" +
                "and special benefits with a bonus rate of " + (depositor.getBonus() * 100) + "%</html>");
        panel.add(infoLabel, BorderLayout.CENTER);

        extraInfoPanel.add(panel, BorderLayout.CENTER);
    }

    private void setupBankOfficerPanel(BankOfficer depositor) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        BankOfficer.EmployeeRole role = depositor.getRole();

        panel.add(new JLabel("Role:"));
        panel.add(new JLabel(role.role()));

        panel.add(new JLabel("Department:"));
        panel.add(new JLabel(role.department()));

        panel.add(new JLabel("Position:"));
        panel.add(new JLabel(role.position()));

        extraInfoPanel.add(panel, BorderLayout.CENTER);
    }
}