package biz.dto;

import biz.exceptions.BusinessException;
import biz.objects.Company;
import biz.objects.Presence;
import java.util.List;

/**
 * Contact's Data Transfer Object.
 */
public interface ContactDto extends AbstractDto {

  boolean addPresence(Presence presence);

  boolean addPresence(Presence presence, boolean check);

  boolean removePresence(Presence presence);
  
  List<PresenceDto> presences();
  
  CompanyDto getCompany();

  void setCompany(Company company);

  String getLastname();

  void setLastname(String lastname);

  String getFirstname();

  void setFirstname(String firstname);

  String getEmail();

  void setEmail(String email);

  String getPhone();

  void setPhone(String phone);

  boolean isActive();

  void setActive(boolean active);

  void prepareForInsert() throws BusinessException;

}
