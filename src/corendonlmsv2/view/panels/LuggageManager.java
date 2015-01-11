package corendonlmsv2.view.panels;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.connectivity.LanguageController;
import corendonlmsv2.main.CorendonLMSv2;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.main.util.PdfUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.DatabaseTables;
import corendonlmsv2.model.Luggage;
import corendonlmsv2.model.UserAccount;
import corendonlmsv2.view.NonEditableTableModel;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
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
        
        model = (NonEditableTableModel) luggageTable.getModel();
        sorter = new TableRowSorter<>(model);
        luggageTable.setRowSorter(sorter);

        loadLuggage();
        registerListeners();
        setComponentProperties();
    }
    
    private void setComponentProperties()
    {
        jLabel1.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        jLabel2.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        refreshLabel.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        refreshLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBackground(CorendonLMSv2.DEFAULT_BACKCOLOR);
        
        refreshLabel.setText(LanguageController.getString("refresh"));
        backButton.setText(LanguageController.getString("back"));
        jLabel1.setText(LanguageController.getString("search"));
        jLabel2.setText(LanguageController.getString("modifySelected"));
        generatePdfButton.setText(LanguageController.getString("generatePdf"));
        deleteButton.setText(LanguageController.getString("deleteLuggage"));
        editButton.setText(LanguageController.getString("editLuggage"));
        detailsButton.setText(LanguageController.getString("viewDetails"));
        registerButton.setText(LanguageController.getString("registerNewLuggage"));
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
                luggage.getDateEntered()
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
            MiscUtil.showMessage(LanguageController.getString("selectLuggage"));
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

            MiscUtil.showMessage(
                    LanguageController.getString("selectedLuggageDeleted"));
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
            String selectedId = getSelectedLuggageId();
            if (selectedId != null)
            {
                new EditLuggage(selectedId, true).setVisible(true);
            }
        } else if (source == generatePdfButton)
        {
            String selectedId = getSelectedLuggageId();
            if (selectedId != null)
            {
                String result;
                
                try
                {
                    result = PdfUtil.generatePdf(selectedId);
                } catch (IOException ex)
                {
                    MiscUtil.showMessage(
                            LanguageController.getString("pdfGenFailed") 
                                    + "\n\n" + ex.getMessage());
                    return;
                }
                
                new ActionLog(UserAccount.getCurrent(), "Generated report for"
                        + " luggage " + selectedId).insert();
                PdfUtil.launchPdf(result);
            }
        } else if (source == registerButton)
        {
            int result = JOptionPane.showConfirmDialog(this, 
                    LanguageController.getString("addToExisting"));
            
            if (result == JOptionPane.YES_OPTION)
            {
                CorendonLMSv2.MAIN_FRAME.displayPanel(new CustomerManager(this));
            } else if (result == JOptionPane.NO_OPTION)
            {
                new EditLuggage().setVisible(true);
            }
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
        refreshLabel = new javax.swing.JLabel();

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

        refreshLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        refreshLabel.setText("Refresh");
        refreshLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                refreshLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(refreshLabel)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(46, 46, 46)
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
                        .addGap(273, 273, 273)
                        .addComponent(registerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(backButton, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)))
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
                    .addComponent(jLabel2)
                    .addComponent(refreshLabel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshLabelMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_refreshLabelMouseClicked
    {//GEN-HEADEREND:event_refreshLabelMouseClicked
        loadLuggage();
    }//GEN-LAST:event_refreshLabelMouseClicked


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
    private javax.swing.JLabel refreshLabel;
    private javax.swing.JButton registerButton;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
}
