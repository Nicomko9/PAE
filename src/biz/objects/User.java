package biz.objects;

import biz.dto.UserDto;
import biz.exceptions.BusinessException;


/**
 * User's business object.
 */
public interface User extends UserDto {

  /**
   * Compare password.
   *
   * @param password password to be compare to
   * @return <code>true</code> in case of match
   */
  boolean comparePassword(String password);

  /**
   * Check some Constraints.
   *
   * @throws BusinessException if of no respect of the constraints.
   */
  void checkConstraints();

  /**
   * Prepare the {@link User} to be insert in the database while setting his Inscription Date.
   */
  void prepareForInsert();

}
