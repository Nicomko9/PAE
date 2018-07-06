package biz.objects;

import biz.dto.ContactDto;
import biz.exceptions.BusinessException;

/**
 * Contact's business object.
 */
public interface Contact extends ContactDto {

  /**
   * Prepare the {@link Contact} to be insert in the database.
   */
  void prepareForInsert();

  /**
   * Check some Constraints.
   *
   * @throws BusinessException if of no respect of the constraints.
   */
  void checkConstraints() throws BusinessException;
  
  String toString();

}
