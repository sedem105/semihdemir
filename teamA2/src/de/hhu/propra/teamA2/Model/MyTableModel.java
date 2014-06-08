package de.hhu.propra.teamA2.Model;



import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public   class MyTableModel extends AbstractTableModel{
	private List<String> columnNames = new ArrayList();
    private List<List> data = new ArrayList();

    {
        columnNames.add("Name");
        columnNames.add("Chat");
       
    }

    public void addRow(List rowData)
    {
        data.add(rowData);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
        System.out.println("add");
    }

    public int getColumnCount()
    {
        return columnNames.size();
    }

    public int getRowCount()
    {
        return data.size();
    }

    public String getColumnName(int col)
    {
        try
        {
            return columnNames.get(col);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public Object getValueAt(int row, int col)
    {
        return data.get(row).get(col);
    }

    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }
}
