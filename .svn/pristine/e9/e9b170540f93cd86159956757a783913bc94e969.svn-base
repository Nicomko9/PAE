package mock.dao;

import biz.dto.AbstractDto;
import biz.dto.CompanyDto;
import biz.dto.ContactDto;
import biz.objects.Company;
import biz.objects.Contact;
import dal.dao.ContactDao;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mock.DataBaseMock;

public class ContactDaoMock implements ContactDao {

  private DataBaseMock dataBaseMock;

  ContactDaoMock(DataBaseMock dataBaseMock) {
    this.dataBaseMock = dataBaseMock;
  }

  @Override
  public ContactDto create(Contact contact) {
    return dataBaseMock.addContact(contact);
  }

  @Override
  public AbstractDto insert(AbstractDto abstractDto) {
    return create((Contact) abstractDto);
  }
  
  @Override
  public ContactDto findByPk(int pk) {
    return dataBaseMock.getContactDtoByPk(pk);
  }

  @Override
  public ContactDto findByEmail(String email) {
    return dataBaseMock.getContactByEmail(email);
  }

  @Override
  public ContactDto findContact(String firstname, String lastname, String companyName) {
    CompanyDto company = dataBaseMock.getCompanyByName(companyName);
    List<ContactDto> contactDtoByCompany = dataBaseMock.getContactsByCompany((Company)company);
    return contactDtoByCompany.stream().filter(c -> c.getFirstname().equals(firstname)
        && c.getLastname().equals(lastname)).findAny().orElse(null);
  }

  @Override
  public List<ContactDto> findContactsForCompany(String companyName)  {
    CompanyDto company = dataBaseMock.getCompanyByName(companyName);
    return dataBaseMock.getContactsByCompany((Company) company);
  }

  @Override
  public List<ContactDto> findContactsForCompany(int companyPk)  {
    CompanyDto company = dataBaseMock.getCompanyByPk(companyPk);
    return dataBaseMock.getContactsByCompany((Company) company);
  }


  @Override
  public List<ContactDto> findAllContacts() {
    return dataBaseMock.getContacts();
  }

  @Override
  public ContactDto update(Contact contact) {
    return dataBaseMock.updateContact(contact);
  }
  
  @Override
  public AbstractDto update(AbstractDto abstractDto) {
    return update((Contact) abstractDto);
  }

  @Override
  public Set<ContactDto> search(String... criteria) {
    return new HashSet<>(dataBaseMock.getContacts());
  }

  @Override
  public void cacheClean() {}

}
