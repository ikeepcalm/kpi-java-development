package dev.ua.ikeepcalm.ui;

import dev.ua.ikeepcalm.BankApp;
import dev.ua.ikeepcalm.app.bank.Bank;
import dev.ua.ikeepcalm.app.bank.impls.PrivatBank;
import dev.ua.ikeepcalm.app.bank.impls.UniversalBank;
import dev.ua.ikeepcalm.app.depositors.Depositor;
import dev.ua.ikeepcalm.app.depositors.impls.BankOfficer;
import dev.ua.ikeepcalm.app.depositors.impls.PlainDepositor;
import dev.ua.ikeepcalm.app.depositors.impls.PreferredDepositor;
import dev.ua.ikeepcalm.ui.models.DepositorTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class AppMainFrame extends JFrame {

    private final List<Depositor> depositors = new ArrayList<>();
    private final DepositorTableModel depositorTableModel;
    private final JTable depositorTable;
    private final DepositorDetailsPanel detailsPanel;
    private final DepositSimulationPanel simulationPanel;
    private final JComboBox<String> depositorTypeComboBox;
    private final JComboBox<String> bankTypeComboBox;
    private final JTextField nameField;
    private final JTextField idField;
    private final JLabel statusLabel;
    private final Timer statusTimer;

    private final JButton createDepositorButton;

    public AppMainFrame() {
        setTitle("Bank Deposit Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        setJMenuBar(createMenuBar());

        JPanel createPanel = new JPanel(new BorderLayout());
        createPanel.setBorder(BorderFactory.createTitledBorder("Create New Depositor"));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Depositor Type:"));
        depositorTypeComboBox = new JComboBox<>(new String[]{"Plain Depositor", "Preferred Depositor", "Bank Officer"});
        formPanel.add(depositorTypeComboBox);

        formPanel.add(new JLabel("Bank:"));
        bankTypeComboBox = new JComboBox<>(
                new String[]{"Privat Bank", "Universal Bank"}
        );
        formPanel.add(bankTypeComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton createButton = new JButton("Create Depositor");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDepositor();
            }
        });
        this.createDepositorButton = createButton;
        buttonPanel.add(createButton);

        createPanel.add(formPanel, BorderLayout.CENTER);
        createPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Depositors"));

        depositorTableModel = new DepositorTableModel(depositors);
        depositorTable = new JTable(depositorTableModel);
        depositorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateSelection();
                }
            }
        });
        depositorTable.setRowHeight(25);
        depositorTable.setShowVerticalLines(false);

        depositorTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
                    int row = e.getFirstRow();
                    if (row >= 0 && row < depositors.size()) {
                        Depositor updatedDepositor = depositors.get(row);
                        setStatus("Updated depositor name: " + updatedDepositor.getName());

                        if (depositorTable.getSelectedRow() == row) {
                            detailsPanel.updateDetails(updatedDepositor);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(depositorTable);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete Selected Depositor");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedDepositor();
            }
        });

        detailsPanel = new DepositorDetailsPanel(this);
        simulationPanel = new DepositSimulationPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Depositor Details", detailsPanel);
        tabbedPane.addTab("Deposit Simulation", simulationPanel);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(createPanel, BorderLayout.NORTH);
        leftPanel.add(listPanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, tabbedPane);
        splitPane.setDividerLocation(600);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        statusLabel = new JLabel("Ready");
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        statusTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Ready");
            }
        });
        statusTimer.setRepeats(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (statusTimer.isRunning()) {
                    statusTimer.stop();
                }
            }
        });

        add(mainPanel);
        setStatus("Application started. Ready to create depositors.");
    }

    private void createDepositor() {
        try {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                showError("ID cannot be empty");
                return;
            }
            int id = Integer.parseInt(idText);

            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showError("Name cannot be empty");
                return;
            }

            String depositorType = (String) depositorTypeComboBox.getSelectedItem();
            String bankType = (String) bankTypeComboBox.getSelectedItem();

            Bank bank = "Privat Bank".equals(bankType) ? new PrivatBank() : new UniversalBank();

            Depositor depositor;
            switch (depositorType) {
                case "Plain Depositor" -> depositor = new PlainDepositor(id, name, bank);
                case "Preferred Depositor" -> depositor = new PreferredDepositor(id, name, bank);
                case "Bank Officer" -> depositor = new BankOfficer(id, name, bank,
                        new BankOfficer.EmployeeRole("Officer", "Banking", "Junior"));
                case null -> {
                    BankApp.log("Depositor type cannot be recognized!", BankApp.LogColor.RED);
                    return;
                }
                default -> {
                    showError("Invalid depositor type");
                    return;
                }
            }

            for (Depositor existing : depositors) {
                if (existing.getId() == id) {
                    showError("A depositor with ID " + id + " already exists");
                    return;
                }
            }

            depositors.add(depositor);
            updateDepositorTable();
            setStatus("Depositor created: " + name + " (ID: " + id + ")");

            idField.setText("");
            nameField.setText("");

        } catch (NumberFormatException e) {
            showError("Please enter a valid numeric ID");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error creating depositor: " + e.getMessage());
        }
    }

    private void updateSelection() {
        int selectedRow = depositorTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < depositors.size()) {
            Depositor selected = depositors.get(selectedRow);
            detailsPanel.updateDetails(selected);
            simulationPanel.setDepositor(selected);
            setStatus("Selected: " + selected.getName() + " (ID: " + selected.getId() + ")");
        } else {
            detailsPanel.clearDetails();
            simulationPanel.setDepositor(null);
        }
    }

    private void deleteSelectedDepositor() {
        int selectedRow = depositorTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < depositors.size()) {
            Depositor selected = depositors.get(selectedRow);
            String name = selected.getName();
            int id = selected.getId();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete depositor: " + name + " (ID: " + id + ")?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                depositors.remove(selectedRow);
                updateDepositorTable();
                setStatus("Depositor deleted: " + name + " (ID: " + id + ")");
            }
        } else {
            showError("Please select a depositor to delete");
        }
    }

    private void updateDepositorTable() {
        depositorTableModel.refreshData();
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
        if (statusTimer.isRunning()) {
            statusTimer.restart();
        } else {
            statusTimer.start();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        setStatus("Error: " + message);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Menu");

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        AppMainFrame.this,
                        "Are you sure you want to exit?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
        fileMenu.add(new JSeparator());
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        return menuBar;
    }

    public void createSampleData() {
        if (!depositors.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "This will replace all existing depositors. Continue?",
                    "Confirm Create Sample Data",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            depositors.clear();
        }

        PrivatBank privatBank = new PrivatBank();
        UniversalBank universalBank = new UniversalBank();

        PlainDepositor plainDepositor = new PlainDepositor(1, "John Smith", privatBank);
        plainDepositor.imitateDeposit(1000, 12);
        depositors.add(plainDepositor);

        PreferredDepositor preferredDepositor = new PreferredDepositor(2, "Emma Johnson", universalBank);
        preferredDepositor.imitateDeposit(2000, 6);
        depositors.add(preferredDepositor);

        BankOfficer officer = new BankOfficer(3, "Michael Brown", privatBank, new BankOfficer.EmployeeRole("Manager", "Loans", "Senior"));
        officer.imitateDeposit(1500, 9);
        depositors.add(officer);

        PlainDepositor anotherPlain = new PlainDepositor(4, "Sarah Wilson", universalBank);
        anotherPlain.imitateDeposit(800, 3);
        depositors.add(anotherPlain);

        updateDepositorTable();
        if (!depositors.isEmpty()) {
            depositorTable.setRowSelectionInterval(0, 0);
            updateSelection();
        }

        setStatus("Sample data created successfully");
    }

    public void startBrouhaha() {
        setStatus("Brouhaha initiated. We cannot do anything with it!");
        createDepositorButton.setEnabled(false);
        nameField.setEnabled(false);
        bankTypeComboBox.setEnabled(false);
        depositorTypeComboBox.setEnabled(false);
    }

    public void stopBrouhaha() {
        nameField.setEnabled(true);
        bankTypeComboBox.setEnabled(true);
        depositorTypeComboBox.setEnabled(true);
        createDepositorButton.setEnabled(true);
        setStatus("Brouhaha stopped. Finally, we can resume our work...");
    }

}