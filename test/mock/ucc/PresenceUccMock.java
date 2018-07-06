package mock.ucc;

import java.util.List;

import biz.PresenceUcc;
import biz.dto.JeDto;
import biz.dto.PresenceDto;
import biz.objects.Presence;
import dal.dao.PresenceDao;

class PresenceUccMock implements PresenceUcc {

  private PresenceDao presenceDao;
  
  PresenceUccMock(PresenceDao presenceDao) {
    this.presenceDao = presenceDao;
  }
  
  @Override
  public PresenceDto register(PresenceDto presence) {
    return presenceDao.create((Presence) presence);
  }

  @Override
  public List<PresenceDto> getAllPresencesOf(JeDto jeDto) {
    return presenceDao.findByYear(jeDto.getDayYear());
  }

  @Override
  public List<PresenceDto> register(List<PresenceDto> presencesDto) {
    for (int idx = 0; idx < presencesDto.size(); idx++) {
      PresenceDto PresenceDto = presencesDto.get(idx);
      Presence participation = (Presence) PresenceDto;
      presenceDao.create(participation);
    }
    return presencesDto;
  }

  @Override
  public List<PresenceDto> getPresencesForContact(int id) {
    return presenceDao.findPresencesForContact(id);
  }

}
