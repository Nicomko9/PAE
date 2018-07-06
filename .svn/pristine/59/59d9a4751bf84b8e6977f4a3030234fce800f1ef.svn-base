package ihm;

import biz.DtoFactory;
import biz.dto.UserDto;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import config.AppConfig;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User's session utility.
 */

class SessionManager {

  /**
   * Cookie's and Session id.
   */
  private static final String USER_SESSION_ID = "jwtUser";

  /**
   * Business object's factory.
   */
  private DtoFactory dtoFactory;

  /**
   * {@link JWTSigner} configured with the secret key.
   */
  private JWTSigner jwtSigner;

  /**
   * {@link JWTVerifier} configured with the secret key.
   */
  private JWTVerifier jwtVerifier;

  private int cookieMaxAge;

  /**
   * Construct the Manager.
   *
   * @param dtoFactory initialize local variable
   */
  SessionManager(DtoFactory dtoFactory, AppConfig config) {
    this.dtoFactory = dtoFactory;

    this.jwtSigner = new JWTSigner(config.getProperty("jwt.secret"));
    this.jwtVerifier = new JWTVerifier(config.getProperty("jwt.secret"));

    this.cookieMaxAge = Integer.parseInt(config.getProperty("cookie.max-age", "6480000"));
  }

  /**
   * Delete sessions and cookies.
   *
   * @param request request object
   * @param response response object
   */
  void deleteSession(HttpServletRequest request, HttpServletResponse response) {
    request.getSession().removeAttribute(USER_SESSION_ID);

    Cookie cookie = new Cookie(USER_SESSION_ID, null);
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

  /**
   * Create a session and add a cookie to the response.
   *
   * @param req request object
   * @param resp response object
   * @param userDto User's informations that are storage in both session and cookie
   * @return {@link UserDto} added to the session and the cookie.
   */
  UserDto createSession(HttpServletRequest req, HttpServletResponse resp, UserDto userDto) {
    Map<String, Object> map = new HashMap<>();

    map.put("login", userDto.getLogin());
    map.put("lastname", userDto.getLastname());
    map.put("firstname", userDto.getFirstname());
    map.put("email", userDto.getEmail());
    map.put("responsible", userDto.isResponsible() ? "1" : "0");

    String signer = jwtSigner.sign(map);

    req.getSession().setAttribute(USER_SESSION_ID, userDto);

    Cookie cookie = new Cookie(USER_SESSION_ID, signer);
    cookie.setComment("JWT cookie container");
    cookie.setMaxAge(this.cookieMaxAge);
    resp.addCookie(cookie);

    return userDto;
  }

  /**
   * Find a connected user in the session or in the cookie.
   *
   * @param req request object
   * @return {@link UserDto} found or <code>null</code>
   */
  UserDto readSession(HttpServletRequest req) {
    try {
      try {
        UserDto user;

        user = (UserDto) req.getSession().getAttribute(USER_SESSION_ID);

        if (null != user) {
          return user;
        }
      } catch (ClassCastException clce) {
        /* continue */
      }
      Cookie[] cookies = req.getCookies();
      if (req.getCookies() == null) {
        return null;
      }

      String token = null;
      for (Cookie c : cookies) {
        if (c.getName().equals(USER_SESSION_ID)) {
          token = c.getValue();

          break;
        }
      }

      if (token == null) {
        return null;
      }

      Map<String, Object> decodedPayload = jwtVerifier.verify(token);

      UserDto user = this.dtoFactory.getUser();

      user.setLogin((String) decodedPayload.get("login"));
      user.setFirstname((String) decodedPayload.get("firstname"));
      user.setLastname((String) decodedPayload.get("lastname"));
      user.setEmail((String) decodedPayload.get("email"));
      user.setResponsible(decodedPayload.get("responsible").equals("1"));

      req.getSession().setAttribute(USER_SESSION_ID, user);

      return user;
    } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException
        | SignatureException | IOException | JWTVerifyException exc) {
      exc.printStackTrace();

      return null;
    }
  }

}
