package biz.objects;

import biz.dto.PresenceDto;
import biz.exceptions.BusinessException;

public interface Presence extends PresenceDto {

  /**
   * Prepare the {@link Contact} to be insert in the database.
   */
  void prepareForInsert();

  /**
   * Check some Constraints.
   *
   * @throws BusinessException if of no respect of the constraints.
   */
  void checkConstraints();

}
