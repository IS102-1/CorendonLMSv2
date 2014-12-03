package corendonlmsv2.model;

import corendonlmsv2.connectivity.DbManager;
import corendonlmsv2.main.util.MiscUtil;
import corendonlmsv2.main.util.StringUtil;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a user account. Encapsulates the username and the user's role
 *
 * @author Emile Pels
 */
public class UserAccount implements IStorable
{

    /**
     * Database table to store objects of this type in
     */
    private static final DatabaseTables TABLE = DatabaseTables.USERS;

    /**
     * UserAccount currently signed in
     */
    private static UserAccount _current;
    
    private final String username, password;
    private final UserRoles userRole;

    /**
     * Initializes a new UserAccount instance
     *
     * @param username User's username
     * @param password User's password. Can be left empty unless a new account
     * should be registered through this instance. Password is hashed before its
     * value is set
     * @param userRole User's role
     * @throws IllegalArgumentException Exception thrown if the username is
     * already registered to another user account
     */
    public UserAccount(String username, String password, UserRoles userRole)
    {
        this(username, password, userRole, true);
    }

    /**
     * Initializes a new UserAccount instance
     *
     * @param username User's username
     * @param password User's password. Can be left empty unless a new account
     * should be registered through this instance. Password is hashed before its
     * value is set
     * @param userRole User's role
     * @param hashPassword Indicates whether the password should be hashed
     */
    public UserAccount(String username, String password, UserRoles userRole,
            boolean hashPassword)
    {
        this.username = username;
        this.password = hashPassword
                ? StringUtil.hashString(password, true) : password;
        this.userRole = userRole;
    }

    /**
     * Checks whether a specified username is already registered to an existing
     * account
     *
     * @param username Username to check
     * @return Boolean indicating whether the username is already registered to
     * an existing account
     * @throws IllegalArgumentException Exception thrown when username is null
     * or whitespace
     */
    public static boolean isUsernameRegistered(String username)
            throws IllegalArgumentException
    {
        if (StringUtil.isStringNullOrWhiteSpace(username))
        {
            throw new IllegalArgumentException("The customer ID can not be"
                    + " null or whitespace.");
        }

        //Get the rows in TABLE where the username matches (absolute)
        ResultSet results = DbManager.getResultSet(TABLE, username,
                TABLE.getColumnAt(TableColumns.USERNAME), true);

        return !MiscUtil.isResultSetEmpty(results);
    }

    /**
     * Validates the user role for entered credentials
     *
     * @param username Username for this account
     * @param password Password for this account
     * @return UserRoles value representing this account
     */
    public static UserRoles validateUserRole(String username, String password)
    {
        //Get the rows in TABLE where the username matches (absolute)
        ResultSet results = DbManager.getResultSet(TABLE, username,
                TABLE.getColumnAt(TableColumns.USERNAME), true);

        //Hash the candidate password for comparison
        UserRoles role = UserRoles.UNAUTHORIZED;

        try
        {
            //Check if we have any rows
            //
            //If so, get the (hashed) password from the DB and hash candidate 
            //for comparison. If it matches, fetch the role
            if (results.next()
                    && results.getString(TABLE.getColumnAt(TableColumns.PASSWORD))
                    .equals(StringUtil.hashString(password, true)))
            {
                role = UserRoles.valueOf(results.getString(
                        TABLE.getColumnAt(TableColumns.USER_ROLE)).toUpperCase());
            }
        } catch (SQLException ex)
        {
            System.err.println("SQL exception: " + ex.getMessage());
        }

        return role;
    }

    /**
     * Gets the value of _current
     *
     * @return Value of _current
     */
    public static UserAccount getCurrent()
    {
        return _current;
    }

    /**
     * Sets the value of _current
     *
     * @param userAccount New value of _current
     */
    public static void setCurrent(UserAccount userAccount)
    {
        _current = userAccount;
    }

    /**
     * Gets the value of username
     *
     * @return Value of username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Gets the value of userRole
     *
     * @return Value of userRole
     */
    public UserRoles getUserRole()
    {
        return userRole;
    }

    @Override
    public String toString()
    {
        return String.format("%s [%s]", username, userRole);
    }

    @Override
    public DatabaseTables getTable()
    {
        return TABLE;
    }

    @Override
    public String getUpdate()
    {
        //Check if username exists
        if (isUsernameRegistered(username))
        {
            throw new IllegalArgumentException("The username is already"
                    + " registered to another user account.");
        }
        
        return String.format("INSERT INTO %s (username, password, user_role) "
                + "VALUES ('%s', '%s', '%s')", TABLE.getDatabaseIdentifier(),
                username, password, userRole.getDatabaseIdentifier());
    }

    /**
     * Represents the various user roles an account can have
     */
    public enum UserRoles
    {

        /**
         * User is not authorized to log in
         */
        UNAUTHORIZED,
        /**
         * User has limited permissions, typically for regular employees
         */
        DESK_EMPLOYEE,
        /**
         * User has increased permissions, typically for managers
         */
        MANAGER,
        /**
         * User has all permissions, typically for developers and highly ranked
         * positions
         */
        ADMIN;

        /**
         * Gets the identifier this role has in the DB
         *
         * @return Identifier this role has in the DB
         */
        public String getDatabaseIdentifier()
        {
            return toString().toLowerCase();
        }
    }

    /**
     * Holds indices for the TABLE's columns
     */
    private class TableColumns
    {

        /**
         * Index for the column user_id
         */
        private static final int USER_ID = 0;

        /**
         * Index for the column username
         */
        private static final int USERNAME = 1;

        /**
         * Index for the column password
         */
        private static final int PASSWORD = 2;

        /**
         * Index for the column user_role
         */
        private static final int USER_ROLE = 3;
    }
}
