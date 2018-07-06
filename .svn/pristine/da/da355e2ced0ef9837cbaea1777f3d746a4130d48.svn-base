package ihm.utils;

import config.annotation.Service;
import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;

public class Logger {

  private static org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();

  private static org.apache.logging.log4j.Logger errorLogger = LogManager.getLogger("logger.errors");

  public static final int SUCCESS = 0;

  public static final int INFORMATION = 1;

  public static final int ERROR = 2;

  public static final int SYSTEM = 3;

  @Service
  public Logger() {}

  /**
   * Print a formatted data to output e.g. 1028302 [generic - server] server started.
   *
   * @param classe {@link Class} from where the output comes
   * @param method {@link Method} form where the output comes
   * @param message the message
   */
  public static void log(String classe, String method, String message, int statut) {
    switch (statut) {
      case SUCCESS :
        logger.info("[" + classe + " - " + method + "] " + message);
        break;
      case INFORMATION :
        logger.info("[" + classe + " - " + method + "] " + message);
        break;
      case ERROR :
        errorLogger.error("[" + classe + " - " + method + "] " + message);
        break;
      default :
        logger.debug("[" + classe + " - " + method + "] " + message);
        break;
    }
  }

}
