package corendonlmsv2.connectivity;

import corendonlmsv2.main.util.StringUtil;
import corendonlmsv2.model.DatabaseTables;
import corendonlmsv2.model.IStorable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Maintains a connection to the application's underlying database connection
 * and acts as a pipeline for outgoing SQL queries and updates
 *
 * @author Emile Pels
 */
public class DbManager
{

    /**
     * Password for the database connection
     */
    private static final String DATABASE_PASSWORD = "emilepels";

    /**
     * URL for the database connection
     */
    private static final String DATABASE_URL = "jdbc:mysql://localhost/LMS_DB";

    /**
     * Username for the database connection
     */
    private static final String DATABASE_USERNAME = "root";

    /**
     * Indicates whether the database is currently connected
     */
    private static boolean _connected;

    /**
     * Resembles the current database connection
     */
    private static Connection _dbConnection;

    private DbManager()
    {
    }

    /**
     * Closes the connection to the underlaying database connection
     */
    public static void close()
    {
        try
        {
            _dbConnection.close();
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
            _connected = false;
        }
    }

    /**
     * Opens the connection to the database, using the credentials and details
     * as set in the constants in this class
     */
    public static void connect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex)
        {
            System.err.println("JDBC exception: " + ex.getMessage());
        }

        try
        {
            _dbConnection = DriverManager.getConnection(DATABASE_URL,
                    DATABASE_USERNAME, DATABASE_PASSWORD);

            _connected = true;
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
        }
    }

    /**
     * Executes a query to the database connection
     *
     * @param query SQL query to execute
     * @return ResultSet object containing the query's results
     */
    public static ResultSet executeQuery(String query)
    {
        if (!_connected)
        {
            connect();
        }

        ResultSet results = null;

        try
        {
            Statement statement = _dbConnection.createStatement();

            results = statement.executeQuery(query);
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
            _connected = false;
        }

        return results;
    }

    /**
     * Executes a update to the database connection
     *
     * @param update SQL update to execute
     */
    public static void executeUpdate(String update)
    {
        if (!_connected)
        {
            connect();
        }

        try
        {
            Statement statement = _dbConnection.createStatement();

            statement.executeUpdate(update);
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
            _connected = false;
        }
    }

    /**
     * Gets the content for the last entry in a specified column
     *
     * @param table Table to search
     * @param column Column to search
     * @return Content for the last entry in a specified column
     */
    public static String getLastEntry(DatabaseTables table, String column)
    {
        ResultSet results = getResultSet(table);

        String lastEntry = null;

        try
        {
            while (results.next())
            {
                lastEntry = results.getString(column);
            }
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
            _connected = false;
        }

        return lastEntry;
    }

    /**
     * Gets all the items in the respective table matching the query in the
     * specified column
     *
     * @param table Database table to search
     * @param query String query to search the specified table for. If null,
     * every database entry is returned
     * @param column Column to search. If null, all columns are searched
     * @param absolute Indicates whether only exact matches are accepted. If
     * false, the returned set will only contain results in which the query
     * string is contained (rather than content tested for equality)
     * @return ResultSet object containing all database entries matching the
     * specified query
     */
    public static ResultSet getResultSet(DatabaseTables table, String query,
            String column, boolean absolute)
    {
        StringBuilder builder = new StringBuilder("SELECT * FROM "
                + table.getDatabaseIdentifier());

        if (!StringUtil.isStringNullOrWhiteSpace(query)
                && !StringUtil.isStringNullOrWhiteSpace(column))
        {
            builder.append(" WHERE ")
                    .append(column);

            if (absolute)
            {
                builder.append(String.format("='%s'", query));
            } else
            {
                builder.append(" LIKE '%")
                        .append(query)
                        .append("%'");
            }
        }

        return DbManager.executeQuery(builder.toString());
    }

    /**
     * Gets all the items in the respective table
     *
     * @param table Database table to search
     * @return ResultSet object containing all database entries matching the
     * specified query
     */
    public static ResultSet getResultSet(DatabaseTables table)
    {
        return getResultSet(table, null, null, false);
    }

    /**
     * Gets the amount of rows in the specified table
     *
     * @param table Table to get amount of rows for
     * @return Amount of rows in the specified table
     */
    public static int getRowLength(DatabaseTables table)
    {
        String query = "SELECT COUNT(*) FROM " + table.getDatabaseIdentifier();

        ResultSet results = executeQuery(query);

        int count = -1;

        try
        {
            if (results.next())
            {
                count = results.getInt(1);
            }
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
            _connected = false;
        }

        return count;
    }

    /**
     * Adds an instance of IStorable to the database
     *
     * @param value Instance of IStorable to add to the database
     * @return Boolean indicating whether the amount of rows in this database
     * was succesfully incremented by 1 (one) throughout the execution of this
     * method
     */
    public static boolean insert(IStorable value)
    {
        DatabaseTables table = value.getTable();

        //Get the amount of rows before executing the update
        int beforeCount = getRowLength(table);

        //Execute the update
        executeUpdate(value.getUpdate());

        //Get the amount of rows after executing the update
        int afterCount = getRowLength(table);

        //Base success on whether the amount of rows in the table increased by 1
        return afterCount == beforeCount + 1;
    }
}
