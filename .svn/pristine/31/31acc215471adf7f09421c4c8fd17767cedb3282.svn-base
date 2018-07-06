package biz.ucc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Test;

import biz.JeUcc;
import biz.objects.BusinessFactory;
import biz.objects.Je;
import config.AppConfig;

public class JeUccImplTest {

  private static AppConfig appConfig;
  private static JeUcc jeUcc;
  private static Je je;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    appConfig = new AppConfig(AppConfig.APP_MOCK_DAO);
    if(!appConfig.load())
      throw new Exception();
    jeUcc = (JeUcc) appConfig.newInstanceOf(JeUcc.class);
    
    je = BusinessFactory.getJe();
    je.setDate(LocalDate.now());
  }
  
  @Test
  public void testCreateNewJe() {
    assertNotNull(jeUcc.createNewJe(je));
  }

  @Test
  public void testGetAllJe() {
    assertFalse(jeUcc.getAllJe().isEmpty());
  }

  @Test
  public void testGetPreviousJe() {
    assertFalse(jeUcc.getPreviousJe().isEmpty());
  }

  @Test
  public void testGetJeByYear() {
    Je je = BusinessFactory.getJe();
    je.setDate(LocalDate.now().minusYears(1));
    assertEquals(je, (Je) jeUcc.getJeByYear(je.getDayYear()));
  }

}
