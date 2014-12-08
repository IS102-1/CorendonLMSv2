package corendonlmsv2.main.util;

/**
 * Aids in writing PDF files to the disk. Used for generating reports
 *
 * @author Emile
 */
public class PdfUtil
{
    private PdfUtil()
    {
    }
    
    /**
     * Generates a PDF from an existing luggage ID
     * 
     * @param luggageId ID for the luggage to write to PDF
     * @return Filename for the generated PDF
     */
    public static String generatePdf(String luggageId)
    {
        if (StringUtil.isStringNullOrWhiteSpace(luggageId))
        {
            throw new IllegalArgumentException("Luggage ID can not be empty.");
        }
        
        String fileName = String.format("Report-%d.pdf", 
                System.currentTimeMillis());
        
        
        return fileName;
    }
    
    /**
     * Builds a string containing an HTML file for reporting luggage
     * 
     * @param luggageId ID for the luggage to generate HTML source for
     * @return HTML source reporting luggage
     */
    private static String getReportString(String luggageId)
    {
        throw new UnsupportedOperationException("");
    }
}
