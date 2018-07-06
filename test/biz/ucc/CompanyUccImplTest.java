package biz.ucc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static util.Util.fillCompany;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import biz.CompanyUcc;
import biz.exceptions.BusinessException;
import biz.objects.Address;
import biz.objects.Company;
import biz.objects.User;
import config.AppConfig;
import mock.DataBaseMock;

public class CompanyUccImplTest {

  private static CompanyUcc companyUcc;
  private static Company company;
  private static Address address;
  private static User creator;
  private static int pkCompany;
  private static LocalDateTime inscriptionDate;
  private static String companyName;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AppConfig appConfig = new AppConfig(AppConfig.APP_MOCK_DAO);
    if(!appConfig.load())
      throw new Exception();
    companyUcc = (CompanyUcc) appConfig.newInstanceOf(CompanyUcc.class);

    DataBaseMock dataBaseMock = appConfig.newInstanceOf(DataBaseMock.class);
    company = (Company) dataBaseMock.getCompanyByPk(1);
    companyName = company.getCompanyName();
    inscriptionDate = company.getInscriptionDate();
    address = (Address) company.getAddress();
    creator = (User) company.getCreator();
    pkCompany = company.getPk();
  }

  @Before
  public void setUp() throws Exception {
    fillCompany(company, address, companyName, creator, inscriptionDate, pkCompany);
  }

  @Test (expected = BusinessException.class)
  public void testRegister1() {
    assertNull(companyUcc.register(company));
  }

  @Test (expected = BusinessException.class)
  public void testRegister2() {
    company.setCompanyName("");
    assertNull(companyUcc.register(company));
  }

  @Test (expected = BusinessException.class)
  public void testRegister3() {
    company.setAddress(null);
    assertNull(companyUcc.register(company));
  }

  @Test
  public void testRegister4() {
    company.setCompanyName("name_junit");
    assertNotNull(companyUcc.register(company));
  }

  @Test
  public void testUpdate1() {
    assertNotNull(companyUcc.update(company));
  }
  
  @Test (expected = BusinessException.class)
  public void testUpdate2() {
    company.setCompanyName("Mainsys");
    assertNotNull(companyUcc.update(company));
  }

  @Test
  public void testGetAllCompanies() {
    assertFalse(companyUcc.getAllCompanies().isEmpty());
  }

  @Test
  public void testGetCompanyByName1() {
    assertNull("company doesn' exist",companyUcc.getCompanyByName("andiaef"));
  }

  @Test
  public void testGetCompanyByName2() {
    assertEquals("Microsoft",companyUcc.getCompanyByName("Microsoft").getCompanyName());
  }
  
  @Test
  public void testGetCompanyByPk1() {
    assertNull(companyUcc.getCompanyByPk(500));
  }
  
  @Test
  public void testGetCompanyByPk2() {
    assertNotNull(companyUcc.getCompanyByPk(2));
  }
  
  @Test
  public void testGetCompaniesForSelection() {
    assertFalse(companyUcc.getAllCompaniesForTheSelection().isEmpty());
  }

}
