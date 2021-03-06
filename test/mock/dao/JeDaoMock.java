package mock.dao;

import biz.dto.AbstractDto;
import biz.dto.JeDto;
import biz.objects.Je;
import dal.dao.JeDao;
import java.util.List;
import mock.DataBaseMock;

public class JeDaoMock implements JeDao {

  private DataBaseMock dataBaseMock;
  
  JeDaoMock(DataBaseMock dataBaseMock) {
    this.dataBaseMock = dataBaseMock;
  }
  
  @Override
  public JeDto findByYear(int year) {
    return dataBaseMock.getJeByYear(year);
  }

  @Override
  public JeDto create(Je je) {
    return dataBaseMock.addJe(je);
  }
  
  @Override
  public AbstractDto insert(AbstractDto abstractDto) {
    return create((Je) abstractDto);
  }
  
  @Override
  public AbstractDto update(AbstractDto abstractDto) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JeDto> findAllJe() {
    return dataBaseMock.getJes();
  }

  @Override
  public void cacheClean() {}
}
