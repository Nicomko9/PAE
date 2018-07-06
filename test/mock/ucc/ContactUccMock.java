package mock.ucc;

import biz.ContactUcc;
import biz.dto.ContactDto;
import biz.objects.Contact;
import dal.dao.ContactDao;
import java.util.List;

class ContactUccMock implements ContactUcc {

  private ContactDao contactDao;

  ContactUccMock(ContactDao contactDao) {
    this.contactDao = contactDao;
  }

  @Override
  public ContactDto createNewContact(ContactDto contactDto) {
    try {
      return contactDao.create((Contact) contactDto);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<ContactDto> getAllContact() {
    return contactDao.findAllContacts();
  }

  @Override
  public List<ContactDto> getContactsForCompany(String companyName) {
    return contactDao.findContactsForCompany(companyName);
  }

  @Override
  public List<ContactDto> getContactsForCompany(int companyPk) {
    return contactDao.findContactsForCompany(companyPk);
  }
  
  @Override
  public ContactDto getContactByPk(int pk) {
    return contactDao.findByPk(pk);
  }

  @Override
  public ContactDto update(ContactDto biz) {
    return contactDao.update((Contact) biz);
  }

  @Override
  public ContactDto getContact(String firstname, String lastname, String companyName) {
    return contactDao.findContact(firstname, lastname, companyName);
  }

}
