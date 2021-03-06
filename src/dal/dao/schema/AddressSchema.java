package dal.dao.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Schema of the table addresses in Database.
 */
public interface AddressSchema extends GenericSchema {

  String TABLE = SCHEMA + ".addresses";

  String FIELD_ADDRESS_PK = "address_number";
  String FIELD_ADDRESS_STREET = "street";
  String FIELD_ADDRESS_STREET_NUMBER = "street_number";
  String FIELD_ADDRESS_BOX = "box";
  String FIELD_ADDRESS_ZIPCODE = "zip_code";
  String FIELD_ADDRESS_COMMUNE = "commune";
  String FIELD_VERSION = "num_version";

  @SuppressWarnings("serial")
  List<String> FIELDS_MAP = Collections.unmodifiableList(new ArrayList<String>() {
    {
      add(FIELD_ADDRESS_STREET);
      add(FIELD_ADDRESS_STREET_NUMBER);
      add(FIELD_ADDRESS_BOX);
      add(FIELD_ADDRESS_ZIPCODE);
      add(FIELD_ADDRESS_COMMUNE);
      add(FIELD_VERSION);
    }
  });
}
