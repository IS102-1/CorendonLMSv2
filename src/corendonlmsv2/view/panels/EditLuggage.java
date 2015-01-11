package corendonlmsv2.view.panels;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.connectivity.LanguageController;
import corendonlmsv2.main.util.DateUtil;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.DatabaseTables;
import corendonlmsv2.model.Luggage;
import corendonlmsv2.model.Luggage.LuggageStatuses;
import corendonlmsv2.model.UserAccount;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;

/**
 * Simple dialog for adding new luggage or editing existing entries
 *
 * @author Emile Pels
 */
public class EditLuggage extends JFrame implements ActionListener
{

    private final String id;
    private final boolean isExistingEntry;

    private final String brandColumn = Luggage.TABLE.getColumnAt(
            Luggage.TableColumns.BRAND_NAME),
            colorColumn = Luggage.TABLE.getColumnAt(
                    Luggage.TableColumns.COLOR),
            customerIdColumn = Luggage.TABLE.getColumnAt(
                    Luggage.TableColumns.CUSTOMER_ID),
            detailsColumn = Luggage.TABLE.getColumnAt(
                    Luggage.TableColumns.DETAILS),
            locationColumn = Luggage.TABLE.getColumnAt(
                    Luggage.TableColumns.LOCATION),
            statusColumn = Luggage.TABLE.getColumnAt(
                    Luggage.TableColumns.STATUS),
            dateEnteredColumn = Luggage.TABLE.getColumnAt(
                    Luggage.TableColumns.DATE_ENTERED);
    
    private String brandInput, colorInput, 
            detailsInput, locationInput, dateEnteredInput;

    private LuggageStatuses initialStatus, newStatus;

    /**
     * Creates new form EditLuggaeg for registering new luggage. If this
     * constructor is called, the customer should be unknown
     */
    public EditLuggage()
    {
        this(null, false);
    }

    /**
     * Creates new form EditLuggage for registering new luggage or editing
     * existing entries
     *
     * @param id If isExistingEntry's value is true, this is assumed to be an
     * existing piece of luggage's ID and a dialog for editing said entry is
     * shown to the user. Else, this argument should contain a customerId to
     * register new luggage for
     * @param isExistingEntry Indicates whether the first argument to this
     * contructor is an existing piece of luggage's ID. This is the case when
     * true is passed. If false, the ID should contain a customerId to register
     * new luggage for
     */
    public EditLuggage(String id, boolean isExistingEntry)
    {
        this.id = id;
        this.isExistingEntry = isExistingEntry;

        initComponents();
        
        customerIdField.setEditable(false);
        
        loadExistingLuggage();
        registerListeners();
        
        setComponentProperties();
    }
    
