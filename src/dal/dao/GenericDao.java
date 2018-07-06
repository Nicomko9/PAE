package dal.dao;

import biz.dto.AbstractDto;

public interface GenericDao {

  AbstractDto insert(AbstractDto abstractDto);
  
  AbstractDto update(AbstractDto abstractDto);

  //AbstractDto delete(AbstractDto abstractDto);

  void cacheClean();

}
