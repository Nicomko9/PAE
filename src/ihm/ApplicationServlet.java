package ihm;

import biz.AddressUcc;
import biz.CompanyUcc;
import biz.ContactUcc;
import biz.DtoFactory;
import biz.JeUcc;
import biz.ParticipationUcc;
import biz.PresenceUcc;
import biz.SearchUcc;
import biz.UserUcc;
import biz.dto.AddressDto;
import biz.dto.CompanyDto;
import biz.dto.ContactDto;
import biz.dto.JeDto;
import biz.dto.ParticipationDto;
import biz.dto.PresenceDto;
import biz.dto.UserDto;
import biz.exceptions.BusinessErrorException;
import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import biz.exceptions.OptimisticLockException;
import biz.objects.Address;
import biz.objects.Company;
import biz.objects.Contact;
import biz.objects.Je;
import biz.objects.Participation;
import biz.objects.Presence;
import ihm.utils.Logger;
import ihm.utils.ServletUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;

/**
 * Unique Servlet of the application.
 */
public class ApplicationServlet extends HttpServlet {


  private static final long serialVersionUID = 1L;

  /**
   * Json encoding / decoding by shared service.
   */
  private transient ServletUtil servletUtil;

  /**
   * User's session utility.
   */
  private transient SessionManager userSessions;

  /**
   * Business object's factory.
   */
  private DtoFactory dtoFactory;

  /**
   * User Use-Case Controller.
   */
  private UserUcc userUcc;

  /**
   * Je Use-Case Controller.
   */
  private JeUcc jeUcc;

  /**
   * Address Use-Case Controller.
   */
  private AddressUcc addressUcc;

  /**
   * Company Use-Case Controller.
   */
  private CompanyUcc companyUcc;

  /**
   * Contact Use-Case Controller.
   */
  private ContactUcc contactUcc;

  /**
   * Participation Use-Case Controller.
   */
  private ParticipationUcc participationUcc;

  /**
   * Presence Use-Case Controller.
   */
  private PresenceUcc presenceUcc;

  /**
   * Search Use-Case Controller.
   */
  private SearchUcc searchUcc;

  private org.apache.logging.log4j.Logger accessLog = LogManager.getLogger("logger.access");

  /**
   * Construct the servlet.
   *
   * @param dtoFactory Business Object's factory
   * @param userSessions Manager of user's session
   * @param servletUtil Service encoding / decoding JSON and filling DTO's
   * @param userUcc User Use-Case Controller
   * @param jeUcc Je USe-Case Controller
   * @param addressUcc Address Use-Case Controller
   * @param companyUcc Company Use-Case Controller
   * @param contactUcc Contact Use-Case Controller
   * @param participationUcc Participation Use-Case Controller
   * @param presenceUcc Presence Use-Case Controller
   */
  ApplicationServlet(DtoFactory dtoFactory, SessionManager userSessions, ServletUtil servletUtil,
      TemplateLoader templateLoader, UserUcc userUcc, JeUcc jeUcc, AddressUcc addressUcc,
      CompanyUcc companyUcc, ContactUcc contactUcc, ParticipationUcc participationUcc,
      PresenceUcc presenceUcc, SearchUcc searchUcc) {

    templateLoader.compile();

    this.dtoFactory = dtoFactory;
    this.userSessions = userSessions;
    this.servletUtil = servletUtil;

    this.userUcc = userUcc;
    this.jeUcc = jeUcc;
    this.addressUcc = addressUcc;
    this.companyUcc = companyUcc;
    this.contactUcc = contactUcc;
    this.participationUcc = participationUcc;
    this.presenceUcc = presenceUcc;
    this.searchUcc = searchUcc;
  }

