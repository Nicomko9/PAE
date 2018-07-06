package biz.objects;

import static org.junit.Assert.*;
import static util.Util.*;

import org.junit.Before;
import org.junit.Test;

import biz.exceptions.BusinessException;

public class AddressBizTest {

  private Address address1, address2;
  
  @Before
  public void setUp() throws Exception {
    address1 = BusinessFactory.getAddress();
    fillAddress(address1, 12, "commune", 500, "rue", 12, 1000);
    address2 = BusinessFactory.getAddress();
    fillAddress(address2, 12, "commune", 501, "rue", 12, 1000);
  }

  @Test
  public void testHashCode1() {
    assertNotEquals(address1.hashCode(), address2.hashCode());
  }
  
  @Test
  public void testHashCode2() {
    address2.setPk(500);
    assertEquals(address1.hashCode(), address2.hashCode());
  }

  @Test
  public void testEquals1() {
    assertFalse(address1.equals(address2));
  }
  
  @Test
  public void testEquals2() {
    address2.setPk(500);
    assertTrue(address1.equals(address2));
  }
  
  @Test (expected = BusinessException.class)
  public void testCheckConstraint1() {
    BusinessFactory.getAddress().checkConstraints();
  }
  
  @Test
  public void testCheckConstraint2() {
    address1.checkConstraints();
  }
  
  @Test
  public void testClone() {
    assertEquals(address1, address1.clone());
    assertNotSame(address1, address1.clone());
  }
  
  @Test
  public void testGetStreet() {
    assertEquals("rue", address1.getStreet());
  }
  
  @Test
  public void testSetStreet() {
    address1.setStreet("nouvelle rue");
    assertEquals("nouvelle rue", address1.getStreet());
  }
  
  @Test
  public void testGetStreetNumber() {
    assertEquals(12, address1.getStreetNumber());
  }
  
  @Test
  public void testSetStreetNumber() {
    address1.setStreetNumber(11);
    assertEquals(11, address1.getStreetNumber());
  }
  
  @Test
  public void testGetBox() {
    assertEquals(12, address1.getBox());
  }
  
  @Test
  public void testSetBox() {
    address1.setBox(9);
    assertEquals(9, address1.getBox());
  }
  
  @Test
  public void testGetZipCode() {
    assertEquals(1000, address1.getZipCode());
  }
  
  @Test
  public void testSetZipCode() {
    address1.setZipCode(1234);
    assertEquals(1234, address1.getZipCode());
  }
  
  @Test
  public void testGetCommune() {
    assertEquals("commune", address1.getCommune());
  }
  
  @Test
  public void testSetCommune() {
    address1.setCommune("nouvelle commune");
    assertEquals("nouvelle commune", address1.getCommune());
  }

}
