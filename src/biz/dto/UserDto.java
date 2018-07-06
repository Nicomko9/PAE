package biz.dto;

import com.owlike.genson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User's Data Transfer Objects.
 */
public interface UserDto extends AbstractDto, Serializable {

  String getLogin();

  void setLogin(String login);

  @JsonIgnore
  String getPassword();

  @JsonIgnore
  void setPassword(String password);

  String getLastname();

  void setLastname(String lastname);

  String getFirstname();

  void setFirstname(String firstname);

  String getEmail();

  void setEmail(String email);

  LocalDateTime getInscriptionDate();

  void setInscriptionDate(LocalDateTime inscriptionDate);

  boolean isResponsible();

  void setResponsible(boolean responsible);

  String toString();

}
