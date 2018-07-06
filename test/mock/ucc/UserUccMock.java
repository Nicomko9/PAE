package mock.ucc;

import biz.UserUcc;
import biz.dto.UserDto;
import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import biz.objects.User;
import dal.dao.UserDao;

class UserUccMock implements UserUcc {

  private UserDao userDao;

  UserUccMock(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public UserDto connect(String login, String password) {
    User user = (User) userDao.findByLogin(login);
    if(user == null) {
      throw new BusinessException(ErrorsCode.BUSINESS_INCORRECT_LOGIN);
    } 
    if(!user.comparePassword(password)) {
      throw new BusinessException(ErrorsCode.BUSINESS_INCORRECT_PASSWORD);
    }
    return user;
  }

  @Override
  public UserDto subscribe(UserDto biz) {
    if(null != userDao.findByLogin(biz.getLogin())) {
      throw new BusinessException(ErrorsCode.BUSINESS_LOGIN_USER_EXISTED);
    }
    return userDao.create((User) biz);
  }

  @Override
  public UserDto getUserByLogin(String login) {
   return userDao.findByLogin(login);
  }

}
