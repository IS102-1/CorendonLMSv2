package corendonlmsv2.view;

import javax.swing.table.DefaultTableModel;

/**
 * Provides a table model with non-editable cells
 * Credits to nelson eldoro @ Stackoverflow
 *
 * @author Emile Pels
 */
public class NonEditableTableModel extends DefaultTableModel
{

    public NonEditableTableModel(Object[] columnNames, int rowCount)
    {
        super(columnNames, rowCount);
    }
    
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
}
