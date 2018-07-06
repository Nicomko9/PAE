package biz.exceptions;

/**
 * Business Exception.
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {

  private int errorCode = ErrorsCode.UNDEFINED_ERROR;

  /**
   * Constructor.
   *
   * @param message message of the {@link Exception}
   * @deprecated since 18/03
   */
  @Deprecated
  public BusinessException(String message) {
    super(message);
  }

  /**
   * Constructor.
   *
   * @param errorCode the code error of the {@link Exception}
   */
  public BusinessException(int errorCode) {
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }

}
