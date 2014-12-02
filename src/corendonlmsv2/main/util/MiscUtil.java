package corendonlmsv2.main.util;

import corendonlmsv2.main.CorendonLMSv2;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Utility class for miscellaneous purproses
 *
 * @author Emile Pels
 */
public class MiscUtil
{

    private MiscUtil()
    {
    }

    /**
     * Determines whether a result set contains no values
     *
     * @param results ResultSet object to check
     * @return Boolean indicating whether the result set contains no values
     */
    public static boolean isResultSetEmpty(ResultSet results)
    {
        boolean isEmpty = true;

        try
        {
            isEmpty = !results.next();
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
        }

        return isEmpty;
    }

    /**
     * Attempts setting the LookAndFeel. Uses the system's default if the
     * selected L&F is not installed
     *
     * @param lookAndFeel The string representative of the L&F to set
     */
    public static void setLookAndFeel(String lookAndFeel)
    {
        try
        {
            for (UIManager.LookAndFeelInfo info
                    : UIManager.getInstalledLookAndFeels())
            {
                if (lookAndFeel.equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException e)
        {
            System.err.println("Could net set LookAndFeel: " + e.getMessage());
        }
    }

    /**
     * Shows a message dialog to the user
     *
     * @param message The string to display
     */
    public static void showMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message,
                CorendonLMSv2.APPLICATION_NAME,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a message dialog to the user
     *
     * @param message The object for which the string representation is
     * displayed, as specified in its toString implementation
     */
    public static void showMessage(Object message)
    {
        showMessage(message.toString());
    }
}
