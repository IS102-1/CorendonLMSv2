/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corendonlmsv2.view.panels;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.main.CorendonLMSv2;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.DatabaseTables;
import corendonlmsv2.model.Luggage;
import corendonlmsv2.model.UserAccount;
import corendonlmsv2.view.NonEditableTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Emile Pels
 */
public class LuggageManager
        extends JPanel implements ActionListener, DocumentListener
{

    private final JPanel parent;
    private final NonEditableTableModel model;
    private final TableRowSorter<NonEditableTableModel> sorter;

    /**
     * Creates new form LuggageManager
     *
     * @param parent Parent panel to display when luggage manager is closed
     */
    public LuggageManager(JPanel parent)
    {
        new ActionLog(UserAccount.getCurrent(),
                "Accessed luggage manager").insert();

        this.parent = parent;
        initComponents();

        jLabel1.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        jLabel2.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        setBackground(CorendonLMSv2.DEFAULT_BACKCOLOR);

        model = (NonEditableTableModel) luggageTable.getModel();
        sorter = new TableRowSorter<>(model);
        luggageTable.setRowSorter(sorter);

        loadLuggage();
        registerListeners();
    }

    /**
     * Registers event listeners
     */
    private void registerListeners()
    {
        backButton.addActionListener(this);
        deleteButton.addActionListener(this);
        detailsButton.addActionListener(this);
        editButton.addActionListener(this);
        generatePdfButton.addActionListener(this);
        registerButton.addActionListener(this);
        searchField.getDocument().addDocumentListener(this);
    }

    /**
     * Sets a filter for the table
     */
    private void search()
    {
        try
        {
            //(?i) is for case-insensitivity
            RowFilter<NonEditableTableModel, Object> filter
                    = RowFilter.regexFilter("(?i)" + searchField.getText());

            sorter.setRowFilter(filter);
        } catch (Exception ex)
        {
            //Leave filter as it is
        }
    }

    /**
     * Removes any entries from the table and reloads it with all luggage
     */
    private void loadLuggage()
    {
        List<Luggage> allLuggage = Luggage.getAllLuggage();
        resetLuggage();

        for (Luggage luggage : allLuggage)
        {
            model.addRow(new Object[]
            {
                luggage.getLuggageId(),
                luggage.getBrandName(),
                luggage.getColor(),
                luggage.getCustomerId(),
                luggage.getDetails(),
                luggage.getLocation(),
                luggage.getStatus(),
                luggage.getDateMissing()
            });
        }
    }

    private void resetLuggage()
    {
        model.setRowCount(0);
    }

    /**
     * Gets the selected luggage's ID. Returns null and shows warning message if
     * no luggage is selected
     *
     * @return Value of the selected luggage ID. Null if not applicable
     */
    private String getSelectedLuggageId()
    {
        int selectedRow = luggageTable.getSelectedRow();

        if (selectedRow == -1)
        {
            MiscUtil.showMessage("Please select a valid luggage entry before "
                    + "proceeding.");
            return null;
        }

        return (String) luggageTable.getValueAt(selectedRow, 0);
    }

    private void deleteSelected()
    {
        String selectedId = getSelectedLuggageId();
        String luggageIdColumn = Luggage.TABLE.getColumnAt(
                Luggage.TableColumns.LUGGAGE_ID);

        if (selectedId != null)
        {
            String update = String.format("DELETE FROM %s WHERE %s=%s",
                    Luggage.TABLE.getDatabaseIdentifier(),
                    luggageIdColumn, selectedId);

            DbManager.executeUpdate(update);

            new ActionLog(UserAccount.getCurrent(),
                    "Deleted luggage " + selectedId).insert();

            MiscUtil.showMessage("The selected luggage has been deleted!");
            loadLuggage();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object source = ae.getSource();

        if (source == backButton)
        {
            CorendonLMSv2.MAIN_FRAME.displayPanel(parent);
        } else if (source == deleteButton)
        {
            deleteSelected();
        } else if (source == detailsButton)
        {
            String selectedId = getSelectedLuggageId();
            if (selectedId != null)
            {
                MiscUtil.showMessage(String.format("Details for luggage "
                        + "(ID: %s):\n\n%s",
                        selectedId,
                        model.getValueAt(luggageTable.getSelectedRow(), 4)));
            }
        } else if (source == editButton)
        {

        } else if (source == generatePdfButton)
        {

        } else if (source == registerButton)
        {

        }
    }

    @Override
    public void insertUpdate(DocumentEvent de)
    {
        search();
    }

    @Override
    public void removeUpdate(DocumentEvent de)
    {
        search();
    }

    @Override
    public void changedUpdate(DocumentEvent de)
    {
        search();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        backButton = new javax.swing.JButton();
        registerButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        luggageTable = new javax.swing.JTable();
        detailsButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        generatePdfButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        backButton.setText("Back");

        registerButton.setText("Register new luggage");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Search:");

        luggageTable.setModel(new NonEditableTableModel(DatabaseTables.LUGGAGE.getColumns(), 0));
        jScrollPane1.setViewportView(luggageTable);

        detailsButton.setText("View details");

        editButton.setText("Edit luggage");

        deleteButton.setText("Delete luggage");

        generatePdfButton.setText("Generate PDF");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Modify selected:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(108, 108, 108)
                        .addComponent(generatePdfButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(detailsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(263, 263, 263)
                        .addComponent(registerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(backButton, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(detailsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generatePdfButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton detailsButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton generatePdfButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable luggageTable;
    private javax.swing.JButton registerButton;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
}
