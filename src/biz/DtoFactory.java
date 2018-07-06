package biz;

import biz.dto.AddressDto;
import biz.dto.CompanyDto;
import biz.dto.ContactDto;
import biz.dto.JeDto;
import biz.dto.ParticipationDto;
import biz.dto.PresenceDto;
import biz.dto.UserDto;
import biz.objects.Je;
import biz.objects.Participation;
import biz.objects.Presence;

/**
 * Data Transfer Object Factory.
 */
public interface DtoFactory {

  /**
   * Give an empty User.
   *
   * @return empty {@link UserDto}
   */
  UserDto getUser();

  /**
   * Give an empty Address.
   *
   * @return empty {@link AddressDto}
   */
  AddressDto getAddress();

  /**
   * Give an empty Company.
   *
   * @return empty {@link CompanyDto}
   */
  CompanyDto getCompany();

  /**
   * Give an empty Contact.
   *
   * @return empty {@link ContactDto}
   */
  ContactDto getContact();

  /**
   * Give an empty Je.
   *
   * @return empty {@link Je}
   */
  JeDto getCompanyDay();

  /**
   * Give an empty Participation.
   *
   * @return empty {@link Participation}
   */
  ParticipationDto getParticipation();

  /**
   * Give an empty Presence.
   *
   * @return empty {@link Presence}
   */
  PresenceDto getPresence();

}
