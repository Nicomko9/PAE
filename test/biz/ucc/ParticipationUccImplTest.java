package biz.ucc;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Test;

import biz.ParticipationUcc;
import biz.exceptions.BusinessException;
import biz.objects.Company;
import biz.objects.Je;
import biz.objects.Participation;
import config.AppConfig;
import mock.DataBaseMock;

public class ParticipationUccImplTest {

  private static AppConfig appConfig;
  private static ParticipationUcc participationUcc;
  private static Participation participation;
  private static Je je;
  private static Company company; 
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    appConfig = new AppConfig(AppConfig.APP_MOCK_DAO);
    if(!appConfig.load())
      throw new Exception();
    participationUcc = appConfig.newInstanceOf(ParticipationUcc.class);
    
    DataBaseMock dataBaseMock = appConfig.newInstanceOf(DataBaseMock.class);
    participation = (Participation) dataBaseMock.getParticipationByYear(LocalDate.now()
        .minusYears(1).getYear()).get(0);
    company = (Company) participation.getCompany();
    je = (Je) participation.getJe();
  }

  @Test (expected = BusinessException.class)
  public void testInvite() {
    assertNull(participationUcc.invite(participation));
  }

  @Test (expected = BusinessException.class)
  public void testConfirm() {
    assertNull(participationUcc.confirm(participation));
  }

  @Test (expected = BusinessException.class)
  public void testRefuse() {
    assertNull(participationUcc.refuse(participation));
  }

  @Test (expected = BusinessException.class)
  public void testInvoice() {
    assertNull(participationUcc.invoice(participation));
  }

  @Test (expected = BusinessException.class)
  public void testPay() {
    assertNull(participationUcc.pay(participation));
  }

  @Test
  public void testCancel() {
    assertNotNull(participationUcc.cancel(participation));
  }

  @Test
  public void testGetAllParticipationsOfJeDto() {
    assertFalse(participationUcc.getAllParticipationsOf(je).isEmpty());
  }

  @Test
  public void testGetAllParticipationsOfCompanyDto() {
    assertFalse(participationUcc.getAllParticipationsOf(company).isEmpty());
  }

}
