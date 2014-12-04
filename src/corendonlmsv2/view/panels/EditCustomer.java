package corendonlmsv2.view.panels;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.main.util.StringUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.Customer;
import corendonlmsv2.model.DatabaseTables;
import corendonlmsv2.model.UserAccount;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Emile Pels
 */
public class EditCustomer
        extends JFrame implements ActionListener, DocumentListener
{

    private final String customerId;
    private String nameInput, addressInput,
            countryInput, emailInput, phoneInput;

    private final String nameColumn = Customer.TABLE.getColumnAt(
            Customer.TableColumns.NAME),
            addressColumn = Customer.TABLE.getColumnAt(
                    Customer.TableColumns.ADDRESS),
            countryColumn = Customer.TABLE.getColumnAt(
                    Customer.TableColumns.COUNTRY),
            emailColumn = Customer.TABLE.getColumnAt(
                    Customer.TableColumns.EMAIL_ADDRESS),
            phoneColumn = Customer.TABLE.getColumnAt(
                    Customer.TableColumns.PHONE_NUMBER);

    /**
     * Creates new form EditCustomer for creating a new customer
     */
    public EditCustomer()
    {
        //Show the frame, but don't set a customer ID as we're creating a new
        //entry
        this(null);
    }

    /**
     * Creates a new form EditCutomer for editing an existing customer
     *
     * @param customerId ID for the customer to edit
     */
    public EditCustomer(String customerId)
    {
        this.customerId = customerId;

        initComponents();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        loadExistingCustomer();
        registerListeners();
    }

    /**
     * Registers event listeners
     */
    private void registerListeners()
    {
        emailField.getDocument().addDocumentListener(this);
        cancelButton.addActionListener(this);
        saveButton.addActionListener(this);
    }

    /**
     * Checks if the e-mail address is either empty or valid, and sets various
     * controls' properties appropriatly
     */
    private void validateEmail()
    {
        String email = emailField.getText();

        boolean isValid = email.isEmpty()
                || StringUtil.isValidEmailAddress(email);

        emailInvalidLabel.setVisible(!isValid);
        saveButton.setEnabled(isValid);
    }

    /**
     * Loads an existing customer's details into the text fields for editing
     */
    private void loadExistingCustomer()
    {
        if (customerId != null)
        {
            DatabaseTables table = Customer.TABLE;
            ResultSet results = DbManager.getResultSet(table, customerId,
                    table.getColumnAt(Customer.TableColumns.CUSTOMER_ID), true);

            try
            {
                if (results.next())
                {
                    nameField.setText(results.getString(nameColumn));
                    addressField.setText(results.getString(addressColumn));
                    countryField.setText(results.getString(countryColumn));
                    emailField.setText(results.getString(emailColumn));
                    phoneField.setText(results.getString(phoneColumn));
                }
            } catch (SQLException ex)
            {
                System.err.println("SQL exception: " + ex.getMessage());
            }
        }
    }

    private void registerCustomer()
    {
        boolean success;

        try
        {
            success = new Customer(addressInput, countryInput,
                    emailInput, nameInput, phoneInput).insert();
        } catch (IllegalArgumentException ex)
        {
            MiscUtil.showMessage(this, ex.getMessage());
            return;
        }

        new ActionLog(UserAccount.getCurrent(), 
                "Registered customer " + nameInput).insert();
        
        MiscUtil.showMessage(this, String.format("Registering the customer was"
                + "%s succesful!", success ? "" : " not"));

        //Hide frame
        setVisible(false);
    }

    private void editCustomer()
    {
        //Double check, just in case
        if (customerId != null && !nameInput.isEmpty()
                && !addressInput.isEmpty() && !phoneInput.isEmpty())
        {
            String update = String.format("UPDATE %s SET %s='%s', %s='%s', "
                    + "%s='%s', %s='%s', %s='%s' WHERE %s=%s",
                    Customer.TABLE.getDatabaseIdentifier(),
                    nameColumn, nameInput,
                    addressColumn, addressInput,
                    countryColumn, countryInput,
                    emailColumn, emailInput,
                    phoneColumn, phoneInput,
                    Customer.TABLE.getColumnAt(
                            Customer.TableColumns.CUSTOMER_ID), customerId
            );
            
            DbManager.executeUpdate(update);
            
            new ActionLog(UserAccount.getCurrent(), 
                "Edited customer " + customerId).insert();
            
            MiscUtil.showMessage(this, 
                    "Updated details for customer " + customerId);
            
            //Hide frame
            setVisible(false);
        }
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
            nameInput = nameField.getText();
            addressInput = addressField.getText();
            countryInput = countryField.getText();
            emailInput = emailField.getText();
            phoneInput = phoneField.getText();

            if (customerId == null)
            {
                registerCustomer();
            } else
            {
                editCustomer();
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent de)
    {
        validateEmail();
    }

    @Override
    public void removeUpdate(DocumentEvent de)
    {
        validateEmail();
    }

    @Override
    public void changedUpdate(DocumentEvent de)
    {
        validateEmail();
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

        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        phoneField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        emailInvalidLabel = new javax.swing.JLabel();
        addressField = new javax.swing.JTextField();
        countryField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        cancelButton.setText("Cancel");

        jLabel1.setText("Name");

        jLabel2.setText("Address");

        jLabel5.setText("Phone number");

        saveButton.setText("Save");

        emailInvalidLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        emailInvalidLabel.setText("INVALID");
        emailInvalidLabel.setVisible(false);

        jLabel3.setText("Country");

        jLabel4.setText("E-mail address");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameField)
                    .addComponent(addressField, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(countryField, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(emailField, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(phoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(emailInvalidLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(countryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(emailInvalidLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField countryField;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailInvalidLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField phoneField;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
