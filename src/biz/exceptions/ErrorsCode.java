package biz.exceptions;


public interface ErrorsCode {

  int UNDEFINED_ERROR = 0;

  int OK = 200;
  int CREATED = 201;
  int SUPER_USER = 202;
  int CREATES_DENIED = 203;

  int HTTP_ERROR_BAD_REQUEST = 400;
  int HTTP_ERROR_UNAUTHORIZED = 401;
  int HTTP_ERROR_PAGE_FAULT = 404;
  int CONFLICT = 409;
  int NO_RESPONSE = 444;

  int BUSINESS_INVALID_CREDENTIALS = 460;
  int BUSINESS_LOGIN_USER_EXISTED = 461;
  int BUSINESS_LOGIN_TOO_LONG = 462;
  int BUSINESS_INCORRECT_EMAIL_FORMAT = 463;
  int BUSINESS_INCORRECT_PASSWORD = 464;
  int BUSINESS_INCORRECT_LOGIN = 465;
  int BUSINESS_EMAIL_USER_EXISTED = 466;
  int BUSINESS_USER_ALREADY_LOGGED_IN = 467;
  int BUSINESS_ERROR_DECODING_JSON = 468;
  int BUSINESS_INCORRECT_DATE = 469;
  int BUSINESS_ALREADY_JE_FOR_DATE = 470;
  int BUSINESS_INVALID_DATE_FORMAT = 471;
  int BUSINESS_INCORRECT_INFORMATIONS = 472;
  int BUSINESS_NAME_COMPANY_EXISTED = 473;
  int BUSINESS_INVALID_CONTACT = 474;
  int BUSINESS_ALREADY_CONTACT_FOR_ID = 475;
  int BUSINESS_INVALID_COMPANY_NAME = 476;
  int BUSINESS_INVALID_COMPANY = 477;
  int BUSINESS_EMAIL_CONTACT_EXISTED = 478;
  int BUSINESS_INVALID_USER = 479;
  int BUSINESS_INVALID_ADDRESS = 480;
  int BUSINESS_INVALID_PARTICIPATION = 481;
  int BUSINESS_INVALID_PRESENCE = 482;
  int BUSINESS_INVALID_COMPANY_DAY = 483;
  int BUSINESS_INVALID_PARTICIPATION_STATE = 484;

  int INTERNAL_UNKNOWN_ERROR = 500;
  int SERVICE_UNAVAILABLE = 503;

}
