import config.AppConfig;
import ihm.ApplicationServlet;
import ihm.utils.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Scripts de démarrage de l'application.
 */
public class Main {

  private AppConfig appConfig;

  /**
   * Constructeur du main.
   *
   * @param state l'état de base : APP_DEV ou APP_PROD
   * @throws Exception si les configurations ne sont pas correctement chargées
   */
  private Main(String state) throws Exception {
    AppConfig app = new AppConfig(state);

    if (!app.load()) {
      Logger.log("Main", "Main", "Unable to load app configuration !", Logger.ERROR);

      throw new Exception("Internal error !");
    }

    String logDefinition = app.getProperty("logger.properties", null);
    if (logDefinition != null) {
      System.setProperty("log4j.configurationFile", "conf/" + logDefinition);
    }

    this.appConfig = app;
  }

  /**
   * Point d'entrée de l'application principale.
   *
   * @param args Liste des arguments donnés lors du lancement de le l'application
   */
  public static void main(String[] args) {
    try {
      String mode = AppConfig.APP_DEV;

      if (args.length > 0) {
        mode = args[0];
      }

      Main app = new Main(mode);

      if (!app.startJetty()) {
        Logger.log("Main", "main", "[" + mode + "] Server failed starting", Logger.ERROR);
      } else {
        Logger.log("Main", "main", "[" + mode + "] Server started at port : "
            + app.appConfig.getProperty("server.port"), Logger.SUCCESS);
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Démarre une nouvelle instance jetty. Note : les configurations de jetty peuvent etre override
   * dans le fichier .properties lancé.
   *
   * @return true si le serveur a correctement démarre
   */
  private boolean startJetty() {
    try {
      Server server;
      WebAppContext context;

      server = new Server(Integer.parseInt(appConfig.getProperty("server.port", "8000")));
      context = new WebAppContext();
      server.setHandler(context);

      context.setContextPath(appConfig.getProperty("server.path", "/"));
      context.setWelcomeFiles(new String[]{"index.html"});
      context.setResourceBase(appConfig.getProperty("server.resources", "web"));
      context.addServlet(new ServletHolder(appConfig.newInstanceOf(ApplicationServlet.class)),
          "/app/*");
      context.addServlet(new ServletHolder(new DefaultServlet()), "/");
      context.setInitParameter("cacheControl", "no-store, no-cache, must-revalidate");

      server.start();

      return true;
    } catch (Exception exception) {
      Logger.log("Main", "startJetty",
          "Internel system error - unable to start server: exiting application", Logger.ERROR);
      exception.printStackTrace();
      return false;
    }
  }

}
