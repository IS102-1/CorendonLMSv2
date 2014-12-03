package corendonlmsv2.model;

/**
 * Interface implementable by objects which can be inserted into the database
 *
 * @author Emile Pels
 */
public interface IStorable
{

    /**
     * Gets the table to which this IStorable instance should be added
     *
     * @return Table to which this IStorable instance should be added
     */
    public DatabaseTables getTable();

    /**
     * Gets the update string to add this IStorable instance to the database
     *
     * @return Update string to add this IStorable instance to the database
     */
    public String getUpdate();
    
    /**
     * Inserts this object into the database by calling DbManager.insert
     * 
     * @return Boolean indicating whether the amount of rows in this database
     * was succesfully incremented by 1 (one) throughout the execution of this
     */
    public boolean insert();
}
