package dal;

/**
 * Supply a common interface to communicate with database.
 */
public interface DalFrontEndServices {

  /**
   * Start transaction mode.
   */
  void startTransaction();

  /**
   * Commit the current transaction.
   */
  void commitTransaction();

  /**
   * Roll back the current transaction.
   */
  void rollbackTransaction();


}
