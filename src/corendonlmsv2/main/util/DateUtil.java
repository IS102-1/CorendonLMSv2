package corendonlmsv2.main.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class with methods related to dates
 *
 * @author Emile Pels
 */
public class DateUtil
{
    /**
     * DateFormat to format dates as.
     * Example: "01-Jan-1970"
     */
    private static final DateFormat DATE_FORMAT = 
            DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
    
    //DateFormat to format date-and-times as.
    //Example: "01/01/1970 00:00:00"
    private static final DateFormat DATE_TIME_FORMAT
            = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    private DateUtil()
    {
    }
    
    /**
     * Gets the current date as a string
     * 
     * @return Current date as a string
     */
    public static String getDateString()
    {
        return new SimpleDateFormat("yyyy/MM/dd").format(now());
    }
    
    /**
     * Fetches a string containing the current date and time
     *
     * @return String containig the current date and time
     */
    public static String getDateTimeString()
    {
        return DATE_TIME_FORMAT.format(now());
    }
    
    /**
     * Checks whether a certain date is in a range of two others
     * 
     * @param start Starting date for the range
     * @param end Ending date for the range
     * @param value Value to check if it's inbetween start and end
     * @return Boolean indicating whether value is inbetween start and end
     */
    public static boolean isBetween(Date start, Date end, Date value)
    {
        if (start == null || end == null || value == null)
        {
            return false;
        }
        
        return start.before(value) && end.after(value);
    }
    
    /**
     * Checks whether a certain date is in a range of two others
     * 
     * @param start Starting date for the range
     * @param end Ending date for the range
     * @param value Value to check if it's inbetween start and end
     * @return Boolean indicating whether value is inbetween start and end
     */
    public static boolean isBetween(String start, String end, String value)
    {
        return isBetween(parseDate(start), parseDate(end), parseDate(value));
    }
    
    /**
     * Parses a date from a string
     * 
     * @param value String to parse
     * @return Date object if the string is formatted correctly. If not, null is
     * returned
     */
    private static Date parseDate(String value)
    {
        try
        {
            return DATE_FORMAT.parse(value);
        } catch (ParseException ex)
        {
            System.err.println("ParseException: " + ex.getMessage());
        }
        
        return null;
    }
    
    /**
     * Gets the current date
     * 
     * @return Current date
     */
    private static Date now()
    {
        return new Date();
    }
}