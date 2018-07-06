package biz.objects;

import static org.junit.Assert.*;
import static util.Util.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import biz.exceptions.BusinessException;

public class ContactBizTest {

  private Contact contact1, contact2;
  private Company company;
  private Address address;
  private User creator;
  
  @Before
  public void setUp() throws Exception {
    contact1 = BusinessFactory.getContact();
    contact2 = BusinessFactory.getContact();
    company = BusinessFactory.getCompany();
    address = BusinessFactory.getAddress();
    creator = BusinessFactory.getUser();
    fillAddress(address, 12, "commune 1", 500, "rue", 12, 1000);
    fillUser(creator, "email@test.be", "zakaria", "lamrini", "zak", "password", 500);
    fillCompany(company, address, "company 1", creator, LocalDateTime.now(), 500);
    fillContact(contact1, company, "email@test.be", "prenom", "nom", "phone", 500);
    fillContact(contact2, company, "email@test.be", "zakaria", "lamrini", "phone", 501);
  }
  
  @Test
  public void testGetCompany1() {
    assertNotSame(contact1.getCompany(), company);
  }
  
  @Test
  public void testGetLastName() {
    assertEquals("nom", contact1.getLastname());
  }
  
  @Test
  public void testSetLastName() {
    contact1.setLastname("last");
    assertEquals("last", contact1.getLastname());
  }
  
  @Test
  public void testGetFirstName() {
    assertEquals("prenom", contact1.getFirstname());
  }
  
  @Test
  public void testSetFirstName() {
    contact1.setFirstname("pre");
    assertEquals("pre", contact1.getFirstname());
  }

  @Test
  public void testGetEmail() {
    assertEquals("email@test.be", contact1.getEmail());
  }
  
  @Test
  public void testSetEmail() {
    contact1.setEmail("email@test.fr");
    assertEquals("email@test.fr", contact1.getEmail());
  }
  
  @Test
  public void testGetPhone() {
    assertEquals("phone", contact1.getPhone());
  }
  
  @Test
  public void testSetPhone() {
    contact1.setPhone("0123456789");
    assertEquals("0123456789", contact1.getPhone());
  }
  
  @Test
  public void testHashCode1() {
    assertNotEquals(contact1.hashCode(), contact2.hashCode());
  }
  
  @Test
  public void testHashCode2() {
    contact2.setPk(500);
    assertEquals(contact1.hashCode(), contact2.hashCode());
  }

  @Test
  public void testEquals1() {
    assertFalse(contact1.equals(contact2));
  }
  
  @Test
  public void testEquals2() {
    contact2.setPk(500);
    assertTrue(contact1.equals(contact2));
  }

  @Test
  public void testGetCompany() {
    company.setCompanyName("nouvelle");
    assertEquals("company 1", contact1.getCompany().getCompanyName());
  }
  
  @Test (expected = BusinessException.class)
  public void testCheckConstraint1() {
    BusinessFactory.getContact().checkConstraints();
  }
  
  @Test
  public void testCheckConstraint2() {
    contact1.checkConstraints();
  }
  
  @Test
  public void testClone() {
    assertEquals(contact1, contact1.clone());
    assertNotSame(contact1, contact1.clone());
  }
  
}
