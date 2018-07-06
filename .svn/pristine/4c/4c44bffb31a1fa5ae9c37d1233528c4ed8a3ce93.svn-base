package biz.objects;

import biz.dto.JeDto;
import biz.exceptions.BusinessException;
import java.util.List;

/**
 * JE's business object.
 */
public interface Je extends JeDto {

  boolean addParticipation(Participation participation);

  boolean addParticipation(Company company);

  List<Participation> getParticipations();

  Participation getParticipation(Participation participation);

  Participation getParticipation(Company company);

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