    /**
     * Sets the components' properties
     */
    private void setComponentProperties()
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        
        jLabel1.setText(LanguageController.getString("customerId"));
        jLabel2.setText(LanguageController.getString("brandName"));
        jLabel3.setText(LanguageController.getString("color"));
        jLabel4.setText(LanguageController.getString("location"));
        jLabel5.setText(LanguageController.getString("status"));
        jLabel6.setText(LanguageController.getString("details"));
        cancelButton.setText(LanguageController.getString("cancel"));
        saveButton.setText(LanguageController.getString("save"));
    }

    private void registerListeners()
    {
        cancelButton.addActionListener(this);
        saveButton.addActionListener(this);
    }

    /**
     * Loads an existing luggage entry's details into the fields
     */
    private void loadExistingLuggage()
    {
        if (isExistingEntry && id != null)
        {
            DatabaseTables table = Luggage.TABLE;
            ResultSet results = DbManager.getResultSet(table, id,
                    table.getColumnAt(Luggage.TableColumns.LUGGAGE_ID), true);

            try
            {
                if (results.next())
                {
                    customerIdField.setText(results.getString(customerIdColumn));
                    brandField.setText(results.getString(brandColumn));
                    colorField.setText(results.getString(colorColumn));
                    locationField.setText(results.getString(locationColumn));
                    detailsField.setText(results.getString(detailsColumn));

                    initialStatus = LuggageStatuses.valueOf(
                            results.getString(statusColumn).toUpperCase());
                    statusComboBox.setSelectedItem(initialStatus);
                }
            } catch (SQLException ex)
            {
                System.err.println("SQL exception: " + ex.getMessage());
            }
        }
    }

    /**
     * Edits an existing luggage entry
     */
    private void editExisting()
    {
        String update = String.format("UPDATE %s SET %s='%s', %s='%s',"
                + " %s='%s', %s='%s', %s='%s' WHERE %s=%s",
                Luggage.TABLE.getDatabaseIdentifier(),
                brandColumn, brandInput,
                colorColumn, colorInput,
                detailsColumn, detailsInput,
                locationColumn, locationInput,
                statusColumn, newStatus.getDatabaseIdentifier(),
                Luggage.TABLE.getColumnAt(Luggage.TableColumns.LUGGAGE_ID), id
        );
        
        DbManager.executeUpdate(update);
        
        new ActionLog(UserAccount.getCurrent(), "Edited luggage " + id).insert();
        
        MiscUtil.showMessage(this, "Updated details for luggage " + id);
        
        //Hide frame
        setVisible(false);
    }

    private void register()
    {
        boolean success;
        
        try
        {
            Luggage luggage = new Luggage(brandInput, colorInput, id, 
                    detailsInput, locationInput, newStatus);
            
            luggage.setDateEntered(DateUtil.getDateString());
            
            success = luggage.insert();
            
        } catch (IllegalArgumentException ex)
        {
            MiscUtil.showMessage(this, ex.getMessage());
            return;
        }
        
        new ActionLog(UserAccount.getCurrent(), "Registered luggage for"
                + " customer " + id).insert();
        
        MiscUtil.showMessage(this, String.format("Registering the luggage was"
                + "%s succesful!", success ? "" : " not"));
        
        //Hide frame
        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object source = ae.getSource();

        if (source == cancelButton)
        {
            setVisible(false);
        } else if (source == saveButton)
        {
            newStatus = (LuggageStatuses) statusComboBox.getSelectedItem();
                        
            brandInput = brandField.getText();
            colorInput = colorField.getText();
            detailsInput = detailsField.getText();
            locationInput = locationField.getText();
            
            if (isExistingEntry)
            {
                editExisting();
            } else
            {
                //Register new luggage
                if (id == null && newStatus != LuggageStatuses.FOUND)
                {
                    //Luggage must have been found
                    MiscUtil.showMessage(this,
                            "No customer ID available to add luggage to!\nMake"
                                    + " sure you selected \"FOUND\""
                                    + " for the status.");
                    return;
                }

                register();
            }
        }
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

        jLabel1 = new javax.swing.JLabel();
        customerIdField = new javax.swing.JTextField();
        brandField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        colorField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        locationField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        statusComboBox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        detailsField = new javax.swing.JTextArea();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Customer ID");

        customerIdField.setEnabled(false);

        jLabel2.setText("Brand name");

        jLabel3.setText("Color");

        jLabel4.setText("Location");

        jLabel5.setText("Status");

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel(LuggageStatuses.values()));

        jLabel6.setText("Details");

        detailsField.setColumns(20);
        detailsField.setLineWrap(true);
        detailsField.setRows(5);
        detailsField.setWrapStyleWord(true);
        jScrollPane1.setViewportView(detailsField);

        cancelButton.setText("Cancel");

        saveButton.setText("Save");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerIdField)
                    .addComponent(brandField)
                    .addComponent(colorField)
                    .addComponent(locationField)
                    .addComponent(statusComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(brandField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(saveButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField brandField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField colorField;
    private javax.swing.JTextField customerIdField;
    private javax.swing.JTextArea detailsField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField locationField;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox statusComboBox;
    // End of variables declaration//GEN-END:variables
}
