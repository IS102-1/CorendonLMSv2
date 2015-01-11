package corendonlmsv2.view.panels;

import corendonlmsv2.connectivity.LanguageController;
import corendonlmsv2.main.CorendonLMSv2;
import corendonlmsv2.main.util.GraphUtil;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.UserAccount;
import corendonlmsv2.view.panels.DateRangeInput.Months;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Asks the user for a time range to display a graph for
 *
 * @author Emile Pels
 */
public class DateRangeInput extends JPanel
{
    
    public enum Months
    {      
        JANUARY(1), FEBRUARY(2), MARCH(3),
        APRIL(4), MAY(5), JUNE(6),
        JULY(7), AUGUST(8), SEPTEMBER(9),
        OCTOBER(10), NOVEMBER(11), DECEMBER(12);
        
        private final int monthIndex;
        
        private Months(int monthIndex)
        {
            this.monthIndex = monthIndex;
        }
        
        public int getMonthIndex()
        {
            return monthIndex;
        }
        
        public static String parse(int month)
        {
            String result;
            
            switch (month)
            {
                case 1: result = "Jan"; break;
                case 2: result = "Feb"; break;
                case 3: result = "Mar"; break;
                case 4: result = "Apr"; break;
                case 5: result = "May"; break;
                case 6: result = "Jun"; break;
                case 7: result = "Jul"; break;
                case 8: result = "Aug"; break;
                case 9: result = "Sep"; break;
                case 10: result = "Oct"; break;
                case 11: result = "Nov"; break;
                case 12: result = "Dec"; break;
                default: result = ""; break;
            }
            
            return result;
        }
    };

    private final Component parent;
    
    /**
     * Creates new form DateRangeInput
     * 
     * @param parent The parent component, to center the chart to
     */
    public DateRangeInput(Component parent)
    {
        initComponents();
        this.parent = parent;
        
        setComponentProperties();
    }
    
    private void setComponentProperties()
    {
        jLabel1.setText(LanguageController.getString("fromYear"));
        jLabel2.setText(LanguageController.getString("fromMonth"));
        jLabel3.setText(LanguageController.getString("toMonth"));
        jLabel4.setText(LanguageController.getString("toYear"));
        saveCheckBox.setText(LanguageController.getString("saveToFile"));
        graphButton.setText(LanguageController.getString("showGraph"));
    }
    
    /**
     * Checks whether a certain month and year are later than another
     * 
     * @param fromMonth Start month for the range to count luggage in
     * @param fromYear Start year for the range to count luggage in
     * @param toMonth End month for the range to count luggage in
     * @param toYear End year for the range to count luggage in
     * @return 
     */
    private static boolean isAfter(int fromMonth, 
            int fromYear, int toMonth, int toYear)
    {
        Calendar fromCal = Calendar.getInstance();
        fromCal.set(Calendar.MONTH, fromMonth - 1);
        fromCal.set(Calendar.YEAR, fromYear);
        
        Calendar toCal = Calendar.getInstance();
        toCal.set(Calendar.MONTH, toMonth - 1);
        toCal.set(Calendar.YEAR, toYear);
        
        return fromCal.before(toCal);
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

        fromSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        fromComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        toComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        toSpinner = new javax.swing.JSpinner();
        graphButton = new javax.swing.JButton();
        saveCheckBox = new javax.swing.JCheckBox();

        fromSpinner.setModel(new javax.swing.SpinnerNumberModel(2015, 2014, 2114, 1));

        jLabel1.setText("From (year):");

        fromComboBox.setModel(new DefaultComboBoxModel(Months.values()));

        jLabel2.setText("From (month):");

        jLabel3.setText("To (month):");

        toComboBox.setModel(new DefaultComboBoxModel(Months.values()));

        jLabel4.setText("To (year):");

        toSpinner.setModel(new javax.swing.SpinnerNumberModel(2015, 2014, 2114, 1));

        graphButton.setText("Show graph");
        graphButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                graphButtonActionPerformed(evt);
            }
        });

        saveCheckBox.setText("Save to file");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fromComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fromSpinner)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(saveCheckBox)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(graphButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(toSpinner)
                            .addComponent(toComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(saveCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(graphButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void graphButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_graphButtonActionPerformed
    {//GEN-HEADEREND:event_graphButtonActionPerformed
        final Months fromMonth = (Months) fromComboBox.getSelectedItem();
        final Months toMonth = (Months) toComboBox.getSelectedItem();
        
        final int fromYear = (int) fromSpinner.getValue();
        final int toYear = (int) toSpinner.getValue();
        
        if (!isAfter(fromMonth.getMonthIndex(), 
                fromYear, toMonth.getMonthIndex(), toYear))
        {
            MiscUtil.showMessage(this, 
                    LanguageController.getString("rangeInvalid"));
            return;
        }
        
        DefaultCategoryDataset dataSet = GraphUtil.getLuggageDataSet(
                fromMonth, fromYear, toMonth, toYear);
        
        JFreeChart chart = ChartFactory.createLineChart(
                LanguageController.getString("luggageStatus"), 
                LanguageController.getString("month"), 
                LanguageController.getString("amount"), 
                dataSet);
        
        if (saveCheckBox.isSelected())
        {
            try
            {
                final int chartWidth = 800, chartHeight = 600;
                ChartUtilities.saveChartAsPNG(new File("chart.png"), 
                        chart, chartWidth, chartHeight);
                
                MiscUtil.showMessage(this, String.format(
                        LanguageController.getString("graphSaved"),
                        "chart.png"));
            } catch (IOException ex)
            {
                System.err.println("IO Exception: " + ex.getMessage());
            }
        }
        
        ChartFrame frame = new ChartFrame("Chart", chart);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(CorendonLMSv2.DEFAULT_SIZE);
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
        
        new ActionLog(UserAccount.getCurrent(), "Created graph").insert();
    }//GEN-LAST:event_graphButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox fromComboBox;
    private javax.swing.JSpinner fromSpinner;
    private javax.swing.JButton graphButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JCheckBox saveCheckBox;
    private javax.swing.JComboBox toComboBox;
    private javax.swing.JSpinner toSpinner;
    // End of variables declaration//GEN-END:variables
}