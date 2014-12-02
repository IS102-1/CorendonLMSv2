package corendonlmsv2.main;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.Customer;
import corendonlmsv2.model.Luggage;
import corendonlmsv2.model.Luggage.LuggageStatuses;
import corendonlmsv2.model.UserAccount;
import corendonlmsv2.model.UserAccount.UserRoles;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Defines the application's main entry point as well as constants used
 * throughout the program
 *
 * @author Emile Pels
 */
public class CorendonLMSv2
{

    /**
     * Application's name
     */
    public static final String APPLICATION_NAME = "Corendon LMS";

    /**
     * Application's default background color
     */
    public static final Color DEFAULT_BACKCOLOR = new Color(156, 10, 13);

    /**
     * Application's default foreground color
     */
    public static final Color DEFAULT_FORECOLOR = Color.WHITE;

    /**
     * Application's main frame default size
     */
    public static final Dimension DEFAULT_SIZE = new Dimension(750, 600);

    /**
     * Filename for the error log
     */
    public static final String ERROR_LOG_FILENAME = "errors.log";

    /**
     * Indicates whether errors should be written to a file rather than the
     * standard error stream
     */
    public static final boolean ERRORS_TO_FILE = false;

    /**
     * Application's look and feel
     */
    public static final String LOOK_AND_FEEL = "Nimbus";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        if (ERRORS_TO_FILE)
        {
            setErrorStream(ERROR_LOG_FILENAME);
        }

        DbManager.connect();
    }

    /**
     * Changes the error output stream to a certain FileOutputStream opened to
     * the specified file name and registers a hook listening for shutdown. Upon
     * the application's termination, the streams and the connection to the
     * database are closed
     *
     * @param fileName File name to write all application logs to
     */
    private static void setErrorStream(String fileName)
    {
        try
        {
            final FileOutputStream fileStream = new FileOutputStream(fileName,
                    true); //Append to file rather than overwriting
            final PrintStream errStream = new PrintStream(fileStream);

            //Set the error stream to the target file
            System.setErr(errStream);

            //Registers a hook listening for shutdown. Upon the application's
            //termination, the streams and the connection to the database
            //are closed
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        fileStream.flush();
                        errStream.flush();

                        fileStream.close();
                        errStream.close();

                        DbManager.close();
                    } catch (IOException ex)
                    {
                    }

                    System.out.printf("%s terminated\n", APPLICATION_NAME);
                }
            }));
        } catch (FileNotFoundException ex)
        {
            //Error occured while setting up the error logging...
            //... the irony :[
        }
    }
}