  /**
   * Handle the POST Http request.
   *
   * @param req request object
   * @param resp response object
   * @throws BusinessException Already connected
   * @throws BusinessException Internal JSON decoding error
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws BusinessException {
    UserDto userDto;
    UserDto connectedUser = userSessions.readSession(req);
    ServletUtil servletUtil = new ServletUtil();

    switch (req.getPathInfo()) {
      case "/connection":
        if (null != connectedUser) {
          throw new BusinessException(ErrorsCode.BUSINESS_USER_ALREADY_LOGGED_IN);
        }
        userDto = this.userUcc.connect(req.getParameter("login"), req.getParameter("password"));
        this.handleResponse(resp, userSessions.createSession(req, resp, userDto));
        return;

      case "/signup":
        try {
          Map<String, Object> requestMap =
              (Map<String, Object>) servletUtil.decode(req.getParameter("json"), Map.class);
          if (null == requestMap) {
            throw new BusinessException(ErrorsCode.BUSINESS_ERROR_DECODING_JSON);
          }
          userDto = servletUtil.fillUserDto(this.dtoFactory.getUser(), requestMap);
        } catch (Exception exception) {
          throw new BusinessErrorException(ErrorsCode.INTERNAL_UNKNOWN_ERROR);
        }
        userDto = this.userUcc.subscribe(userDto);
        if (null != userDto) {
          this.handleResponse(resp, userSessions.createSession(req, resp, userDto));
        }
        return;

      case "/disconnect":
        userSessions.deleteSession(req, resp);
        this.handleResponse(resp, true);
        return;
      default:
        break;
    }

    checkConnection(req, connectedUser);

    switch (req.getPathInfo()) {
      case "/auth":
        this.handleResponse(resp, connectedUser);
        break;
      case "/search":
        this.handleResponse(resp, searchUcc.search(req.getParameter("search")));
        break;
      case "/je":
        int year;
        try {
          year = Integer.parseInt(req.getParameter("year"));
        } catch (Exception exception) {
          throw new BusinessException(ErrorsCode.BUSINESS_INCORRECT_DATE);
        }

        this.handleResponse(resp, jeUcc.getJeByYear(year));
        break;
      case "/je/create":
        JeDto je = this.dtoFactory.getCompanyDay();
        try {
          je.setDate(req.getParameter("date"));
        } catch (Exception exception) {
          throw new BusinessException(ErrorsCode.BUSINESS_INVALID_DATE_FORMAT);
        }

        this.handleResponse(resp, jeUcc.createNewJe(je));
        break;
      case "/je/invite":
        int dateYear = Integer.parseInt(req.getParameter("year"));
        Set<String> set =
            (HashSet<String>) this.servletUtil.decode(req.getParameter("json"), Set.class);

        JeDto jeInvite = jeUcc.getJeByYear(dateYear);
        List<ParticipationDto> participations = new ArrayList<ParticipationDto>();
        for (String id : set) {
          CompanyDto company = companyUcc.getCompanyByPk(Integer.parseInt(id));
          ParticipationDto participation = servletUtil
              .fillParticipationDto(this.dtoFactory.getParticipation(), company, jeInvite);
          participations.add(participation);
        }

        this.handleResponse(resp, participationUcc.inviteCompanies(participations));
        break;
      case "/noJe":
        /*
         * Check if ther is a JE for this year return true or false
         */
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        jeUcc.getJeByYear(cal.get(Calendar.YEAR));
        break;
      case "/je/list":
        /*
         * Return all the years of previous JE
         */
        this.handleResponse(resp, jeUcc.getAllJe());
        break;
      case "/je/previous":
        /*
         * Return all the years of previous JE
         */
        this.handleResponse(resp, jeUcc.getPreviousJe());
        break;
      case "/entreprise/listForInvite":
        Logger.log("ApplicationServlet", "doPost", "/entreprise/listForInvite", Logger.INFORMATION);
        this.handleResponse(resp, this.companyUcc.getAllCompaniesForTheSelection());
        break;
      case "/entreprise/create":
        Logger.log("ApplicationServlet", "doPost", "/entreprise/create", Logger.INFORMATION);
        CompanyDto companyDto = this.dtoFactory.getCompany();
        Map<String, Object> requestMap =
            (Map<String, Object>) servletUtil.decode(req.getParameter("json"), Map.class);
        if (null == requestMap) {
          throw new BusinessException(ErrorsCode.BUSINESS_ERROR_DECODING_JSON);
        }

