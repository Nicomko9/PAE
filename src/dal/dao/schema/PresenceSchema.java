package dal.dao.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Schema of the table contacts in Database.
 */
public interface PresenceSchema extends GenericSchema {

  String TABLE = SCHEMA + ".presences";

  String FIELD_PRESENCE_PARTICIPATION_YEAR = "participation_year";
  String FIELD_PRESENCE_COMPANY_NUMBER = "company_number";
  String FIELD_PRESENCE_CONTACT_NUMBER = "contact_number";
  String FIELD_PRESENCE_ETAT = "";

  @SuppressWarnings("serial")
  List<String> FIELDS_MAP = Collections.unmodifiableList(new ArrayList<String>() {
    {
      add(FIELD_PRESENCE_PARTICIPATION_YEAR);
      add(FIELD_PRESENCE_COMPANY_NUMBER);
      add(FIELD_PRESENCE_CONTACT_NUMBER);
    }
  });


}
