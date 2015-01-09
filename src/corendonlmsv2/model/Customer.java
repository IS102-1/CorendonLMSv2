package corendonlmsv2.model;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.connectivity.LanguageController;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.main.util.StringUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public static final DatabaseTables TABLE = DatabaseTables.CUSTOMERS;

    private final String address, country, emailAddress, name, phoneNumber;
    private String customerId;

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
            String name, String phoneNumber)
    {
        this.address = address;
        this.country = country;
        this.emailAddress = emailAddress;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets all customers in the database
     *
     * @return All customers in the database
     */
    public static List<Customer> getAllCustomers()
    {
        List<Customer> customers = new ArrayList<>();

        ResultSet results = DbManager.getResultSet(TABLE);

        String customerIdColumn = TABLE.getColumnAt(TableColumns.CUSTOMER_ID),
                nameColumn = TABLE.getColumnAt(TableColumns.NAME),
                addressColumn = TABLE.getColumnAt(TableColumns.ADDRESS),
                countryColumn = TABLE.getColumnAt(TableColumns.COUNTRY),
                emailColumn = TABLE.getColumnAt(TableColumns.EMAIL_ADDRESS),
                phoneColumn = TABLE.getColumnAt(TableColumns.PHONE_NUMBER);

        try
        {
            while (results.next())
            {
                Customer customer = new Customer(
                        results.getString(addressColumn),
                        results.getString(countryColumn),
                        results.getString(emailColumn),
                        results.getString(nameColumn),
                        results.getString(phoneColumn)
                );
                customer.setCustomerId(results.getString(customerIdColumn));

                customers.add(customer);
            }
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
        }

        return customers;
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
     * Get the value of customerID
     *
     * @return the value of customerId
     */
    public String getCustomerId()
    {
        return customerId;
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

    /**
     * Sets the value of customerId
     *
     * @param customerId new value of customerId
     */
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    @Override
    public DatabaseTables getTable()
    {
        return TABLE;
    }

    @Override
    public String getUpdate() throws IllegalArgumentException
    {
        if (isPhoneNumberRegistered(phoneNumber))
        {
            throw new IllegalArgumentException(
                    LanguageController.getString("numberRegistered"));
        }

        return String.format("INSERT INTO %s (name, address, country, "
                + "email_address, phone_number) VALUES ('%s', '%s', '%s', "
                + "'%s', '%s')", TABLE.getDatabaseIdentifier(), name, address,
                country, emailAddress, phoneNumber);
    }

    @Override
    public boolean insert()
    {
        return DbManager.insert(this);
    }

    /**
     * Holds indices for the TABLE's columns
     */
    public class TableColumns
    {

        /**
         * Index for the column customer_id
         */
        public static final int CUSTOMER_ID = 0;

        /**
         * Index for the column name
         */
        public static final int NAME = 1;

        /**
         * Index for the column address
         */
        public static final int ADDRESS = 2;

        /**
         * Index for the column country
         */
        public static final int COUNTRY = 3;

        /**
         * Index for the column email_address
         */
        public static final int EMAIL_ADDRESS = 4;

        /**
         * Index for the column phone_number
         */
        public static final int PHONE_NUMBER = 5;
    }
}
