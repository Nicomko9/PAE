package biz.dto;

import biz.objects.Company;
import biz.objects.Contact;
import biz.objects.Participation;
import com.owlike.genson.annotation.JsonIgnore;

public interface PresenceDto extends AbstractDto {

  @Override
  @JsonIgnore
  int getPk();

  @Override
  @JsonIgnore
  void setPk(int primaryKey);

  ParticipationDto getParticipation();

  void setParticipation(Participation participation);

  CompanyDto getCompany();

  void setCompany(Company company);

  ContactDto getContact();

  void setContact(Contact contact);

  boolean isActif();

  void setActif(boolean actif);


}
