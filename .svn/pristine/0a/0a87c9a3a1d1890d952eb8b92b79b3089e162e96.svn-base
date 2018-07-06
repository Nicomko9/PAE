package biz;

import java.util.List;

import biz.dto.ContactDto;
import biz.exceptions.BusinessErrorException;
import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import biz.exceptions.FatalException;
import biz.exceptions.OptimisticLockException;
import biz.objects.Contact;
import dal.DalFrontEndServices;
import dal.dao.ContactDao;
import ihm.utils.DataValidator;

/**
 * Implementation of the Contact's Use Case Controller.
 */
class ContactUccImpl implements ContactUcc {

  private DalFrontEndServices dal;

  private ContactDao contactDao;
  
  private UnitOfWork uof;

  ContactUccImpl(DalFrontEndServices dal, ContactDao contactDao, UnitOfWork uof) {
    this.dal = dal;
    this.contactDao = contactDao;
    this.uof = uof;
  }

  /**
   * Persists a contact.
   *
   * @param contactDto to persists.
   * @return the contactDto with the information added in the DataBase
   */
  @Override
  public ContactDto createNewContact(ContactDto contactDto) {
    if (!DataValidator.validateString(contactDto.getFirstname())
        || !DataValidator.validateString(contactDto.getLastname())
        || !DataValidator.validateString(contactDto.getCompany().getCompanyName())) {
      //        || ((contactDto.getEmail() != null || !contactDto.getEmail().equals("")) 
      //            && !DataValidator.validateEmail(contactDto.getEmail()))) {
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_CONTACT);
    }
    
    Contact contact = (Contact) contactDto;
    contact.prepareForInsert();

    if (null != this.contactDao.findByEmail(contactDto.getEmail())) {
      throw new BusinessErrorException(ErrorsCode.BUSINESS_EMAIL_CONTACT_EXISTED);
    }

    try {
      //this.dal.startTransaction();
      this.uof.startTransaction();

      //contactDto = this.contactDao.create(contact);
      this.uof.insert(contact);

      //this.dal.commitTransaction();
      this.uof.commitTransaction();

      contactDto = (ContactDto) this.uof.getInsertedObject();
      
      return contactDto;
    } catch (Exception exception) {
      //this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw new BusinessException(ErrorsCode.UNDEFINED_ERROR);
    }
  }

  /**
   * Get All the contacts.
   *
   * @return {@link List} of {@link ContactDto}
   * @throws IllegalArgumentException if there's an error with the query
   */
  @Override
  public List<ContactDto> getAllContact() {
    try {

      this.dal.startTransaction();

      List<ContactDto> contactsDto = this.contactDao.findAllContacts();

      this.dal.commitTransaction();

      return contactsDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }

  /**
   * Get All the contacts for the given company name.
   *
   * @return {@link List} of {@link ContactDto}
   * @throws IllegalArgumentException if there's an error with the query
   */
  @Override
  public List<ContactDto> getContactsForCompany(String name) {

    if (!DataValidator.validateString(name)) {
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_COMPANY_NAME);
    }

    try {

      this.dal.startTransaction();

      List<ContactDto> contactsDto =  this.contactDao.findContactsForCompany(name);

      this.dal.commitTransaction();

      return contactsDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }

  /**
   * Get All the contacts for the given company name.
   *
   * @return {@link List} of {@link ContactDto}
   * @throws IllegalArgumentException if there's an error with the query
   */
  @Override
  public List<ContactDto> getContactsForCompany(int pk) {

    if (!DataValidator.validateInt(pk)) {
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_COMPANY_NAME);
    }

    try {

      this.dal.startTransaction();

      List<ContactDto> contactsDto =  this.contactDao.findContactsForCompany(pk);

      this.dal.commitTransaction();

      return contactsDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }

  /**
   * Update a contact already present in the database.
   *
   * @param contactDto {@link ContactDto} to persist
   * @return the information of the contact updated in the database
   */
  @Override
  public ContactDto update(ContactDto contactDto) {

    Contact contact = (Contact) contactDto;

    if (!DataValidator.validateString(contact.getFirstname())
        || !DataValidator.validateString(contact.getLastname())) {
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_CONTACT);
    }

    ContactDto cont = this.contactDao.findByEmail(contactDto.getEmail());
    if (cont != null && !contactDto.equals(cont)) {
      throw new BusinessErrorException(ErrorsCode.BUSINESS_EMAIL_CONTACT_EXISTED);
    }

    try {
      //this.dal.startTransaction();
      this.uof.startTransaction();

      //contactDto = this.contactDao.update(contact);
      this.uof.update(contact);

      //this.dal.commitTransaction();
      this.uof.commitTransaction();

      contactDto = (ContactDto) this.uof.getUpdatedObject();
      
      return contactDto;
    } catch (OptimisticLockException exception) {
      //this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw exception;
    } catch (Exception exception) {
      //this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw new FatalException();
    }
  }

  /**
   * Give a {@link ContactDto} based on her firstname, her lastname and her company's name.
   *
   * @param firstname the contact's firstname to match
   * @param lastname the contact's lastname to match
   * @param companyName the contact's company's name to match
   * @return the {@link ContactDto} found whith the information
   */
  @Override
  public ContactDto getContact(String firstname, String lastname, String companyName) {
    try {

      this.dal.startTransaction();

      ContactDto contactDto = this.contactDao.findContact(firstname, lastname, companyName);

      this.dal.commitTransaction();

      return contactDto;
    } catch (IllegalArgumentException exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }
  
  @Override
  public ContactDto getContactByPk(int pk) {
    try {

      this.dal.startTransaction();

      ContactDto contactDto = this.contactDao.findByPk(pk);

      this.dal.commitTransaction();

      return contactDto;
    } catch (IllegalArgumentException exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }
  
}
