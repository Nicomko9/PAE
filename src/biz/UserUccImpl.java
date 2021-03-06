package biz;

import biz.dto.UserDto;
import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import biz.exceptions.FatalException;
import biz.objects.User;
import dal.DalFrontEndServices;
import dal.dao.UserDao;
import ihm.utils.DataValidator;
import ihm.utils.Logger;

/**
 * Implementation of the User's Use Case Controller.
 */
class UserUccImpl implements UserUcc {

  private DalFrontEndServices dal;

  private UserDao userDao;
  
  private UnitOfWork uof;

  UserUccImpl(DalFrontEndServices dal, UserDao userDao, UnitOfWork uof) {
    this.dal = dal;
    this.userDao = userDao;
    this.uof = uof;
  }

  /**
   * Start a connection.
   *
   * @param login login of the user to connect
   * @param password password of the user to connect
   * @return the information of the user in the Database.
   */
  @Override
  public UserDto connect(String login, String password) {
    if (!DataValidator.validateString(login) || !DataValidator.validateString(password)) {
      throw new BusinessException(ErrorsCode.HTTP_ERROR_BAD_REQUEST);
    }

    this.dal.startTransaction();

    User user = (User) this.userDao.findByLogin(login);

    if (user == null) {
      Logger.log("UserUccImpl", "connect", "The login is unknown", Logger.ERROR);
      this.dal.rollbackTransaction();
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_CREDENTIALS);
    }
    if (!user.comparePassword(password)) {
      Logger.log("UserUccImpl", "connect", "The password is incorrect", Logger.ERROR);
      this.dal.rollbackTransaction();
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_CREDENTIALS);
    }

    Logger.log("UserUccImpl", "connect", "The user " + login + " was successsfully connected",
        Logger.SUCCESS);

    this.dal.commitTransaction();

    return user;
  }

  /**
   * Register a new user into the Database.
   *
   * @param userDto the user to persist
   * @return a boolean to indicate whether or not it worked.
   */
  @Override
  public UserDto subscribe(UserDto userDto) {

    User user = (User) userDto;
    user.checkConstraints();

    if (null != this.userDao.findByLogin(user.getLogin())) {
      Logger.log("UserUccImpl", "subscribe", "The login " + user.getLogin() + " exists already",
          Logger.ERROR);
      throw new BusinessException(ErrorsCode.BUSINESS_LOGIN_USER_EXISTED);
    } 
    if (null != this.userDao.findByEmail(user.getEmail())) {
      Logger.log("UserUccImpl", "subscribe", "The mail " + user.getEmail() + " exists already",
          Logger.ERROR);
      throw new BusinessException(ErrorsCode.BUSINESS_EMAIL_USER_EXISTED);
    }

    user.prepareForInsert();

    try {
      //this.dal.startTransaction();
      this.uof.startTransaction();

      //userDto = this.userDao.create(user);
      this.uof.insert(userDto);
      
      //this.dal.commitTransaction();
      this.uof.commitTransaction();
      
      userDto = (UserDto) this.uof.getInsertedObject();
      
      Logger.log("UserUccImpl", "subscribe", "The user " + user.getLogin() 
          + " was successfully registered", Logger.ERROR);

      return userDto;
    } catch (Exception exception) {
      //this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw new FatalException();
    }
  }

  /**
   * Get a User by his login.
   *
   * @param login of the User
   * @return {@link UserDto} that fits the good login
   */
  @Override
  public UserDto getUserByLogin(String login) {

    if (!DataValidator.validateString(login)) {
      throw new BusinessException(ErrorsCode.HTTP_ERROR_BAD_REQUEST);
    }

    try {

      this.dal.startTransaction();

      UserDto userDto = this.userDao.findByLogin(login);

      this.dal.commitTransaction();

      return userDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }

  }

}
