package corendonlmsv2.view.panels;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.connectivity.LanguageController;
import corendonlmsv2.main.CorendonLMSv2;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.Customer;
import corendonlmsv2.model.DatabaseTables;
import corendonlmsv2.model.UserAccount;
import corendonlmsv2.view.NonEditableTableModel;
import java.awt.Cursor;
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
 * @author Daan Bol
 */
public class CustomerManager
        extends JPanel implements ActionListener, DocumentListener
{

    private final JPanel parent;
    private final NonEditableTableModel model;
    private final TableRowSorter<NonEditableTableModel> sorter;

    /**
     * Creates new form CustomerManager
     *
     * @param parent Parent panel to display when customer manager is closed
     */
    public CustomerManager(JPanel parent)
    {
        new ActionLog(UserAccount.getCurrent(), 
                "Accessed customer manager").insert();
        
        this.parent = parent;
        initComponents();
        
        model = (NonEditableTableModel) customerTable.getModel();
        sorter = new TableRowSorter<>(model);
        customerTable.setRowSorter(sorter);
        
        loadCustomers();
        registerListeners();
        
        setComponentProperties();
    }
    
    /**
     * Sets the components' properties
     */
    private void setComponentProperties()
    {
        jLabel1.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        jLabel2.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        refreshLabel.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        refreshLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBackground(CorendonLMSv2.DEFAULT_BACKCOLOR);
        
        backButton.setText(LanguageController.getString("back"));
        registerCustomerButton.setText(
                LanguageController.getString("registerCustomer"));
        refreshLabel.setText(LanguageController.getString("refresh"));
        jLabel1.setText(LanguageController.getString("modifySelected"));
        registerLuggageButton.setText(
                LanguageController.getString("registerLuggage"));
        deleteButton.setText(LanguageController.getString("deleteCustomer"));
        editButton.setText(LanguageController.getString("editCustomer"));
    }
    
    /**
     * Registers event listeners
     */
    private void registerListeners()
    {
        backButton.addActionListener(this);
        deleteButton.addActionListener(this);
        editButton.addActionListener(this);
        registerCustomerButton.addActionListener(this);
        registerLuggageButton.addActionListener(this);
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
            //Intentionally left blank
        }
    }

    /**
     * Removes any entries from the table and reloads it with customers
     */
    private void loadCustomers()
    {
        List<Customer> customers = Customer.getAllCustomers();
        resetCustomers();

        for (Customer customer : customers)
        {
            model.addRow(new String[]
            {
                customer.getCustomerId(),
                customer.getName(),
                customer.getAddress(),
                customer.getCountry(),
                customer.getEmailAddress(),
                customer.getPhoneNumber()
            });
        }
    }

    /**
     * Removes any entries from the table
     */
    private void resetCustomers()
    {
        model.setRowCount(0);
    }
    
    /**
     * Gets the selected customer's ID. Returns null and shows warning message 
     * if no customer is selected
     *
     * @return Value of the selected customer ID. Null if not applicable
     */
    private String getSelectedCustomerId()
    {
        int selectedRow = customerTable.getSelectedRow();

        if (selectedRow == -1)
        {
            MiscUtil.showMessage(LanguageController.getString("selectCustomer"));
            return null;
        }

        return (String) customerTable.getValueAt(selectedRow, 0);
    }
    
    /**
     * Deletes the selected customer from the database
     */
    private void deleteSelected()
    {
        String selectedId = getSelectedCustomerId();
        String customerIdColumn = Customer.TABLE.getColumnAt(
                Customer.TableColumns.CUSTOMER_ID);
        
        if (selectedId != null)
        {
            String update = String.format("DELETE FROM %s WHERE %s=%s",
                    Customer.TABLE.getDatabaseIdentifier(),
                    customerIdColumn, selectedId);
            
            DbManager.executeUpdate(update);
            
            new ActionLog(UserAccount.getCurrent(), 
                String.format("Deleted customer " + selectedId)).insert();
            
            MiscUtil.showMessage(LanguageController.getString("customerDeleted"));
            loadCustomers();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object source = ae.getSource();
        
        if (source == backButton)
        {
            CorendonLMSv2.MAIN_FRAME.displayPanel(parent);
        } else if (source == editButton)
        {
            String customerId = getSelectedCustomerId();
            if (customerId != null)
            {
                new EditCustomer(customerId).setVisible(true);
            }
        }  else if (source == registerCustomerButton)
        {
            new EditCustomer().setVisible(true);
        }  else if (source == registerLuggageButton)
        {
            String customerId = getSelectedCustomerId();
            if (customerId != null)
            {
                new EditLuggage(customerId, false).setVisible(true);
            }
        }  else if (source == deleteButton)
        {
            deleteSelected();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        registerCustomerButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        registerLuggageButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        refreshLabel = new javax.swing.JLabel();

        customerTable.setModel(new NonEditableTableModel(DatabaseTables.CUSTOMERS.getColumns(), 0));
        jScrollPane1.setViewportView(customerTable);

        registerCustomerButton.setText("Register new customer");

        backButton.setText("Back");

        registerLuggageButton.setText("Register luggage");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Modify selected:");

        deleteButton.setText("Delete customer");

        editButton.setText("Edit customer");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Search:");

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
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(157, 157, 157)
                        .addComponent(registerCustomerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(backButton, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(refreshLabel)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(34, 34, 34)
                        .addComponent(registerLuggageButton, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerLuggageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshLabel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshLabelMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_refreshLabelMouseClicked
    {//GEN-HEADEREND:event_refreshLabelMouseClicked
        loadCustomers();
    }//GEN-LAST:event_refreshLabelMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JTable customerTable;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel refreshLabel;
    private javax.swing.JButton registerCustomerButton;
    private javax.swing.JButton registerLuggageButton;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
}