        AddressDto addressDto =
            servletUtil.fillAddressDto(this.dtoFactory.getAddress(), requestMap);
        addressDto = this.addressUcc.register(addressDto);
        if (addressDto == null) {
          return;
        }
        UserDto user = userUcc.getUserByLogin(connectedUser.getLogin());
        companyDto = servletUtil.fillCompanyDto(companyDto, requestMap, user, addressDto);
        this.handleResponse(resp, this.companyUcc.register(companyDto));
        break;
      case "/entreprise/list":
        Logger.log("ApplicationServlet", "doPost", "/entreprise/list", Logger.INFORMATION);
        /*
         * return all the entreprises
         */
        this.handleResponse(resp, this.companyUcc.getAllCompanies());
        break;
      case "/entreprise/update":
        Logger.log("ApplicationServlet", "doPost", "/entreprise/update", Logger.INFORMATION);
        /*
         * Update the company in the datatable
         */
        CompanyDto compDto = this.dtoFactory.getCompany();
        requestMap =
            (Map<String, Object>) this.servletUtil.decode(req.getParameter("json"), Map.class);
        if (requestMap == null) {
          throw new BusinessException(ErrorsCode.BUSINESS_ERROR_DECODING_JSON);
        }
        AddressDto address =
            this.servletUtil.fillAddressDto(this.dtoFactory.getAddress(), requestMap);
        try {
          address = this.addressUcc.update(address);
          compDto.setAddress((Address) address);
          compDto.setCompanyName((String) requestMap.get("name"));
          compDto.setVersion(Integer.parseInt((String) requestMap.get("version")));
          compDto.setPk(Integer.parseInt((String) requestMap.get("pk")));
          this.handleResponse(resp, this.companyUcc.update(compDto));
        } catch (OptimisticLockException exception) {
          this.handleResponse(resp, exception.getErrorCode(), exception.getErrorCode());
        }
        break;
      case "/contact/create":
        Logger.log("ApplicationServlet", "doPost", "/contact/create", Logger.INFORMATION);
        ContactDto contactDto = this.dtoFactory.getContact();
        this.handleResponse(resp, this.contactUcc.createNewContact(
            this.servletUtil.requestContact(req, "json", contactDto, this.companyUcc)));
        break;
      case "/contact/list":
        /*
         * Return all the contact
         */

