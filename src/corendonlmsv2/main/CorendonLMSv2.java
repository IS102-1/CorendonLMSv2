package corendonlmsv2.main;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.model.ActionLog;
import corendonlmsv2.model.UserAccount;
import corendonlmsv2.model.UserAccount.UserRoles;
import corendonlmsv2.view.PanelViewer;
import corendonlmsv2.view.panels.Login;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.SwingUtilities;

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
    public static final Dimension DEFAULT_SIZE = new Dimension(750, 650);

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
     * Application's main frame
     */
    public static final PanelViewer MAIN_FRAME = new PanelViewer();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        if (ERRORS_TO_FILE)
        {
            setErrorStream(ERROR_LOG_FILENAME);
        }
        
        System.out.println("Starting " + APPLICATION_NAME);

        MiscUtil.setLookAndFeel(LOOK_AND_FEEL);

        DbManager.connect();

        insertAdminAccount();

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                MAIN_FRAME.displayPanel(new Login());
            }
        });
    }

    /**
     * Inserts an administrator account into the database. For debugging
     * purposes during development only
     */
    private static void insertAdminAccount()
    {
        final String username = "admin", password = "admin";
        final UserRoles role = UserRoles.ADMIN;

        if (!UserAccount.isUsernameRegistered(username))
        {
            new UserAccount(username, password, role).insert();
        }
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
                    UserAccount currentUser = UserAccount.getCurrent();
                    
                    if (currentUser != null)
                    {
                        //Someone was logged in and closed the application:
                        //write a log to the database
                        new ActionLog(currentUser, "Signed out").insert();
                    }
                    
                    try
                    {
                        //Flush and close all streams
                        fileStream.flush();
                        errStream.flush();

                        fileStream.close();
                        errStream.close();
                        
                        //Close the connection to the database
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
