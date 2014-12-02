package corendonlmsv2.model;

import corendonlmsv2.main.util.DateUtil;

/**
 * Represents a piece of luggage for database writing
 *
 * @author Emile Pels
 */
public class Luggage implements IStorable
{

    /**
     * Database table to store objects of this type in
     */
    private static final DatabaseTables TABLE = DatabaseTables.LUGGAGE;
        
    private final String brandName, color, customerId, 
            details, location, dateMissing;
    private final LuggageStatuses status;

    /**
     * Initializes a new object of luggage
     * 
     * @param brandName Luggage's brand name
     * @param color Luggage's color
     * @param customerId Customer ID the luggage belongs to
     * @param details Luggage's details
     * @param location Luggage's location
     * @param status Luggage's status
     * @throws IllegalArgumentException Exception thrown if the customer ID is
     * not registered, or the ID is not valid
     */
    public Luggage(String brandName, String color, String customerId, 
            String details, String location, LuggageStatuses status) 
            throws IllegalArgumentException
    {
        if (!Customer.isCustomerIdRegistered(customerId) || customerId == null)
        {
            throw new IllegalArgumentException("The customer ID is not"
                    + " registered to a customer or is invalid.");
        }
        
        this.brandName = brandName;
        this.color = color;
        this.customerId = customerId;
        this.details = details;
        this.location = location;
        this.status = status;
        
        this.dateMissing = (status == LuggageStatuses.MISSING) 
                ? DateUtil.getDateString() : null;
    }
    
    /**
     * Get the value of brandName
     *
     * @return the value of brandName
     */
    public String getBrandName()
    {
        return brandName;
    }

    /**
     * Get the value of Color
     *
     * @return the value of Color
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Get the value of customerId
     *
     * @return the value of customerId
     */
    public String getCustomerId()
    {
        return customerId;
    }

    /**
     * Get the value of dateMissing
     *
     * @return the value of dateMissing
     */
    public String getDateMissing()
    {
        return dateMissing;
    }
    
    /**
     * Get the value of details
     *
     * @return the value of details
     */
    public String getDetails()
    {
        return details;
    }

    /**
     * Get the value of location
     *
     * @return the value of location
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public LuggageStatuses getStatus()
    {
        return status;
    }

    @Override
    public DatabaseTables getTable()
    {
        return TABLE;
    }

    @Override
    public String getUpdate()
    {
        return String.format("INSERT INTO %s (brand_name, color, customer_id,"
                + " details, location, status, date) VALUES "
                + "('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                TABLE.getDatabaseIdentifier(), brandName, color, customerId,
                details, location, status, 
                dateMissing == null ? "" : dateMissing);
    }
    
    /**
     * Represents the various statuses a piece of luggage can have.
     *
     * Ideally, luggage is first created as TRANSIT. When the luggage is
     * succesfully delivered, the status becomes COMPLETED and the case is
     * closed.
     *
     * If the luggage happens to go missing, the status is set to MISSING. When
     * the luggage is found, the status becomes RESOLVED and the case is closed
     */
    public enum LuggageStatuses
    {

        /**
         * Luggage is missing
         */
        MISSING,
        /**
         * Luggage was missing, but the case is now resolved
         */
        RESOLVED,
        /**
         * Luggage is on transit as planned
         */
        TRANSIT,
        /**
         * Luggage is succesfully delivered and case is closed
         */
        COMPLETED;
        
        public String getDatabaseIdentifier()
        {
            return toString().toLowerCase();
        }
    }
}
