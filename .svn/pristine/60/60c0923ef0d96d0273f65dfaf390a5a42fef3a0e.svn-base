package dal;

import java.sql.PreparedStatement;

/**
 * Supply a common interface to communicate with database.
 */
public interface DalBackEndServices {

  /**
   * Prepare a {@link PreparedStatement}, if not already cached.
   *
   * @param sql query to prepare
   * @return a {@link PreparedStatement} based on the given {@link String}
   */
  PreparedStatement prepareStatement(String sql);

}
