package biz.dto;

import biz.objects.Company;
import biz.objects.Contact;
import biz.objects.Je;
import com.owlike.genson.annotation.JsonIgnore;
import java.util.List;

public interface ParticipationDto extends AbstractDto {

  @Override
  @JsonIgnore
  int getPk();

  @Override
  @JsonIgnore
  void setPk(int primaryKey);

  boolean addContact(Contact contact);

  boolean removeContact(Contact contact);
  
  List<ContactDto> contacts();
  
  CompanyDto getCompany();

  void setCompany(Company company);

  JeDto getJe();

  void setJe(Je je);

  String getState();

  void setState(String state);

  String getLastState();

  int getLastStateIndex();

  int getStateIndex();

  void setLastState(String lastState);

}
