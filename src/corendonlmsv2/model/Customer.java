package corendonlmsv2.model;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.main.util.StringUtil;
import java.sql.ResultSet;

/**
 * Represents a customer and their contact details
 *
 * @author Emile Pels
 */
public class Customer implements IStorable
{

    /**
     * Database table to store objects of this type in
     */
    private static final DatabaseTables TABLE = DatabaseTables.CUSTOMERS;

    private final String address, country, emailAddress, name, phoneNumber;

    /**
     * Initializes a new Customer instance
     *
     * @param address Customer's address
     * @param country Customer's country of residence
     * @param emailAddress Customer's e-mail address
     * @param name Customer's name
     * @param phoneNumber Customer's phone number
     * @throws IllegalArgumentException Exception thrown if the phone number is
     * already registered to another customer
     */
    public Customer(String address, String country, String emailAddress,
            String name, String phoneNumber) throws IllegalArgumentException
    {
        if (isPhoneNumberRegistered(phoneNumber))
        {
            throw new IllegalArgumentException("The phone number is already"
                    + " registered to an existing customer.");
        }
        
        this.address = address;
        this.country = country;
        this.emailAddress = emailAddress;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Checks whether a specified customer ID is already registered to an
     * existing customer
     *
     * @param customerId Customer ID to check
     * @return Boolean indicating whether the customer ID is already registered
     * to an existing customer
     * @throws IllegalArgumentException Exception thrown when customer ID is
     * null or whitespace
     */
    public static boolean isCustomerIdRegistered(String customerId)
            throws IllegalArgumentException
    {
        if (StringUtil.isStringNullOrWhiteSpace(customerId))
        {
            throw new IllegalArgumentException("The customer ID can not be"
                    + " null or whitespace.");
        }

        //Get the rows in TABLE where the customerId matches (absolute)
        ResultSet results = DbManager.getResultSet(TABLE, customerId,
                TABLE.getColumnAt(TableColumns.CUSTOMER_ID), true);

        return !MiscUtil.isResultSetEmpty(results);
    }

    /**
     * Checks whether a specified phone number is already registered to an
     * existing customer
     *
     * @param phoneNumber Phone number to check
     * @return Boolean indicating whether the phone number is already registered
     * to an existing customer
     * @throws IllegalArgumentException Exception thrown when phone number is
     * null or whitespace
     */
    public static boolean isPhoneNumberRegistered(String phoneNumber)
            throws IllegalArgumentException
    {
        if (StringUtil.isStringNullOrWhiteSpace(phoneNumber))
        {
            throw new IllegalArgumentException("The phone number can not be"
                    + " null or whitespace.");
        }

        //Get the rows in TABLE where the customerId matches (absolute)
        ResultSet results = DbManager.getResultSet(TABLE, phoneNumber,
                TABLE.getColumnAt(TableColumns.PHONE_NUMBER), true);

        return !MiscUtil.isResultSetEmpty(results);
    }

    /**
     * Get the value of address
     *
     * @return the value of address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * Get the value of country
     *
     * @return the value of country
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * Get the value of emailAddress
     *
     * @return the value of emailAddress
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the value of phoneNumber
     *
     * @return the value of phoneNumber
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    @Override
    public DatabaseTables getTable()
    {
        return TABLE;
    }

    @Override
    public String getUpdate()
    {
        return String.format("INSERT INTO %s (name, address, country, "
                + "email_address, phone_number) VALUES ('%s', '%s', '%s', "
                + "'%s', '%s')", TABLE.getDatabaseIdentifier(), name, address, 
                country, emailAddress, phoneNumber);
    }

    /**
     * Holds indices for the TABLE's columns
     */
    private class TableColumns
    {

        /**
         * Index for the column customer_id
         */
        private static final int CUSTOMER_ID = 0;

        /**
         * Index for the column name
         */
        private static final int NAME = 1;

        /**
         * Index for the column address
         */
        private static final int ADDRESS = 2;

        /**
         * Index for the column country
         */
        private static final int COUNTRY = 3;

        /**
         * Index for the column email_address
         */
        private static final int EMAIL_ADDRESS = 4;

        /**
         * Index for the column phone_number
         */
        private static final int PHONE_NUMBER = 5;
    }
}
