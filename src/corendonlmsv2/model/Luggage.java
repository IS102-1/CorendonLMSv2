package corendonlmsv2.model;

import corendonlmsv2.connectivity.DbManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public static final DatabaseTables TABLE = DatabaseTables.LUGGAGE;

    private final String brandName, color, customerId,
            details, location;
    private final LuggageStatuses status;

    private String luggageId, dateMissing;

    /**
     * Initializes a new object of luggage
     *
     * @param brandName Luggage's brand name
     * @param color Luggage's color
     * @param customerId Customer ID the luggage belongs to
     * @param details Luggage's details
     * @param location Luggage's location
     * @param status Luggage's status
     */
    public Luggage(String brandName, String color, String customerId,
            String details, String location, LuggageStatuses status)
    {
        this.brandName = brandName;
        this.color = color;
        this.customerId = customerId;
        this.details = details;
        this.location = location;
        this.status = status;
    }

    /**
     * Gets all the luggage in the database
     *
     * @return All luggage in the database
     */
    public static List<Luggage> getAllLuggage()
    {
        List<Luggage> allLuggage = new ArrayList<>();

        ResultSet results = DbManager.getResultSet(TABLE);

        String luggageIdColumn = TABLE.getColumnAt(TableColumns.LUGGAGE_ID),
                brandNameColumn = TABLE.getColumnAt(TableColumns.BRAND_NAME),
                colorColumn = TABLE.getColumnAt(TableColumns.COLOR),
                customerIdColumn = TABLE.getColumnAt(TableColumns.CUSTOMER_ID),
                detailsColumn = TABLE.getColumnAt(TableColumns.DETAILS),
                locationColumn = TABLE.getColumnAt(TableColumns.LOCATION),
                statusColumn = TABLE.getColumnAt(TableColumns.STATUS),
                dateMissingColumn = TABLE.getColumnAt(TableColumns.DATE_MISSING);

        try
        {
            while (results.next())
            {
                Luggage luggage = new Luggage(
                        results.getString(brandNameColumn),
                        results.getString(colorColumn),
                        results.getString(customerIdColumn),
                        results.getString(detailsColumn),
                        results.getString(locationColumn),
                        LuggageStatuses.valueOf(
                                results.getString(statusColumn).toUpperCase())
                );
                luggage.setLuggageId(results.getString(luggageIdColumn));
                luggage.setDateMissing(results.getString(dateMissingColumn));

                allLuggage.add(luggage);
            }
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
        }

        return allLuggage;
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
     * Get the value of luggageId
     *
     * @return the value of luggageId
     */
    public String getLuggageId()
    {
        return luggageId;
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

    /**
     * Sets the value of dateMissing
     *
     * @param dateMissing new value for dateMissing
     */
    public void setDateMissing(String dateMissing)
    {
        this.dateMissing = dateMissing;
    }

    /**
     * Sets the value of luggageId
     *
     * @param luggageId new value for luggageId
     */
    public void setLuggageId(String luggageId)
    {
        this.luggageId = luggageId;
    }

    @Override
    public DatabaseTables getTable()
    {
        return TABLE;
    }

    @Override
    public String getUpdate() throws IllegalArgumentException
    {
//        if (!Customer.isCustomerIdRegistered(customerId) || customerId == null)
//        {
//            throw new IllegalArgumentException("The customer ID is not"
//                    + " registered to a customer or is invalid.");
//        }

        return String.format("INSERT INTO %s (brand_name, color, customer_id,"
                + " details, location, status, date_missing) VALUES "
                + "('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                TABLE.getDatabaseIdentifier(), brandName, color, customerId,
                details, location, status, dateMissing);
    }

    @Override
    public boolean insert()
    {
        return DbManager.insert(this);
    }

    /**
     * Enumeration represents the various statuses a piece of luggage can have.
     *
     * Ideally, luggage is first assigned the value NORMAL when registered.
     * Under normal circumstances, it stays like this. However, when a piece of
     * luggage is reported lost by a customer, its status is changed to LOST.
     *
     * Alternatively, a piece of luggage can be found although its owner is not
     * known. It is then assigned the value FOUND, until the customer reports in
     * and the luggage is transferred back to the owner. When luggage has had
     * the MISSING or FOUND status, but is later returned to the owner, the
     * status RESOLVED is appointed to said luggage.
     */
    public enum LuggageStatuses
    {

        /**
         * Luggage was signed in under normal circumstances and has not been
         * lost to date
         */
        NORMAL,
        /**
         * Luggage is reported missing by the customer; the location of this
         * piece of luggage is unknown
         */
        MISSING,
        /**
         * Luggage has been found, but the owner is unknown
         */
        FOUND,
        /**
         * Luggage was missing or has been found, but the case is now resolved
         */
        RESOLVED;

        public String getDatabaseIdentifier()
        {
            return toString().toLowerCase();
        }
    }

    /**
     * Holds the luggage table's indices for eased access through
     * DatabaseTables.getColumnAt
     */
    public class TableColumns
    {

        /**
         * Index for the column luggage_id
         */
        public static final int LUGGAGE_ID = 0;

        /**
         * Index for the column brand_name
         */
        public static final int BRAND_NAME = 1;

        /**
         * Index for the column color
         */
        public static final int COLOR = 2;

        /**
         * Index for the column customer_id
         */
        public static final int CUSTOMER_ID = 3;

        /**
         * Index for the column details
         */
        public static final int DETAILS = 4;

        /**
         * Index for the column location
         */
        public static final int LOCATION = 5;

        /**
         * Index for the column status
         */
        public static final int STATUS = 6;

        /**
         * Index for the column date_missing
         */
        public static final int DATE_MISSING = 7;
    }
}
