package dev.ua.ikeepcalm.ui;

import dev.ua.ikeepcalm.app.depositors.Depositor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class DepositSimulationPanel extends AppMainFrame {
    
    private final JTextField amountField;
    private final JTextField monthsField;
    private final JTextField accelerationField;
    private final JProgressBar progressBar;
    private final JLabel timeRemainingLabel;
    private final JLabel projectedResultLabel;
    private final JLabel currentValueLabel;
    private final JLabel profitMarginLabel;
    private final JButton startButton;
    private final JButton stopButton;
    
    private Depositor currentDepositor;
    private Timer simulationTimer;
    private int currentMonth = 0;
    private int totalMonths = 0;
    private int depositAmount = 0;
    private int currentValue = 0;
    
    public DepositSimulationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Deposit Simulation"));
        
        JPanel paramsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        paramsPanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        paramsPanel.add(new JLabel("Deposit Amount:"));
        amountField = new JTextField("1000");
        paramsPanel.add(amountField);
        
        paramsPanel.add(new JLabel("Deposit Term (months):"));
        monthsField = new JTextField("12");
        paramsPanel.add(monthsField);
        
        paramsPanel.add(new JLabel("Simulation Speed (1 month = X seconds):"));
        accelerationField = new JTextField("1");
        paramsPanel.add(accelerationField);
        
        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        JPanel resultsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        resultsPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        resultsPanel.add(new JLabel("Time Remaining:"));
        timeRemainingLabel = new JLabel("0 months");
        resultsPanel.add(timeRemainingLabel);
        
        resultsPanel.add(new JLabel("Projected Result:"));
        projectedResultLabel = new JLabel("0");
        resultsPanel.add(projectedResultLabel);
        
        resultsPanel.add(new JLabel("Current Value:"));
        currentValueLabel = new JLabel("0");
        resultsPanel.add(currentValueLabel);
        
        resultsPanel.add(new JLabel("Profit Margin:"));
        profitMarginLabel = new JLabel("0%");
        resultsPanel.add(profitMarginLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        startButton = new JButton("Start Simulation");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });
        buttonPanel.add(startButton);
        
        stopButton = new JButton("Stop Simulation");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopSimulation();
            }
        });
        buttonPanel.add(stopButton);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(paramsPanel, BorderLayout.NORTH);
        topPanel.add(progressPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setEnabled(false);
    }
    
    public void setDepositor(Depositor depositor) {
        this.currentDepositor = depositor;
        boolean hasDepositor = (depositor != null);
        
        setEnabled(hasDepositor);
        startButton.setEnabled(hasDepositor);
        
        if (!hasDepositor) {
            resetSimulation();
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        amountField.setEnabled(enabled);
        monthsField.setEnabled(enabled);
        accelerationField.setEnabled(enabled);
        startButton.setEnabled(enabled && simulationTimer == null);
    }
    
    private void startSimulation() {
        try {
            depositAmount = Integer.parseInt(amountField.getText().trim());
            totalMonths = Integer.parseInt(monthsField.getText().trim());
            int accelerationFactor = Integer.parseInt(accelerationField.getText().trim());
            
            if (depositAmount <= 0 || totalMonths <= 0 || accelerationFactor <= 0) {
                JOptionPane.showMessageDialog(this, 
                        "Amount, months, and simulation speed must be positive values", 
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentMonth = 0;
            currentValue = depositAmount;
            progressBar.setValue(0);
            progressBar.setMaximum(totalMonths);
            
            int projectedResult = currentDepositor.imitateDeposit(depositAmount, totalMonths);
            currentDepositor.setDeposited(currentDepositor.getDeposited() - depositAmount);
            currentDepositor.setObtained(currentDepositor.getObtained() - projectedResult);
            
            projectedResultLabel.setText(String.valueOf(projectedResult));
            updateDisplay();

            amountField.setEnabled(false);
            monthsField.setEnabled(false);
            accelerationField.setEnabled(false);
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            
            int delay = accelerationFactor * 1000;
            simulationTimer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateSimulation();
                }
            });
            simulationTimer.start();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter valid numbers for amount, months, and simulation speed", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void stopSimulation() {
        if (simulationTimer != null) {
            simulationTimer.stop();
            simulationTimer = null;
        }
        
        amountField.setEnabled(true);
        monthsField.setEnabled(true);
        accelerationField.setEnabled(true);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }
    
    private void resetSimulation() {
        stopSimulation();
        currentMonth = 0;
        currentValue = 0;
        progressBar.setValue(0);
        timeRemainingLabel.setText("0 months");
        projectedResultLabel.setText("0");
        currentValueLabel.setText("0");
        profitMarginLabel.setText("0%");
    }
    
    private void updateSimulation() {
        currentMonth++;

        int projectedFinal = Integer.parseInt(projectedResultLabel.getText());
        float progress = (float) currentMonth / totalMonths;
        currentValue = depositAmount + (int)((projectedFinal - depositAmount) * progress);
        
        updateDisplay();
        
        if (currentMonth >= totalMonths) {
            simulationTimer.stop();
            simulationTimer = null;
            
            int result = currentDepositor.imitateDeposit(depositAmount, totalMonths);
            currentValue = result;
            updateDisplay();
            
            JOptionPane.showMessageDialog(this, 
                    "Deposit simulation completed!\nFinal amount: " + result, 
                    "Simulation Complete", JOptionPane.INFORMATION_MESSAGE);
            
            amountField.setEnabled(true);
            monthsField.setEnabled(true);
            accelerationField.setEnabled(true);
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }
    
    private void updateDisplay() {
        progressBar.setValue(currentMonth);
        timeRemainingLabel.setText((totalMonths - currentMonth) + " months");
        currentValueLabel.setText(String.valueOf(currentValue));
        
        if (depositAmount > 0) {
            double profitMargin = ((double)(currentValue - depositAmount) / depositAmount) * 100;
            DecimalFormat df = new DecimalFormat("0.00");
            profitMarginLabel.setText(df.format(profitMargin) + "%");
        }
    }
}