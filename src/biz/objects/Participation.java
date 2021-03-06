package biz.objects;

import biz.dto.ParticipationDto;
import biz.exceptions.BusinessException;


public interface Participation extends ParticipationDto {

  boolean confirm();

  boolean refuse();

  boolean invoice();

  boolean pay();

  void cancel();

  boolean isPayed();

  boolean rollback();

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

}
