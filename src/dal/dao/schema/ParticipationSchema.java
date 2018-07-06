package dal.dao.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface ParticipationSchema extends GenericSchema {

  String TABLE = SCHEMA + ".participations";

  String FIELD_PARTICIPATION_YEAR = "participation_year";
  String FIELD_PARTICIPATION_COMPANY_NUMBER = "company_number";
  String FIELD_PATICIPATION_STATE = "state";
  String FIELD_PARTICIPATION_LAST_STATE = "last_state";
  String FIELD_VERSION = "num_version";

  @SuppressWarnings("serial")
  List<String> FIELDS_MAP = Collections.unmodifiableList(new ArrayList<String>() {
    {
      add(FIELD_PARTICIPATION_YEAR);
      add(FIELD_PARTICIPATION_COMPANY_NUMBER);
      add(FIELD_PATICIPATION_STATE);
      add(FIELD_PARTICIPATION_LAST_STATE);
      add(FIELD_VERSION);
    }
  });

  @SuppressWarnings("serial")
  List<String> FIELDS_MAP_UPDATE = Collections.unmodifiableList(new ArrayList<String>() {
    {
      add(FIELD_PATICIPATION_STATE);
      add(FIELD_PARTICIPATION_LAST_STATE);
      add(FIELD_VERSION);
    }
  });


}
