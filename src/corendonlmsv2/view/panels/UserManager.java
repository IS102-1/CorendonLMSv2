package corendonlmsv2.view.panels;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.main.CorendonLMSv2;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.main.util.StringUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.UserAccount;
import corendonlmsv2.model.UserAccount.UserRoles;
import corendonlmsv2.view.NonEditableTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Emile Pels
 */
public class UserManager extends JPanel implements ActionListener
{

    /**
     * Provides column names for this table, so the password is not shown
     */
    private static final String[] COLUMN_NAMES = new String[]
    {
        "User ID", "Username", "User role"
    };

    private static final String[] USER_ROLES = new String[]
    {
        "DESK_EMPLOYEE",
        "MANAGER",
        "ADMIN"
    };

    private final JPanel parent;
    private final NonEditableTableModel model;

    /**
     * Creates new form LogViewer
     *
     * @param parent Parent panel to display when user manager is closed
     */
    public UserManager(JPanel parent)
    {
        new ActionLog(UserAccount.getCurrent(),
                "Accessed user manager").insert();

        this.parent = parent;
        initComponents();

        jLabel1.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        jLabel2.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        jLabel3.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        jLabel4.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        jLabel5.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        jLabel6.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        jLabel8.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        setBackground(CorendonLMSv2.DEFAULT_BACKCOLOR);

        model = (NonEditableTableModel) userTable.getModel();

        //Remove UserRoles.UNAUTHORIZED from combobox
        if (roleComboBox.getComponentCount() > 0)
        {
            roleComboBox.removeItemAt(0);
        }
        
        loadUsers();

        registerListeners();
    }

    /**
     * Registers listeners for all appropriate components
     */
    private void registerListeners()
    {
        backButton.addActionListener(this);
        deleteButton.addActionListener(this);
        editPasswordButton.addActionListener(this);
        editRoleButton.addActionListener(this);
        registerButton.addActionListener(this);
    }

    /**
     * Removes any entries from the table and reloads it with all current users
     */
    private void loadUsers()
    {
        List<UserAccount> users = UserAccount.getAllUsers();
        resetUsers();

        for (UserAccount user : users)
        {
            model.addRow(new String[]
            {
                user.getUserId(),
                user.getUsername(),
                user.getUserRole().toString()
            });
        }
    }

    /**
     * Removes all rows from the table
     */
    private void resetUsers()
    {
        model.setRowCount(0);
    }

    /**
     * Gets the selected account's user ID. Returns null and shows warning
     * message if no user account is selected
     *
     * @return Value of the selected user ID. Null if not applicable
     */
    private String getSelectedUserId()
    {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1)
        {
            MiscUtil.showMessage("Please select a valid user account before "
                    + "proceeding.");
            return null;
        }

