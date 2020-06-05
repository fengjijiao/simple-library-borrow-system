package test7.view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

public class JTableUnEdited extends JTable {
    private Object[][] DataSet;
    private Object[] ColumnNames;

    public JTableUnEdited(TableModel dm) {
        super(dm);
    }

    public JTableUnEdited(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        this.DataSet = rowData;
        this.ColumnNames = columnNames;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void addRowSelectListener(RowSelect callBack) {
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = getSelectedRow();
                if(row != -1) {
                    callBack.SelectChanged(row);
                }
            }
        });
    }

    public interface RowSelect {
        void SelectChanged(int row);
    }
}