        this.handleResponse(resp, this.contactUcc.getAllContact());
        break;
      case "/contact/listForCompany":
        /*
         * List all the contacts for a given company
         */
        if (req.getParameter("json") != null) {
          if (req.getParameter("json").equals("")) {
            throw new BusinessException(ErrorsCode.HTTP_ERROR_BAD_REQUEST);
          }
          int id = Integer.parseInt(req.getParameter("json"));

          List<ContactDto> contacts = this.contactUcc.getContactsForCompany(id);

          for (ContactDto c : contacts) {
            this.presenceUcc.getPresencesForContact(c.getPk()).stream()
                .forEach(p -> c.addPresence((Presence) p));
          }

          this.handleResponse(resp, contacts);
        } else {
          this.handleResponse(resp, false);
        }
        break;
      case "/contact/update":
        ContactDto cont = this.dtoFactory.getContact();
        cont = this.servletUtil.requestContact(req, "json", cont, this.companyUcc);
        try {
          this.handleResponse(resp, this.contactUcc.update(cont));
        } catch (OptimisticLockException exception) {
          this.handleResponse(resp, exception.getErrorCode(), exception.getErrorCode());
        }
        break;
      case "/participations/company":
        CompanyDto cdto = this.dtoFactory.getCompany();
        cdto.setPk(Integer.parseInt(req.getParameter("company-pk")));
        this.handleResponse(resp, participationUcc.getAllParticipationsOf(cdto));
        break;
      case "/participations":
        JeDto jeDto = this.dtoFactory.getCompanyDay();
        int annee = Integer.parseInt(req.getParameter("annee"));
        jeDto = jeUcc.getJeByYear(annee);
        if (jeDto == null) {
          this.handleResponse(resp, null, 400);
        } else {
          this.handleResponse(resp, participationUcc.getAllParticipationsOf(jeDto));
        }
        break;
      case "/company/select":
        int i = Integer.parseInt(req.getParameter("pk"));
        this.handleResponse(resp, this.companyUcc.getCompanyByPk(i));
        break;
      case "/contact/select":
        int k = Integer.parseInt(req.getParameter("pk"));
        this.handleResponse(resp, this.contactUcc.getContactByPk(k));
        break;
      case "/contacts/forCompanies":
        Set<String> companies =
            (Set<String>) this.servletUtil.decode(req.getParameter("json"), Set.class);
        List<ContactDto> dtoList = new ArrayList<>();
        for (String pk : companies) {
          dtoList.addAll(this.contactUcc.getContactsForCompany(Integer.parseInt(pk)));
        }
        this.handleResponse(resp, dtoList);
        break;
      case "/participation/update":
        annee = Integer.parseInt(req.getParameter("je"));
        int companyPk = Integer.parseInt(req.getParameter("company"));
        jeDto = jeUcc.getJeByYear(annee);
        companyDto = companyUcc.getCompanyByPk(companyPk);
        ParticipationDto participation = this.dtoFactory.getParticipation();
        participation.setCompany((Company) companyDto);
        participation.setJe((Je) jeDto);
        participation.setLastState(req.getParameter("lastState"));
        participation.setState(req.getParameter("state"));
        participation.setVersion(Integer.parseInt(req.getParameter("version")));
        try {
          switch (req.getParameter("state")) {
            case "refusée":
              this.handleResponse(resp, this.participationUcc.refuse(participation));
              break;
            case "confirmée":
              this.handleResponse(resp, this.participationUcc.confirm(participation));
              break;
            case "facturée":
              this.handleResponse(resp, this.participationUcc.invoice(participation));
              break;
            case "payée":
              this.handleResponse(resp, this.participationUcc.pay(participation));
              break;
            default:
              throw new BusinessException(ErrorsCode.HTTP_ERROR_BAD_REQUEST);
          }
        } catch (OptimisticLockException exception) {
          this.handleResponse(resp, exception.getErrorCode(), exception.getErrorCode());
        }
        break;
      case "/participation/cancel":
        annee = Integer.parseInt(req.getParameter("je"));
        int companyPkCancel = Integer.parseInt(req.getParameter("company"));
        jeDto = jeUcc.getJeByYear(annee);
        companyDto = companyUcc.getCompanyByPk(companyPkCancel);
        participation = this.dtoFactory.getParticipation();
        participation.setCompany((Company) companyDto);
        participation.setJe((Je) jeDto);
        participation.setState(req.getParameter("state"));
        participation.setVersion(Integer.parseInt(req.getParameter("version")));
        try {
          this.handleResponse(resp, participationUcc.cancel(participation));
        } catch (OptimisticLockException exception) {
          this.handleResponse(resp, false, exception.getErrorCode());
        }
        break;
      case "/participation/rollback":
        annee = Integer.parseInt(req.getParameter("je"));
        int companyPkRollback = Integer.parseInt(req.getParameter("company"));
        jeDto = jeUcc.getJeByYear(annee);
        companyDto = companyUcc.getCompanyByPk(companyPkRollback);
        ParticipationDto participationRollback = this.dtoFactory.getParticipation();
        participationRollback.setCompany((Company) companyDto);
        participationRollback.setJe((Je) jeDto);
        participationRollback.setState(req.getParameter("state"));
        participationRollback.setLastState(req.getParameter("lastState"));
        participationRollback.setVersion(Integer.parseInt(req.getParameter("version")));
        try {
          this.handleResponse(resp, participationUcc.rollback(participationRollback));
        } catch (OptimisticLockException exception) {
          this.handleResponse(resp, exception.getErrorCode(), exception.getErrorCode());
        }
        break;
      case "/presences":
        int anneePresences = Integer.parseInt(req.getParameter("annee"));
        JeDto jePresences = jeUcc.getJeByYear(anneePresences);
        if (jePresences == null) {
          this.handleResponse(resp, "Il n'y pas de JE pour cette année", 400);
        } else {
          this.handleResponse(resp, presenceUcc.getAllPresencesOf(jePresences));
        }
        break;
      case "/presences/change":
        int contactId = Integer.parseInt(req.getParameter("pk"));
        PresenceDto presence = this.dtoFactory.getPresence();
        ContactDto contactPresent = (this.contactUcc.getContactByPk(contactId));
        presence.setCompany((Company) contactPresent.getCompany());
        presence.setActif(contactPresent.isActive());
        presence.setContact((Contact) contactPresent);
        jeDto = this.jeUcc.getJeByYear(Integer.parseInt(req.getParameter("year")));
        List<ParticipationDto> parts = this.participationUcc.getAllParticipationsOf(jeDto);
        for (ParticipationDto par : parts) {
          if (par.getCompany().getPk() == contactPresent.getCompany().getPk()) {
            presence.setParticipation((Participation) par);
          }
        }
        this.handleResponse(resp, this.presenceUcc.register(presence));
        break;
      case "/presences/listForContact":
        if (req.getParameter("json").equals("")) {
          throw new BusinessException(ErrorsCode.HTTP_ERROR_BAD_REQUEST);
        }
        int idCon = Integer.parseInt(req.getParameter("json"));
        this.handleResponse(resp, this.presenceUcc.getPresencesForContact(idCon));
        break;
      default:
        this.handleResponse(resp, "Page not found", 404);
        break;
    }

  }

  /**
   * In case of business exception, render a JSON response "{ error : message }".
   */
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      super.service(req, resp);
      this.accessLog.info("[" + req.getMethod() + "] " + req.getPathInfo() + " : response OK");
    } catch (OptimisticLockException exception) {
      this.accessLog.error("[" + req.getMethod() + "] " + req.getPathInfo()
          + " : optimistic lock exception (" + exception.getErrorCode() + ")");
      this.handleResponse(resp, exception.getErrorCode(), exception.getErrorCode());
    } catch (BusinessException exception) {
      Integer errCode = exception.getErrorCode();
      this.accessLog.error("[" + req.getMethod() + "] " + req.getPathInfo()
          + " : business exception (" + errCode + ")");

      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      resp.setContentType("application/json");
      resp.getWriter().write(errCode.toString());
    } catch (Exception exception) {
      this.handleResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Send a response to back-end.
   *
   * @param resp response object
   * @param data the data to send to front-end
   * @param code page status code (200, 404, etc)
   */
  private void handleResponse(HttpServletResponse resp, Object data, int code) {
    try {
      String str = servletUtil.encode(data);

      byte[] bytes = str.getBytes(Charset.forName("UTF-8"));

      resp.setContentLength(bytes.length);
      resp.setContentType("application/javascript");
      resp.setStatus(code);
      resp.getOutputStream().write(bytes);
    } catch (IOException exception) {
      try {
        resp.sendError(500);
      } catch (IOException ioe) {
        Logger.log("ApplicationServlet", "handleResponse", "Internal error", Logger.ERROR);
      }
    }
  }

  /**
   * Default status code is 200.
   */
  private void handleResponse(HttpServletResponse resp, Object data) {
    handleResponse(resp, data, 200);
  }


  /**
   * Throws a BusinessException if there is no session or cookie.
   *
   * @param req request
   */
  private void checkConnection(HttpServletRequest req, UserDto userDto) {
    if (userDto == null) {
      throw new BusinessErrorException(ErrorsCode.HTTP_ERROR_UNAUTHORIZED);
    }
  }


}
