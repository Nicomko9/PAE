package biz.exceptions;

/**
 * Business Exception.
 */
@SuppressWarnings("serial")
public class FatalException extends RuntimeException {


  private int errorCode = ErrorsCode.UNDEFINED_ERROR;

  /**
   * Constructor.
   *
   * @param message message of the {@link Exception}
   * @deprecated since 18/03
   */
  @Deprecated
  public FatalException(String message) {
    super(message);
  }

  /**
   * Constructor.
   *
   * @param errorCode the code error of the {@link Exception}
   */
  public FatalException(int errorCode) {
    this.errorCode = errorCode;
  }
  
  /**
   * Constructor.
   *
   */
  public FatalException() {
  }

  public int getErrorCode() {
    return errorCode;
  }
  
}
