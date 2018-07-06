package biz.objects;

import biz.dto.CompanyDto;
import biz.exceptions.BusinessException;

/**
 * Company's business object.
 */
public interface Company extends CompanyDto {

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
