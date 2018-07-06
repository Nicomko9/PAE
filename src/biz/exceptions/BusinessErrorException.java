package biz.exceptions;

/**
 * Business Error Exceptions.
 */
@SuppressWarnings("serial")
public class BusinessErrorException extends BusinessException {

  /**
   * Constructor.
   *
   * @param message message off the {@link Exception}
   */
  @Deprecated
  public BusinessErrorException(String message) {
    super(message);
  }

  public BusinessErrorException(int errorCode) {
    super(errorCode);
  }

}
