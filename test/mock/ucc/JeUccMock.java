package mock.ucc;

import java.util.Comparator;
import java.util.List;

import biz.JeUcc;
import biz.dto.JeDto;
import biz.objects.Je;
import dal.dao.JeDao;

class JeUccMock implements JeUcc {

  private JeDao jeDao;
  
  JeUccMock(JeDao jeDao) {
    this.jeDao = jeDao;
  }
  
  @Override
  public JeDto createNewJe(JeDto jeDto) {
    return jeDao.create((Je) jeDto);
  }

  @Override
  public List<JeDto> getAllJe() {
    return jeDao.findAllJe();
  }

  @Override
  public List<JeDto> getPreviousJe() {
    List<JeDto> list = this.jeDao.findAllJe();

    list.sort(new Comparator<JeDto>() {
      @Override
      public int compare(JeDto o1, JeDto o2) {
        return o2.getDayDate().compareTo(o1.getDayDate());
      }
    });

    if (list.size() >= 5) {
      return list.subList(0, 5);
    }
    return list;
  }

  @Override
  public JeDto getJeByYear(int year) {
    return jeDao.findByYear(year);
  }

}
