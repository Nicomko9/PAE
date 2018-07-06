package dal.dao;


import biz.dto.UserDto;
import biz.objects.User;
import dal.dao.schema.UserSchema;

public interface UserDao extends UserSchema, GenericDao {

  /**
   * Persist a user into the table users in database.
   *
   * @param user the {@link User} to persist
   * @return the value of the {@link User} casted in {@link UserDto}
   */
  UserDto create(User user);

  /**
   * Find a single user based on a login.
   *
   * @param login the value to lookup
   * @return the {@link UserDto} instance found in the research
   */
  UserDto findByLogin(String login);

  /**
   * Find a single user based on an email.
   *
   * @param email the value to lookup
   * @return the {@link UserDto} instance found in the research
   */
  UserDto findByEmail(String email);

  /**
   * Find a single user based on a Primary Key.
   *
   * @param pk the primary key
   * @return the {@link User}
   */
  UserDto findByPk(int pk);

}
