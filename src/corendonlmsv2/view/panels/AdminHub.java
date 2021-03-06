package corendonlmsv2.view.panels;

import corendonlmsv2.connectivity.LanguageController;
import corendonlmsv2.main.CorendonLMSv2;
import corendonlmsv2.main.util.StringUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.UserAccount;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Brecht Dogge
 */
public class AdminHub extends JPanel implements ActionListener
{

    /**
     * Creates new form Hub
     */
    public AdminHub()
    {
        initComponents();

        setBackground(CorendonLMSv2.DEFAULT_BACKCOLOR);

        welcomeLabel.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        welcomeLabel.setText(String.format(LanguageController.getString(
                "welcomeBack"), UserAccount.getCurrent()));

        signOutButton.setText(LanguageController.getString("signOut"));
        logsButton.setText(LanguageController.getString("viewLogs"));
        usersButton.setText(LanguageController.getString("manageUsers"));
        
        registerListeners();
    }

    /**
     * Registers listeners for all appropriate components
     */
    private void registerListeners()
    {
        signOutButton.addActionListener(this);
        logsButton.addActionListener(this);
        usersButton.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object source = ae.getSource();
        JPanel panel;        
        
        if (source == signOutButton)
        {
            new ActionLog(UserAccount.getCurrent(), "Signed out").insert();
            
            UserAccount.setCurrent(null);
            panel = new Login();
        } else if (source == logsButton)
        {
            panel = new LogViewer(this);
        } else if (source == usersButton)
        {
            panel = new UserManager(this);
        } else
        {
            return;
        }
        
        CorendonLMSv2.MAIN_FRAME.displayPanel(panel);
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

        welcomeLabel = new javax.swing.JLabel();
        signOutButton = new javax.swing.JButton();
        logsButton = new javax.swing.JButton();
        usersButton = new javax.swing.JButton();

        welcomeLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        welcomeLabel.setText("welcomeLabel");

        signOutButton.setText("Sign out");

        logsButton.setBackground(new java.awt.Color(150, 40, 40));
        logsButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        logsButton.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        logsButton.setIcon(new ImageIcon("resources" + StringUtil.PATH_SEPERATOR + "Logs.png"));
        logsButton.setText("View logs");
        logsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        logsButton.setIconTextGap(10);
        logsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        usersButton.setBackground(new java.awt.Color(150, 40, 40));
        usersButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        usersButton.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        usersButton.setIcon(new ImageIcon("resources" + StringUtil.PATH_SEPERATOR + "User.png"));
        usersButton.setText("Manage users");
        usersButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        usersButton.setIconTextGap(10);
        usersButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(welcomeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(signOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(logsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                        .addGap(80, 80, 80)
                        .addComponent(usersButton, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(welcomeLabel)
                    .addComponent(signOutButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(logsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                    .addComponent(usersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(258, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton logsButton;
    private javax.swing.JButton signOutButton;
    private javax.swing.JButton usersButton;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
