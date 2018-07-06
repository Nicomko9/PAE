package biz;

import biz.dto.AddressDto;
import biz.dto.CompanyDto;
import biz.dto.ContactDto;
import biz.dto.JeDto;
import biz.dto.ParticipationDto;
import biz.dto.PresenceDto;
import biz.dto.UserDto;
import biz.objects.BusinessFactory;

/**
 * Implementation of the Data Transfer Object Factory.
 */
class DtoFactoryImpl implements DtoFactory {

  /**
   * Give an empty UserDto.
   *
   * @return empty {@link UserDto}
   */
  public UserDto getUser() {
    return BusinessFactory.getUser();
  }

  /**
   * Give an empty AddressDto.
   *
   * @return empty {@link AddressDto}
   */
  public AddressDto getAddress() {
    return BusinessFactory.getAddress();
  }

  /**
   * Give an empty CompanyDto.
   *
   * @return empty {@link CompanyDto}
   */
  public CompanyDto getCompany() {
    return BusinessFactory.getCompany();
  }

  /**
   * Give an empty ContactDto.
   *
   * @return empty {@link ContactDto}
   */
  public ContactDto getContact() {
    return BusinessFactory.getContact();
  }

  /**
   * Give an empty JeDto.
   *
   * @return empty {@link JeDto}
   */
  public JeDto getCompanyDay() {
    return BusinessFactory.getJe();
  }

  /**
   * Give an empty ParticipationDto.
   *
   * @return empty {@link ParticipationDto}
   */
  public ParticipationDto getParticipation() {
    return BusinessFactory.getParticipation();
  }

  /**
   * Give an empty PresenceDto.
   *
   * @return empty {@link PresenceDto}
   */
  public PresenceDto getPresence() {
    return BusinessFactory.getPresence();
  }

}
