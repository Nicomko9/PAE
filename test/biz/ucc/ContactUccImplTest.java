package biz.ucc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static util.Util.fillContact;

import org.junit.BeforeClass;
import org.junit.Test;

import biz.ContactUcc;
import biz.exceptions.BusinessException;
import biz.objects.BusinessFactory;
import biz.objects.Company;
import biz.objects.Contact;
import config.AppConfig;
import mock.DataBaseMock;

public class ContactUccImplTest {

  private static ContactUcc contactUcc;
  private static Contact contact;
  private static Company company;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AppConfig appConfig = new AppConfig(AppConfig.APP_MOCK_DAO);
    if(!appConfig.load())
      throw new Exception();
    contactUcc = (ContactUcc) appConfig.newInstanceOf(ContactUcc.class);
    
    DataBaseMock dataBaseMock = appConfig.newInstanceOf(DataBaseMock.class);
    
    contact = (Contact) dataBaseMock.getContactDtoByPk(1);
    company = (Company) contact.getCompany();
  }
  
  @Test (expected = BusinessException.class)
  public void testCreateNewContact1() {
    assertNull(contactUcc.createNewContact(contact));
  }
  
  @Test
  public void testCreateNewContact2() {
    contact.setEmail("");
    assertNotNull(contactUcc.createNewContact(contact));
  }
  
  @Test
  public void testCreateNewContact3() {
    Contact contact = BusinessFactory.getContact();
    fillContact(contact, company, "test.test@test.be", "fir", "last", "1234", 1);
    assertNotNull(contactUcc.createNewContact(contact));    
   }
  
  @Test
  public void testGetContactsForCompany1(){
    assertFalse(contactUcc.getContactsForCompany("Apple").isEmpty());
  }
  
  @Test
  public void testGetContactsForCompany2(){
    assertTrue(contactUcc.getContactsForCompany("Adbhb").isEmpty());
  }
  
  @Test
  public void testGetContactsForCompany3(){
    assertFalse(contactUcc.getContactsForCompany(1).isEmpty());
  }
  
  @Test
  public void testGetContactsForCompany4(){
    assertTrue(contactUcc.getContactsForCompany(500).isEmpty());
  }
  
  @Test
  public void testUpdate1(){
    assertNotNull(contactUcc.update(contact));
  }
  
  @Test (expected = BusinessException.class)
  public void testUpdate2(){
    Contact contact = BusinessFactory.getContact();
    fillContact(contact, company, "test.test@test.be", "fir", "last", "1234", 67);
    assertNull(contactUcc.update(contact));
  }
  
  @Test
  public void testGetAllContact(){
    assertFalse(contactUcc.getAllContact().isEmpty());
  }
  
  @Test
  public void testGetContact1() {
    assertNotNull(contactUcc.getContact("Richard", "Watterson", "Apple"));
  }
  
  @Test
  public void testGetContact2() {
    assertNull(contactUcc.getContact("Richard", "Watterson", "Microsoft"));
  }
  
  @Test
  public void testGetContactByPk1() {
    assertNotNull(contactUcc.getContactByPk(1));
  }
  
  @Test
  public void testGetContactByPk2() {
    assertNull(contactUcc.getContactByPk(500));
  }

}
