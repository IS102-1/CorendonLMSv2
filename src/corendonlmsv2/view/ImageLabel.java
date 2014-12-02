package corendonlmsv2.view;

import corendonlmsv2.main.CorendonLMSv2;
import java.awt.Color;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Creates a simple picturebox
 *
 * TODO: Set background to corendon red
 *
 * @author Emile
 */
public class ImageLabel extends JPanel
{

    /**
     * Color for the image label's border
     */
    private static final Color BORDER_COLOR = CorendonLMSv2.DEFAULT_FORECOLOR;
    
    /**
     * Border for the image label
     */
    private static final Border BORDER = BorderFactory.createLineBorder(
            BORDER_COLOR, 5);

    /**
     * Initializes a new instance of ImageLabel
     *
     * @param fileName File path of the image to display
     */
    public ImageLabel(String fileName)
    {
        setBackground(CorendonLMSv2.DEFAULT_BACKCOLOR);
        setBorder(BORDER);
        setOpaque(true);

        if (new File(fileName).exists())
        {
            add(new JLabel(new ImageIcon(fileName)));
        }
    }
}
