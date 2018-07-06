package biz.ucc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static util.Util.fillAddress;

import org.junit.BeforeClass;
import org.junit.Test;

import biz.AddressUcc;
import biz.exceptions.BusinessException;
import biz.objects.Address;
import biz.objects.BusinessFactory;
import config.AppConfig;
import mock.DataBaseMock;

public class AddressUccImplTest {

  private static AddressUcc addressUcc;
  
  private static Address address;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AppConfig appConfig = new AppConfig(AppConfig.APP_MOCK_DAO);
    if(!appConfig.load())
      throw new Exception();
    addressUcc = (AddressUcc) appConfig.newInstanceOf(AddressUcc.class);
    
    DataBaseMock dataBaseMock = appConfig.newInstanceOf(DataBaseMock.class);
    address = (Address) dataBaseMock.getAddressByPk(1);
  }
  
  @Test (expected = BusinessException.class)
  public void testRegister1() {
    assertNull(addressUcc.register(BusinessFactory.getAddress()));
  }
  
  @Test
  public void testRegister2() {
    fillAddress(address, 12, "Jette", 1, "rue de la famine", 112, 1090);
    assertNotNull(addressUcc.register(address));
  }

  @Test
  public void testGetAddress1() {
    assertNull("Address doesn't exist", addressUcc.getAddress("rue", 1, 1, "commune"));
  }
  
  @Test
  public void testGetAddress2() {
    assertEquals("Molenbeek", addressUcc.getAddress("rue de la prospérité", 112, 1080, "Molenbeek").getCommune());
  }
  
  @Test
  public void testGetByPk1() {
    assertNotNull(addressUcc.getByPk(1));
  }
  
  @Test
  public void testGetByPk2() {
    assertNull(addressUcc.getByPk(500));
  }
  
  @Test
  public void testUpdate() {
    address.setCommune("nouvelle commune");
    assertNotNull(addressUcc.update(address));
  }
  
}
