package dal.dao.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Schema of the table companies in Database.
 */
public interface CompanySchema extends GenericSchema {

  String TABLE = SCHEMA + ".companies";

  String FIELD_COMPANY_PK = "company_number";
  String FIELD_COMPANY_NAME = "company_name";
  String FIELD_COMPANY_CREATOR = "creator";
  String FIELD_COMPANY_ADDRESS = "address_number";
  String FIELD_COMPANY_INSCRIPTION_DATE = "inscription_date";
  String FIELD_COMPANY_LAST_PARTICIPATION = "last_participation_year";
  String FIELD_VERSION = "num_version";

  @SuppressWarnings("serial")
  List<String> FIELDS_MAP = Collections.unmodifiableList(new ArrayList<String>() {
    {
      add(FIELD_COMPANY_NAME);
      add(FIELD_COMPANY_CREATOR);
      add(FIELD_COMPANY_ADDRESS);
      add(FIELD_COMPANY_INSCRIPTION_DATE);
      add(FIELD_COMPANY_LAST_PARTICIPATION);
      add(FIELD_VERSION);
    }
  });

  @SuppressWarnings("serial")
  List<String> FIELDS_MAP_UPDATE = Collections.unmodifiableList(new ArrayList<String>() {
    {
      add(FIELD_COMPANY_NAME);
      add(FIELD_COMPANY_LAST_PARTICIPATION);
      add(FIELD_VERSION);
    }
  });

}
