package corendonlmsv2.model;

/**
 * Contains data regarding the tables in the underlaying database
 *
 * @author Emile Pels
 */
public enum DatabaseTables
{

    /**
     * Database table to store customers (of Customer) in
     */
    CUSTOMERS(new String[]
    {
        "customer_id",
        "name",
        "address",
        "country",
        "email_address",
        "phone_number"
    }),
    /**
     * Database table to store logs (of ActionLog) in
     */
    LOGS(new String[]
    {
        "log_id",
        "username",
        "user_role",
        "date_time",
        "log_message"
    }),
    /**
     * Database table to store luggage (of Luggage) in
     */
    LUGGAGE(new String[]
    {
        "luggage_id",
        "brand_name",
        "color",
        "customer_id",
        "details",
        "location",
        "status",
        "date_entered"
    }),
    /**
     * Database table to store users (of UserAccount) in
     */
    USERS(new String[]
    {
        "user_id",
        "username",
        "password",
        "user_role"
    });

    /**
     * All columns in this database
     */
    private final String[] columns;

    /**
     * Initializes a new object of DatabaseTables, and sets the columns
     * appropriatly
     *
     * @param columns Columns in this database
     */
    private DatabaseTables(String[] columns)
    {
        this.columns = columns;
    }

    /**
     * Gets the column at a certain index. If the index is out of range, null
     * will be returned
     *
     * @param index Index to get the column for
     * @return Column at a certain index. If the index is out of range, null
     * will be returned
     */
    public String getColumnAt(int index)
    {
        if (index >= columns.length)
        {
            return null;
        }

        return columns[index];
    }

    /**
     * Gets the amount of columns in this database
     *
     * @return Amount of columns in this database
     */
    public int getColumnLength()
    {
        return columns.length;
    }

    /**
     * Gets an array of all columns in this database
     *
     * @return Array of all columns in this database
     */
    public String[] getColumns()
    {
        return columns;
    }

    /**
     * Gets the database identifier for this table
     *
     * @return Database identifier for this table
     */
    public String getDatabaseIdentifier()
    {
        return toString().toLowerCase();
    }
}