        return (String) userTable.getValueAt(selectedRow, 0);
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
        } else if (source == editPasswordButton)
        {
            editPassword();
        } else if (source == editRoleButton)
        {
            editRole();
        } else if (source == registerButton)
        {
            registerUser();
        }
    }

    /**
     * Deletes the selected user from the database
     */
    private void deleteSelected()
    {
        String selectedId = getSelectedUserId();

        //Get the name for the user ID column
        String userIdColumn = UserAccount.TABLE.getColumnAt(
                UserAccount.TableColumns.USER_ID);

        if (selectedId != null)
        {
            String update = String.format("DELETE FROM %s WHERE %s=%s",
                    UserAccount.TABLE.getDatabaseIdentifier(), userIdColumn,
                    selectedId);

            DbManager.executeUpdate(update);

            new ActionLog(UserAccount.getCurrent(), 
                    "Deleted user no. " + selectedId).insert();
            
            loadUsers();
            MiscUtil.showMessage("The user account has been deleted.");
        }
    }

    /**
     * Edits the password for the selected user
     */
    private void editPassword()
    {
        String selectedId = getSelectedUserId();

        //Get the name for the user ID column
        String userIdColumn = UserAccount.TABLE.getColumnAt(
                UserAccount.TableColumns.USER_ID);

        //Get the name for the password column
        String passwordColumn = UserAccount.TABLE.getColumnAt(
                UserAccount.TableColumns.PASSWORD);

        if (selectedId != null)
        {
            String firstEntry = JOptionPane.showInputDialog("What would you "
                    + "like to change your password to?");

            if (!StringUtil.isStringNullOrWhiteSpace(firstEntry))
            {
                String secondEntry = JOptionPane.showInputDialog("Please "
                        + "confirm your password by retyping it.");

                if (firstEntry.equals(secondEntry))
                {
                    String update = String.format("UPDATE %s SET %s='%s' "
                            + "WHERE %s=%s",
                            UserAccount.TABLE.getDatabaseIdentifier(),
                            passwordColumn,
                            StringUtil.hashString(firstEntry, true),
                            userIdColumn,
                            selectedId
                    );

                    DbManager.executeUpdate(update);
                    
                    new ActionLog(UserAccount.getCurrent(), "Edited password "
                            + "for user no. " + selectedId).insert();

                    MiscUtil.showMessage("The password has succesfully been"
                            + " changed!");
                } else
                {
                    MiscUtil.showMessage("The entered passwords do not match.");
                }
            } else
            {
                MiscUtil.showMessage("Password can not be empty.");
            }
        }
    }

    /**
     * Edits the role for the selected user
     */
    private void editRole()
    {
        String selectedId = getSelectedUserId();

        //Get the name for the user ID column
        String userIdColumn = UserAccount.TABLE.getColumnAt(
                UserAccount.TableColumns.USER_ID);

        //Get the name for the password column
        String userRoleColumn = UserAccount.TABLE.getColumnAt(
                UserAccount.TableColumns.USER_ROLE);

        String input = (String) JOptionPane.showInputDialog(
                null,
                "What is this account's new user role?",
                "New user role",
                JOptionPane.QUESTION_MESSAGE,
                null,
                USER_ROLES,
                USER_ROLES[0]
        );

        if (input != null)
        {
            UserRoles result = UserRoles.valueOf(input);

            String update = String.format("UPDATE %s SET %s='%s' WHERE %s=%s",
                    UserAccount.TABLE.getDatabaseIdentifier(),
                    userRoleColumn,
                    result.getDatabaseIdentifier(),
                    userIdColumn,
                    selectedId
            );
            
            DbManager.executeUpdate(update);
            
            new ActionLog(UserAccount.getCurrent(), String.format("Changed role"
                    + " for user no. %s to %s", selectedId, result)).insert();
            
            loadUsers();
            MiscUtil.showMessage("The user role has succesfully been changed!");
        } else
        {
            MiscUtil.showMessage("Input is not valid, try again.");
        }
    }
    
    /**
     * Registers a new user account
     */
    private void registerUser()
    {
        //Get the values from the panel
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        UserRoles userRole = (UserRoles) roleComboBox.getSelectedItem();
        
        usernameField.setText("");
        passwordField.setText("");
        passwordRepeatField.setText("");
        
        if (StringUtil.isStringNullOrWhiteSpace(username)
                || StringUtil.isStringNullOrWhiteSpace(password))
        {
            MiscUtil.showMessage("The username and password can not be empty.");
            return;
        }
        
        boolean result;
        
        try
        {
            result = new UserAccount(username, password, userRole).insert();
        } catch (IllegalArgumentException ex)
        {
            MiscUtil.showMessage(ex.getMessage());
            return;
        }
        
        new ActionLog(UserAccount.getCurrent(), "Registered account with"
                + " username " + username).insert();
        
        loadUsers();
        MiscUtil.showMessage(String.format("Registering the account "
                + "was%s succesful!", (result ? "" : " not")));
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
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        passwordRepeatField = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        roleComboBox = new javax.swing.JComboBox();
        registerButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        editRoleButton = new javax.swing.JButton();
        editPasswordButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Existing users:");

        userTable.setModel(new NonEditableTableModel(COLUMN_NAMES, 0));
        jScrollPane1.setViewportView(userTable);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Register new user:");

        jLabel3.setText("Username:");

        jLabel4.setText("Password:");

        jLabel5.setText("Repeat password:");

        jLabel6.setText("User role:");

        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel(UserRoles.values()));

        registerButton.setText("Register");

        deleteButton.setText("Delete");

        editRoleButton.setText("Edit role");

        editPasswordButton.setText("Edit password");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Modify selected:");

        backButton.setText("Back");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(editRoleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(editPasswordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordRepeatField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(backButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordRepeatField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(registerButton))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editRoleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editPasswordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editPasswordButton;
    private javax.swing.JButton editRoleButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JPasswordField passwordRepeatField;
    private javax.swing.JButton registerButton;
    private javax.swing.JComboBox roleComboBox;
    private javax.swing.JTable userTable;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables
}
