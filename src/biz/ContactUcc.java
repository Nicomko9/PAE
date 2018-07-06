package biz;

import biz.dto.ContactDto;
import java.util.List;

/**
 * Contact's Use Case Controller.
 */
public interface ContactUcc {

  /**
   * Persists a contact.
   *
   * @param contactDto {@link contactDto} to persist
   * @return the contactDto with the information added in the DataBase
   */
  ContactDto createNewContact(ContactDto contactDto);

  /**
   * Get All the contacts.
   *
   * @return {@link List} of {@link ContactDto}
   */
  List<ContactDto> getAllContact();

  /**
   * Get All the contacts for the given company name.
   *
   * @return {@link List} of {@link ContactDto}
   */
  List<ContactDto> getContactsForCompany(String name);

  /**
   * Get All the contacts for the given company id.
   *
   * @return {@link List} of {@link ContactDto}
   */
  List<ContactDto> getContactsForCompany(int id);
  
  /**
   * Get the contacts by pk.
   *
   * @return contactDto {@link ContactDto}
   */
  ContactDto getContactByPk(int pk);

  /**
   * Update a contact already present in the database.
   *
   * @param contact {@link ContactDto} to change
   * @return {@link ContactDto} that has been updated in the database
   */
  ContactDto update(ContactDto contact);

  /**
   * Give a {@link ContactDto} based on her firstname, her lastname and her company's name.
   *
   * @param firstname the contact's firstname to match
   * @param lastname the contact's lastname to match
   * @param companyName the contact's company's name to match
   * @return the {@link ContactDto} found whith the information
   */
  ContactDto getContact(String firstname, String lastname, String companyName);
  

}
