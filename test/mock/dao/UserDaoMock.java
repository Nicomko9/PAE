package mock.dao;

import biz.dto.AbstractDto;
import biz.dto.UserDto;
import biz.objects.User;
import dal.dao.UserDao;
import mock.DataBaseMock;

public class UserDaoMock implements UserDao {

  private DataBaseMock dataBaseMock;
  
  UserDaoMock(DataBaseMock dataBaseMock) {
    this.dataBaseMock = dataBaseMock;
  }
  
  @Override
  public UserDto create(User user) {    
    return dataBaseMock.addUser(user);
  }
  
  @Override
  public UserDto insert(AbstractDto abstractDto) {
    return create((User) abstractDto);
  }
  
  @Override
  public AbstractDto update(AbstractDto abstractDto) {
    return null;
  }

  @Override
  public UserDto findByLogin(String login) {
    return dataBaseMock.getUserByLogin(login);
  }

  @Override
  public UserDto findByEmail(String email) {
    return dataBaseMock.getUserByEmail(email);
  }

  @Override
  public UserDto findByPk(int pk) {
   return dataBaseMock.getUserByPk(pk);
  }

  @Override
  public void cacheClean() {}
}
