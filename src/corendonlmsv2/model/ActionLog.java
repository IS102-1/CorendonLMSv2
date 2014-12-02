package corendonlmsv2.model;

import corendonlmsv2.main.util.DateUtil;

/**
 * Represents a log for an action performed by the user
 *
 * @author Emile Pels
 */
public class ActionLog implements IStorable
{

    /**
     * Database table to store objects of this type in
     */
    private static final DatabaseTables TABLE = DatabaseTables.LOGS;

    /**
     * User account from which the action was performed
     */
    private final UserAccount userAccount;

    /**
     * String containing the date and time on which the action was performed
     */
    private final String dateTime;

    /**
     * Message describing what the action involved
     */
    private final String message;

    /**
     * Initializes a new object of ActionLog
     *
     * @param userAccount User account the action was invoked from
     * @param message Message to log for this action
     */
    public ActionLog(UserAccount userAccount, String message)
    {
        this.userAccount = userAccount;
        this.message = message;
        this.dateTime = DateUtil.getDateTimeString();
    }

    /**
     * Initializes a new object of ActionLog
     *
     * @param userAccount User account the action was invoked from
     * @param message Message to log for this action
     * @param dateTime Date time for the moment this log was created
     */
    public ActionLog(UserAccount userAccount, String message, String dateTime)
    {
        this.userAccount = userAccount;
        this.message = message;
        this.dateTime = dateTime;
    }

    /**
     * Get the date time for the action log
     *
     * @return Date time for the action log
     */
    public String getDateTime()
    {
        return dateTime;
    }

    /**
     * Get the message for the action log
     *
     * @return Message for the action log
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Get the UserAccount for the action log
     *
     * @return UserAccount for the action log
     */
    public UserAccount getUserAccount()
    {
        return userAccount;
    }

    @Override
    public DatabaseTables getTable()
    {
        return DatabaseTables.LOGS;
    }

    @Override
    public String getUpdate()
    {
        return String.format("INSERT INTO %s (username, user_role, date_time, "
                + "log_message) VALUES ('%s', '%s', '%s', '%s')",
                TABLE.getDatabaseIdentifier(), userAccount.getUsername(),
                userAccount.getUserRole(), dateTime, message);
    }
}
