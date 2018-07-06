package mock;

import java.sql.PreparedStatement;

import config.annotation.Service;
import dal.DalBackEndServices;
import dal.DalFrontEndServices;

/**
 * Services for the communication with Database.
 */
class DalServicesMock implements DalBackEndServices, DalFrontEndServices {

  /**
   * Constructor.
   *
   */
  @Service()
  public DalServicesMock() {}

  /**
   * Start transaction mode.
   */
  public void startTransaction() {}

  /**
   * Commit the current transaction.
   */
  public void commitTransaction() {}

  /**
   * Roll back the current transaction.
   */
  public void rollbackTransaction() {}

  /**
   * Prepare a {@link PreparedStatement} if not already cached.
   *
   * @param statement {@link String} query to prepare.
   * @return a {@link PreparedStatement} based on the given {@link String}
   */
  public PreparedStatement prepareStatement(String statement) {
    return null;
  }

}
