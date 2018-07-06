package biz;

import biz.dto.UserDto;


/**
 * User's Use Case Controller.
 */
public interface UserUcc {

  /**
   * Start a connection.
   *
   * @param login login of the user to connect
   * @param password password of the user to connect
   * @return the information of the user in the Database.
   */
  UserDto connect(String login, String password);

  /**
   * Register a new user into the Database.
   *
   * @param biz {@link UserDto}to persist
   * @return a userDto with the information added in database
   */
  UserDto subscribe(UserDto biz);

  /**
   * Get a User by his login.
   *
   * @param login of the User
   * @return {@link UserDto} that fits the good login
   */
  UserDto getUserByLogin(String login);

}
