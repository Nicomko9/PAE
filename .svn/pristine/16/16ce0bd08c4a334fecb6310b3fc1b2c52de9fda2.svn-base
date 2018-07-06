package mock.dao;

import biz.dto.AbstractDto;
import biz.dto.PresenceDto;
import biz.objects.Presence;
import dal.dao.PresenceDao;
import java.util.List;
import mock.DataBaseMock;

class PresenceDaoMock implements PresenceDao {

  private DataBaseMock dataBaseMock;

  PresenceDaoMock(DataBaseMock dataBaseMock) {
    this.dataBaseMock = dataBaseMock;
  }

  @Override
  public PresenceDto create(Presence presence) {
    return dataBaseMock.addPresence(presence);
  }

  @Override
  public AbstractDto insert(AbstractDto abstractDto) {
    return create((Presence) abstractDto);
  }
  
  @Override
  public List<PresenceDto> findByYear(int year) {
    return dataBaseMock.getPresencesByYear(year);
  }


  @Override
  public PresenceDto update(Presence presence) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public AbstractDto update(AbstractDto abstractDto) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<PresenceDto> findPresencesForContact(int id) {
    return dataBaseMock.getPresencesForContact(id);
  }

  @Override
  public PresenceDto findForJe(int pkJe, int pkCompany, int pkContact) {
    return null;
  }

  @Override
  public void cacheClean() {}
}
