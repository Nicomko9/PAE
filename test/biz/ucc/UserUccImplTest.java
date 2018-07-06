package biz.ucc;

import static org.junit.Assert.*;
import static util.Util.*;

import org.junit.*;

import biz.UserUcc;
import biz.exceptions.BusinessException;
import biz.objects.BusinessFactory;
import biz.objects.User;
import config.AppConfig;
import mock.DataBaseMock;

public class UserUccImplTest {

  private static AppConfig appConfig;
  private static UserUcc userUccImpl;
  private static User user; 

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    appConfig = new AppConfig(AppConfig.APP_MOCK_DAO);
    if(!appConfig.load())
      throw new Exception();
    userUccImpl =  (UserUcc) appConfig.newInstanceOf(UserUcc.class);

    DataBaseMock dataBaseMock = appConfig.newInstanceOf(DataBaseMock.class);
    user = (User) dataBaseMock.getUserByPk(1);
  }

  @Test (expected = BusinessException.class)
  public void testConnect1() {
    assertNull(userUccImpl.connect(null, PASSWORD));
  }

  @Test (expected = BusinessException.class)
  public void testConnect2() {
    assertNull(userUccImpl.connect(LOGIN, null));
  }

  @Test
  public void testConnect3() {
    assertNotNull(userUccImpl.connect("zak", "motdepasse"));
  }

  @Test (expected = BusinessException.class)
  public void testSubscribe1() {
    assertNull(userUccImpl.subscribe(BusinessFactory.getUser()));
  }

  @Test (expected = BusinessException.class)
  public void testSubscribe2() {
    assertNull(userUccImpl.subscribe(user));
  }

  @Test (expected = BusinessException.class)
  public void testSubscribe3() {
    user.setLogin("test");
    assertNull(userUccImpl.subscribe(user));
  }

  @Test (expected = BusinessException.class)
  public void testSubscribe4() {
    user.setLogin("zak");
    user.setEmail("test@pre.be");
    assertNull(userUccImpl.subscribe(user));
  }

  @Test
  public void testSubscribe5() {
    user.setLogin("test_junit");
    user.setEmail("test@pre.be");
    assertNotNull(userUccImpl.subscribe(user));
  }

  @Test
  public void testGetUserByLogin1() {
    assertEquals("zak",userUccImpl.getUserByLogin("zak").getLogin());
  }

  @Test
  public void testGetUserByLogin2() {
    assertNull(userUccImpl.getUserByLogin("dhiz"));
  }

}
