package biz.exceptions;

/**
 * Business Exception.
 */
@SuppressWarnings("serial")
public class OptimisticLockException extends RuntimeException {
  
  /**
   * Constructor.
   *
   * @param message message of the {@link Exception}
   */
  public OptimisticLockException(String message) {
    super(message);
  }
  
  /**
   * Constructor.
   *
   */
  public OptimisticLockException() {
    super();
  }
  
  public int getErrorCode() {
    return ErrorsCode.CONFLICT;
  }

}
