package dev.ua.ikeepcalm.ui.models;

import dev.ua.ikeepcalm.app.depositors.Depositor;
import dev.ua.ikeepcalm.app.utils.AnalyzerUtil;
import dev.ua.ikeepcalm.ui.AppMainFrame;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
import java.util.List;


public abstract class DepositorTableModel extends Depositor {

    private final List<Depositor> depositors;
    private final String[] columnNames = {"ID", "Name", "Type", "Bank", "Deposited", "Obtained", "Profit"};
    private final boolean[] editableColumns = {false, true, false, false, false, false, false};

    public DepositorTableModel(List<Depositor> depositors) {
        this.depositors = depositors;
    }

    @Override
    public int getRowCount() {
        return depositors.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editableColumns[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= depositors.size()) {
            return null;
        }

        Depositor depositor = depositors.get(rowIndex);
        DecimalFormat df = new DecimalFormat("#.##");

        return switch (columnIndex) {
            case 0 -> depositor.getId();
            case 1 -> depositor.getName();
            case 2 -> depositor.getClass().getSimpleName();
            case 3 -> depositor.getBank().getClass().getSimpleName();
            case 4 -> depositor.getDeposited();
            case 5 ->    depositor.getObtained();
            case 6 -> {
                double profitMargin = AnalyzerUtil.calculateProfitMargin(depositor);
                yield df.format(profitMargin) + "%";
            }
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (rowIndex >= depositors.size()) {
            return;
        }

        Depositor depositor = depositors.get(rowIndex);

        if (columnIndex == 1 && value instanceof String newName) {
            if (!newName.trim().isEmpty()) {
                updateDepositorName(depositor, newName);
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
    }

    private void updateDepositorName(Depositor depositor, String newName) {
        depositor.setName(newName);
    }

    public void refreshData() {
        fireTableDataChanged();
    }
}