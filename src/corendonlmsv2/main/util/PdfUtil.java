package corendonlmsv2.main.util;

import com.lowagie.text.DocumentException;
import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.model.Customer;
import corendonlmsv2.model.Luggage;
import corendonlmsv2.model.UserAccount;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Aids in writing PDF files to the disk. Used for generating reports
 *
 * @author Emile
 */
public class PdfUtil
{

    //HTML file containing the base text for the report
    private static final String REPORT_BASE = String.format(
            "resources%sLuggageReport.html", StringUtil.PATH_SEPERATOR);

    //Subdirectory to write reports to
    private static final String SUBDIRECTORY = "Reports";

    private PdfUtil()
    {
    }

    /**
     * Generates a PDF from an existing luggage ID
     *
     * @param luggageId ID for the luggage to write to PDF
     * @return Filename for the generated PDF
     * @throws IOException Exception thrown if the input file with the report's
     * base text can not be found
     */
    public static String generatePdf(String luggageId) throws IOException
    {
        createOutputDir();

        //Write PDF to /Reports directory
        String fileName = String.format("%s%sReport-%d.pdf", SUBDIRECTORY,
                StringUtil.PATH_SEPERATOR, System.currentTimeMillis());

        String report = getReportString(luggageId);

        if (report == null)
        {
            throw new IllegalArgumentException("Luggage ID was not found!");
        }

        DocumentBuilder docBuilder;
        try
        {
            docBuilder
                    = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex)
        {
            System.err.println("ParserConfigurationException: "
                    + ex.getMessage());

            return null;
        }

        OutputStream outFile = null;
        ITextRenderer renderer = null;

        try
        {
            Document doc = docBuilder.parse(
                    new InputSource(new StringReader(report)));

            renderer = new ITextRenderer();
            renderer.setDocument(doc, null);

            outFile = new FileOutputStream(fileName);
            renderer.layout();
            renderer.createPDF(outFile);
        } catch (DocumentException | IOException | SAXException ex)
        {
            System.err.println("Exception: " + ex.getMessage());

            return null;
        } finally
        {
            if (outFile != null)
            {
                outFile.close();
            }

            if (renderer != null)
            {
                renderer.finishPDF();
            }
        }

        return fileName;
    }

    /**
     * Builds a string containing an HTML file for reporting luggage
     *
     * @param luggageId ID for the luggage to generate HTML source for
     * @return HTML source reporting luggage
     * @throws IOException Exception thrown if the input file with the report's
     * base textcan not be found
     */
    private static String getReportString(String luggageId)
            throws IOException
    {
        String result = null;
        File inFile = new File(REPORT_BASE);

        if (StringUtil.isStringNullOrWhiteSpace(luggageId))
        {
            throw new IllegalArgumentException("Luggage ID can not be empty.");
        }

        if (!inFile.exists())
        {
            throw new FileNotFoundException(String.format(""));
        }

        StringBuilder builder = new StringBuilder();

        for (String line : Files.readAllLines(inFile.toPath(),
                Charset.defaultCharset()))
        {
            builder.append(line).append('\n');
        }

        ResultSet luggageResults = DbManager.getResultSet(Luggage.TABLE,
                luggageId,
                Luggage.TABLE.getColumnAt(Luggage.TableColumns.LUGGAGE_ID),
                true);

        try
        {
            if (luggageResults.next())
            {
                result = builder.toString().replace("-luggage-", luggageId);

                result = result.replace("-brand-", luggageResults.getString(
                        Luggage.TABLE.getColumnAt(
                                Luggage.TableColumns.BRAND_NAME)));
                result = result.replace("-color-", luggageResults.getString(
                        Luggage.TABLE.getColumnAt(
                                Luggage.TableColumns.COLOR)));
                result = result.replace("-status-", luggageResults.getString(
                        Luggage.TABLE.getColumnAt(
                                Luggage.TableColumns.STATUS)));
                result = result.replace("-details-", luggageResults.getString(
                        Luggage.TABLE.getColumnAt(
                                Luggage.TableColumns.DETAILS)));

                String customerId = luggageResults.getString(
                        Luggage.TABLE.getColumnAt(
                                Luggage.TableColumns.CUSTOMER_ID));

                if (customerId != null)
                {
                    ResultSet cusResults = DbManager.getResultSet(
                            Customer.TABLE, customerId,
                            Customer.TABLE.getColumnAt(
                                    Customer.TableColumns.CUSTOMER_ID), true);

                    if (cusResults.next())
                    {
                        result = result.replace("-name-", cusResults.getString(
                                Customer.TABLE.getColumnAt(
                                        Customer.TableColumns.NAME)));
                        result = result.replace("-address-", cusResults.getString(
                                Customer.TABLE.getColumnAt(
                                        Customer.TableColumns.ADDRESS)));
                        result = result.replace("-country-", cusResults.getString(
                                Customer.TABLE.getColumnAt(
                                        Customer.TableColumns.COUNTRY)));
                        result = result.replace("-email-", cusResults.getString(
                                Customer.TABLE.getColumnAt(
                                        Customer.TableColumns.EMAIL_ADDRESS)));
                        result = result.replace("-phone-", cusResults.getString(
                                Customer.TABLE.getColumnAt(
                                        Customer.TableColumns.PHONE_NUMBER)));
                    }
                }

                UserAccount currentUser = UserAccount.getCurrent();
                if (currentUser != null)
                {
                    result = result.replace("-employee-",
                            currentUser.getUsername());
                }
            }
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Checks if the subdirectory for report writing exists. If not, creation is
     * attempted
     *
     * @return Returns false only if the directory did not exist and creation
     * failed
     */
    private static boolean createOutputDir()
    {
        File dir = new File(SUBDIRECTORY);

        if (!(dir.exists() && dir.isDirectory()))
        {
            return dir.mkdir();
        }

        return true;
    }

    /**
     * Launches a PDF and shows it to the user
     *
     * @param filename PDF's path
     */
    public static void launchPdf(String filename)
    {
        File file = new File(filename);

        if (file.exists())
        {
            String message = String.format("PDF has been written to %s. Would "
                    + "you like to open it now?", filename);

            if (JOptionPane.showConfirmDialog(null, message, "Success",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                try
                {
                    Desktop.getDesktop().open(file);
                } catch (IOException ex)
                {
                }
            }
        }
    }
}
