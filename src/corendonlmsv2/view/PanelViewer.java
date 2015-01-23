package corendonlmsv2.view;

import corendonlmsv2.main.CorendonLMSv2;
import corendonlmsv2.main.util.StringUtil;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Extends the JFrame for easy panel rotation
 *
 * @author Emile
 */
public class PanelViewer extends JFrame
{

    /**
     * File name for the logo to display on top of the frame
     */
    private static final String LOGO_FILENAME = String.format("resources%sCorendonLogo.png", StringUtil.PATH_SEPERATOR);
//"resources" 
            //+ StringUtil.PATH_SEPERATOR + "CorendonLogo.png";

    /**
     * ImageLabel to display on top of the frame
     */
    private static final ImageLabel LOGO = new ImageLabel(LOGO_FILENAME);

    /**
     * Initializes the frame and adds a window listener to dispose of the frame
     * when it is closed by the user
     */
    public PanelViewer()
    {
        //Set the frame's title and size
        super(CorendonLMSv2.APPLICATION_NAME);

        initFrame();
    }

    /**
     * Initializes the frame by setting its fields appropriatly, and registering
     * a windowclosing event listener which disposes of the instance when needed
     */
    private void initFrame()
    {
        Dimension frameSize = CorendonLMSv2.DEFAULT_SIZE;

        setBackground(CorendonLMSv2.DEFAULT_BACKCOLOR);
        setMinimumSize(frameSize);

        //Center frame on screen
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        //Listen for the frame's closing event, and
        //dispose of the frame when it is triggered
        addWindowListener(
                new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent event)
                    {
                        dispose();

                        System.exit(0);
                    }
                }
        );
    }

    /**
     * Displays a panel on the main frame
     *
     * @param panel Panel to display
     */
    public void displayPanel(JPanel panel)
    {
        Container pane = getContentPane();
        pane.removeAll();

        pane.add(LOGO, BorderLayout.NORTH);
        pane.add(panel, BorderLayout.CENTER);

        pane.revalidate();
        pane.repaint();

        if (!isVisible())
        {
            setVisible(true);
        }
    }
}
