package dal.dao;

import biz.dto.ContactDto;
import biz.objects.Contact;
import dal.dao.schema.ContactSchema;
import java.util.List;
import java.util.Set;

public interface ContactDao extends ContactSchema, GenericDao {

  /**
   * Persist a contact into the table contacts in database.
   *
   * @param contact the {@link Contact} to persist
   * @return the {@link ContactDto} persisted
   */
  ContactDto create(Contact contact);

  /**
   * Update a contact already present in the database.
   *
   * @param contact {@link Contact} to persist
   * @return the new value of the {@link Contact} casted in {@link ContactDto}
   */
  ContactDto update(Contact contact);

  /**
   * Find a single contact based on his pk.
   *
   * @param pk the value to lookup
   * @return the {@link ContactDto} instance found in the research
   */
  ContactDto findByPk(int pk);

  /**
   * Find a single contact based on his email.
   *
   * @param email the value to lookup
   * @return the {@link ContactDto} instance found in the research
   */
  ContactDto findByEmail(String email);

  /**
   * Find a contact based on his firstname, lastname and company.
   *
   * @param firstname the firstname to match
   * @param lastname the lastname to match
   * @param companyName the contact's company's name to match
   * @return the {@link ContactDto} instance found in the research
   */
  ContactDto findContact(String firstname, String lastname, String companyName);

  /**
   * Get All the contacts for the given company name.
   *
   * @return {@link List} of {@link ContactDto}
   * @throws IllegalArgumentException if there's an error with the query
   */
  List<ContactDto> findContactsForCompany(String companyName);

  /**
   * Get All the contacts for the given company name.
   *
   * @return {@link List} of {@link ContactDto}
   * @throws IllegalArgumentException if there's an error with the query
   */
  List<ContactDto> findContactsForCompany(int companyId);

  /**
   * Get All the contacts.
   *
   * @return {@link List} of {@link ContactDto}
   * @throws IllegalArgumentException if there's an error with the query
   */
  List<ContactDto> findAllContacts();

  /**
   * Search for contact that meet that meet at least one of the criteria.
   *
   * @return a {@link Set} of {@link ContactDto}
   */
  Set<ContactDto> search(String... criteria);

}
