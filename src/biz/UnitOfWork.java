package biz;

import biz.dto.AbstractDto;

public interface UnitOfWork {

  void startTransaction();
  
  void commitTransaction();
  
  void rollbackTransaction();
  
  void insert(AbstractDto abstractDto);
  
  void update(AbstractDto abstractDto);
  
  void delete(AbstractDto abstractDto);

  AbstractDto getInsertedObject();
  
  AbstractDto getUpdatedObject();
  
  AbstractDto getDeletedObject();
  
}
