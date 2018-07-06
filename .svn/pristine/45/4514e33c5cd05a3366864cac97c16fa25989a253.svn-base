package biz.objects;

import static util.Util.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import org.junit.*;

import biz.exceptions.BusinessException;

public class ParticipationBizTest {

  private Participation participation1, participation2;
  private Company company1, company2;
  private Address address;
  private User creator;
  private Je je;
  
  @Before
  public void setUp() throws Exception {
    participation1 = BusinessFactory.getParticipation();
    participation2 = BusinessFactory.getParticipation();
    company1 = BusinessFactory.getCompany();
    company2 = BusinessFactory.getCompany();
    address = BusinessFactory.getAddress();
    creator = BusinessFactory.getUser();
    je = BusinessFactory.getJe();
    fillAddress(address, 12, "commune", 500, "rue", 13, 1000);
    fillUser(creator, "email@test.be", "zak", "lam", "zak", "password", 500);
    fillCompany(company1, address, "comp 1", creator, LocalDateTime.now(), 500);
    fillCompany(company2, address, "comp 1", creator, LocalDateTime.now(), 501);
    je.setDate(LocalDate.now());
    fillParticipation(participation1, company1, je, "facturée", "payée");
    fillParticipation(participation2, company2, je, "facturée", "payée");
  }
  
  @Test
  public void testCheckConstraints1() {
    participation1.checkConstraints();
  }
  
  @Test (expected = BusinessException.class)
  public void testCheckConstraints2() {
    BusinessFactory.getParticipation().checkConstraints();
  }
  
  @Test
  public void testHashCode1() {
    assertNotEquals(participation1.hashCode(), participation2.hashCode());
  }
  
  @Test
  public void testHashCode2() {
    participation2.setCompany(company1);
    assertEquals(participation1.hashCode(), participation2.hashCode());
  }
  
  @Test
  public void testEquals1() {
    assertFalse(participation1.equals(participation2));
  }
  
  @Test
  public void testEquals2() {
    Participation participation = BusinessFactory.getParticipation();
    fillParticipation(participation, company1, je, "facturée", "payée");
    assertTrue(participation1.equals(participation));
  }
  
  @Test
  public void testConfirm1() {
    assertFalse(participation1.confirm());
  }
  
  @Test
  public void testConfirm2() {
    Participation participation = BusinessFactory.getParticipation();
    fillParticipation(participation, company1, je, null, "invitée");
    assertFalse(participation.confirm());
  }
  
  @Test
  public void testInvoice1() {
    assertFalse(participation1.invoice());
  }
  
  @Test
  public void testInvoice2() {
    Participation participation = BusinessFactory.getParticipation();
    fillParticipation(participation, company1, je, "invitée", "confirmée");
    assertFalse(participation.invoice());
  }

  @Test
  public void testPay1() {
    assertTrue(participation1.pay());
  }
  
  @Test
  public void testPay2() {
    Participation participation = BusinessFactory.getParticipation();
    fillParticipation(participation, company1, je, "confirmée", "facturée");
    assertFalse(participation.pay());
  }
  
  @Test
  public void testRefuse1() {
    assertFalse(participation1.refuse());
  }
  
  @Test
  public void testRefuse2() {
    Participation participation = BusinessFactory.getParticipation();
    fillParticipation(participation, company1, je, null, "invitée");
    assertFalse(participation.refuse());
  }
  
  @Test (expected = BusinessException.class)
  public void testCheckConstraint1() {
    BusinessFactory.getParticipation().checkConstraints();
  }
  
  @Test
  public void testCheckConstraint2() {
    participation1.checkConstraints();
  }
  
  @Test
  public void testClone() {
    assertEquals(participation1, participation1.clone());
    assertNotSame(participation1, participation1.clone());
  }
  
}