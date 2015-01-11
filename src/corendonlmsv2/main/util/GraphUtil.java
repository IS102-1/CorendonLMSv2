package corendonlmsv2.main.util;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.model.Luggage.LuggageStatuses;
import corendonlmsv2.view.panels.DateRangeInput.Months;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Emile Pels
 */
public class GraphUtil
{
    
    /**
     * Gets the amount of luggage for a specified month in a year matching
     * the specified status
     * 
     * @param status The status for the luggage to count
     * @param month The month the luggage should be registered in
     * @param year The year the luggage should be registered in
     * @return The number of pieces of luggage registered in that month
     * with the specified status
     */
    private static int getLuggageForMonth(
            LuggageStatuses status, int month, int year)
    {
        final String query = String.format("SELECT COUNT(*) AS count "
                + "FROM luggage "
                + "WHERE status = '%s' "
                + "AND MONTH(date_entered) = %d;", 
                status.getDatabaseIdentifier(), month);
        
        final ResultSet results = DbManager.executeQuery(query);
        
        try
        {
            if (results.next())
            {
                return results.getInt("count");
            }
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
        }
        
        return 0;
    }
    
    
    /**
     * Gets a data set containing the luggage ordered by month, year and status
     * 
     * @param fromMonth Start month for the range to count luggage in
     * @param fromYear Start year for the range to count luggage in
     * @param toMonth End month for the range to count luggage in
     * @param toYear End year for the range to count luggage in
     * @return a data set containing the luggage ordered by month, year and 
     * status
     */
    public static DefaultCategoryDataset getLuggageDataSet(Months fromMonth, 
            int fromYear, Months toMonth, int toYear)
    {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        
        for (int year = fromYear; year <= toYear; ++year)
        {
            for (int month = fromMonth.getMonthIndex(); 
                    month <= toMonth.getMonthIndex(); 
                    ++month)
            {
                for (LuggageStatuses status : LuggageStatuses.values())
                {
                    int nLuggage = getLuggageForMonth(status, month, year);
                    
                    dataSet.addValue(nLuggage, status, 
                            Months.parse(month) + " " + year);
                }
            }
        }
        
        //JFreeChart chart = ChartFactory.createLineChart("Luggage statuses", 
        //        "Month", "Amount", dataSet);
        
        return dataSet;
    }
    
    /**
     * Saves a chart to a new file as an image
     * 
     * @param chart
     * @return 
     */
    public static String saveChartToFile(JFreeChart chart)
    {
        return "";
    }
}
