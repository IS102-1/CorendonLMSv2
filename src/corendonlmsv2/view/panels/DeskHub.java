package corendonlmsv2.view.panels;

import corendonlmsv2.main.CorendonLMSv2;
import corendonlmsv2.main.util.StringUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.UserAccount;
import corendonlmsv2.view.PanelViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Emile
 */
public class DeskHub extends JPanel implements ActionListener
{

    /**
     * Creates new form Hub
     */
    public DeskHub()
    {
        initComponents();

        setBackground(CorendonLMSv2.DEFAULT_BACKCOLOR);

        welcomeLabel.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        welcomeLabel.setText(String.format("Welcome back, %s! What would you"
                + " like to do?", UserAccount.getCurrent()));

        registerListeners();
    }
    
    /**
     * Registers listeners for all appropriate components
     */
    private void registerListeners()
    {
        signOutButton.addActionListener(this);
        customersButton.addActionListener(this);
        luggageButton.addActionListener(this);
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
        } else if (source == customersButton)
        {
            panel = new CustomerManager(this);
        } else if (source == luggageButton)
        {
            panel = new LuggageManager(this);
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
        customersButton = new javax.swing.JButton();
        luggageButton = new javax.swing.JButton();

        welcomeLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        welcomeLabel.setText("welcomeLabel");

        signOutButton.setText("Sign out");

        customersButton.setBackground(new java.awt.Color(150, 40, 40));
        customersButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        customersButton.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        customersButton.setIcon(new ImageIcon("resources" + StringUtil.PATH_SEPERATOR + "Customer.png"));
        customersButton.setText("Manage customers");
        customersButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        customersButton.setIconTextGap(10);
        customersButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        luggageButton.setBackground(new java.awt.Color(150, 40, 40));
        luggageButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        luggageButton.setForeground(CorendonLMSv2.DEFAULT_FORECOLOR);
        luggageButton.setIcon(new ImageIcon("resources" + StringUtil.PATH_SEPERATOR + "Luggage.png"));
        luggageButton.setText("Manage luggage");
        luggageButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        luggageButton.setIconTextGap(10);
        luggageButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(welcomeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 484, Short.MAX_VALUE)
                        .addComponent(signOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(customersButton, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                        .addGap(80, 80, 80)
                        .addComponent(luggageButton, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)))
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
                    .addComponent(luggageButton, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                    .addComponent(customersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(243, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton customersButton;
    private javax.swing.JButton luggageButton;
    private javax.swing.JButton signOutButton;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
