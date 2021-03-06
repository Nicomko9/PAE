package ihm.utils;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Validity utility.
 */
public class DataValidator {

  /**
   * Email regular expression.
   */
  private static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\."
      + "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

  /**
   * Validate an email.
   *
   * @param email String to validate as an email
   * @return <code>true</code> if email match the regEx otherwise return <code>false</code>
   */
  public static boolean validateEmail(String email) {
    return email != null && Pattern.matches(EMAIL_REGEX, email);
  }

  /**
   * Validate a {@link String}.
   *
   * @param str String to validate a {@link String}
   * @return <code>true</code> if str match the regEx otherwise return <code>false</code>
   */
  public static boolean validateString(String str) {
    return str != null && !str.equals("");
  }

  /**
   * Validate a {@link LocalDate}.
   *
   * @param date LocalDate to validate a {@link LocalDate}
   * @return <code>true</code> if date match the regEx otherwise return <code>false</code>
   */
  public static boolean validateDate(LocalDate date) {
    return date != null;
  }

  /**
   * Validate a {@link Integer}.
   *
   * @param integer Integer to validate a {@link Integer}
   * @return <code>true</code> if integer match the regEx otherwise return <code>false</code>
   */
  public static boolean validateInt(int integer) {
    return integer > 0;
  }

}
