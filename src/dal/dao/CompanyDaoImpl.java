package dal.dao;

import biz.DtoFactory;
import biz.dto.AbstractDto;
import biz.dto.CompanyDto;
import biz.exceptions.FatalException;
import biz.exceptions.OptimisticLockException;
import biz.objects.Address;
import biz.objects.BizObject;
import biz.objects.Company;
import dal.DalBackEndServices;
import dal.dao.schema.AddressSchema;
import dal.dao.schema.CompanySchema;
import ihm.utils.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Company's Data Access Object Class.
 */
class CompanyDaoImpl extends AbstractDao implements CompanyDao, CompanySchema {

  /**
   * Insert Company auto generated query.
   */
  private static final String COMPANY_CREATE_QUERY =
      "INSERT INTO " + TABLE + " (" + String.join(",", FIELDS_MAP) + ") VALUES ("
          + String.join(",", Collections.nCopies(FIELDS_MAP.size(), "?")) + ") RETURNING *";

  /**
   * A compiled expression finding all fields from the table companies in database.
   */
  private static final String COMPANY_FIND_QUERY = "SELECT * FROM " + TABLE + " WHERE ";

  private static final String COMPANY_FIND_ALL_QUERY = "SELECT * FROM " + TABLE;

  /**
   * Update Company auto generated query.
   */
  private static final String COMPANY_UPDATE_QUERY =
      "UPDATE " + TABLE + " SET (" + String.join(",", FIELDS_MAP_UPDATE) + ") = ("
          + String.join(",", Collections.nCopies(FIELDS_MAP_UPDATE.size(), "?")) + ") WHERE "
          + FIELD_COMPANY_PK + " = ? AND " + FIELD_VERSION + " = ? RETURNING *";

  private AddressDao addressDao;
  @SuppressWarnings("unused")
  private UserDao userDao;

  /**
   * Constructor.
   *
   * @param dal services provider
   * @param factory {@link DtoFactory} object
   */
  public CompanyDaoImpl(DalBackEndServices dal, DtoFactory factory, AddressDao addressDao,
      UserDao userDao) {
    super(dal, factory);
    this.addressDao = addressDao;
    this.userDao = userDao;
  }

  /**
   * Find a single company based on a pk.
   *
   * @param pk the value to lookup
   * @return the {@link CompanyDto} instance found in the research
   */
  @Override
  public CompanyDto findByPk(int pk) {
    if (this.cacheContains(pk)) {
      return (CompanyDto) this.cacheGet(pk);
    }

    return (CompanyDto) this.cacheStore(pk, (BizObject) findBy(FIELD_COMPANY_PK, "" + pk));
  }

  /**
   * Find a single company based on a name.
   *
   * @param name the value to lookup
   * @return the {@link Company} instance found in the research
   */
  @Override
  public CompanyDto findByName(String name) {
    Company comp;

    if ("".equals(name)) {
      return null;
    }

    try {
      PreparedStatement stmt =
          this.dal.prepareStatement(COMPANY_FIND_QUERY + FIELD_COMPANY_NAME + " = ?");

      stmt.setString(1, name);
      ResultSet rs = stmt.executeQuery();

      if (!rs.next()) {
        rs.close();
        Logger.log("CompanyDaoImpl", "findByName", "The company was not found",
            Logger.INFORMATION);
        return null;
      }

      comp = populateDto(rs, true);
      // comp.setPk((Integer) rs.getObject(FIELD_COMPANY_PK));
      rs.close();
      //
      return comp;
    } catch (SQLException exception) {
      //      Logger.log("CompanyDaoImpl", "findByName", sqle.getMessage());
      //      throw new IllegalArgumentException(
      //          "Error querying company : " + COMPANY_FIND_QUERY + FIELD_COMPANY_NAME + " = ?");
      Logger.log("CompanyDaoImpl", "findByName", "The search of the company failed : "
          + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }
  }

  /**
   * Persist a company into the table companies in database.
   *
   * @param company the {@link Company} to persist
   * @return the value of the {@link Company} casted in {@link CompanyDto}
   */
  @Override
  public CompanyDto create(Company company) {

    try {
      PreparedStatement stmt = this.dal.prepareStatement(COMPANY_CREATE_QUERY);

      stmt.setString(1, company.getCompanyName());
      stmt.setInt(2, company.getCreator().getPk());
      stmt.setInt(3, company.getAddress().getPk());
      stmt.setTimestamp(4, Timestamp.valueOf(company.getInscriptionDate()));
      stmt.setInt(5, company.getLastParticipationYear());
      stmt.setInt(6, 1);
      ResultSet rs = stmt.executeQuery();

      if (!rs.next()) {
        rs.close();
        throw new FatalException();
      }

      company = populateDto(rs, true);

      rs.close();
      return company;
    } catch (SQLException exception) {
      Logger.log("CompanyDaoImpl", "create", "The company creation failed : " 
          + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }
  }
  
  @Override
  public AbstractDto insert(AbstractDto abstractDto) {
    try {
      return create((Company) abstractDto);
    } catch (ClassCastException exception) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Update a company already present in the database.
   *
   * @param company {@link Company} to persist
   * @return the new value of the {@link Company} casted in {@link CompanyDto}
   */
  @Override
  public CompanyDto update(Company company) {   
    try {
      PreparedStatement stmt = this.dal.prepareStatement(COMPANY_UPDATE_QUERY);
      stmt.setString(1, company.getCompanyName());
      stmt.setInt(2, company.getLastParticipationYear());
      stmt.setInt(3, company.getVersion() + 1);
      stmt.setInt(4, company.getPk());
      stmt.setInt(5, company.getVersion());

      ResultSet rs = stmt.executeQuery();

      if (!rs.next()) {
        rs.close();
        Logger.log("CompanyDaoImpl", "udpate", "The company update failed : " 
            + "The version number does not match that of the database", Logger.ERROR);
        throw new OptimisticLockException();
      }

      Company com = populateDto(rs, false);
      com.setAddress((Address) company.getAddress());

      Logger.log("CompanyDaoImpl", "udpate", "The company was successfully updated",
          Logger.SUCCESS);

      rs.close();
      
      return com;
    } catch (SQLException exception) {
      Logger.log("CompanyDaoImpl", "udpate", "The company update failed : " 
          + exception.getMessage(), Logger.ERROR);     
      throw new FatalException();
    }
  }
  
  @Override
  public AbstractDto update(AbstractDto abstractDto) {
    try {
      return update((Company) abstractDto);
    } catch (ClassCastException exception) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Find a company based on a couple of key<->value.
   *
   * @param key field to lookup
   * @param value value to match
   * @return the {@link CompanyDto} instance found in the research
   */
  private CompanyDto findBy(String key, Object value) {
    CompanyDto company;
    boolean pk = false;

    if (value == null) {
      return null;
    }

    if (key.equals(FIELD_COMPANY_PK)) {
      pk = true;
    }

    if (!pk && !FIELDS_MAP.contains(key)) {
      throw new IllegalArgumentException("You cannot lookup company based on " + key);
    }

    try {
      PreparedStatement stmt = this.dal.prepareStatement(COMPANY_FIND_QUERY + key + " = ?");

      if (!pk) {
        stmt.setString(1, (String) value);
      } else {
        stmt.setInt(1, Integer.parseInt((String) value));
      }

      ResultSet rs = stmt.executeQuery();
      
      if (!rs.next()) {
        rs.close();
        Logger.log("CompanyDaoImpl", "findBy", "The company was not found", Logger.INFORMATION);
        return null;
      }

      company = populateDto(rs, true);
      
      Logger.log("CompanyDaoImpl", "findBy", "The company was found",
          Logger.SUCCESS);
      
      rs.close();
      return company;
    } catch (SQLException exception) {
      Logger.log("CompanyDaoImpl", "findBy", "The search of the company failed : "
          + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }
  }

  /**
   * Create an instance of DTO company based on result set.
   *
   * @param rs {@link ResultSet} coming directly from database
   * @param withFk if it is true, the method search for all fk, its object
   * @return a {@link Company} object with the information of the {@link ResultSet}
   * @throws SQLException if an error occurs during result set access
   */
  private Company populateDto(ResultSet rs, boolean withFk) throws SQLException {
    Company company = (Company) this.factory.getCompany();

    if (withFk) {
      Address add = (Address) addressDao.findByPk(rs.getInt(FIELD_COMPANY_ADDRESS));
      company.setAddress(add);
    }

    company.setPk(rs.getInt(FIELD_COMPANY_PK));
    company.setCompanyName(rs.getString(FIELD_COMPANY_NAME));
    company.setInscriptionDate(rs.getTimestamp(FIELD_COMPANY_INSCRIPTION_DATE).toLocalDateTime());
    company.setLastParticipationYear(rs.getInt(FIELD_COMPANY_LAST_PARTICIPATION));
    company.setVersion(rs.getInt(FIELD_VERSION));

    return company;
  }

  /**
   * Get All the Companies.
   *
   * @return {@link List} of {@link CompanyDto}
   */
  @Override
  public List<CompanyDto> findAllCompanies() {
    List<CompanyDto> allComp = new ArrayList<CompanyDto>();
    try {
      PreparedStatement ps =
          this.dal.prepareStatement(COMPANY_FIND_ALL_QUERY + " ORDER BY " + FIELD_COMPANY_NAME);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        allComp.add(populateDto(rs, true));
      }
      rs.close();

    } catch (SQLException exception) {
      Logger.log("CompanyDaoImpl", "findAllCompanies", "The search of all companies failed : "
          + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }
    return allComp;
  }

  /**
   * Find all company for the selection.
   *
   * @return {@link List} of {@link CompanyDto}
   */
  @Override
  public List<CompanyDto> findAllNewCompanies() {
    List<CompanyDto> companies = new ArrayList<>();
    try {
      PreparedStatement stmt =
          this.dal.prepareStatement(COMPANY_FIND_QUERY + "((DATE_PART('year',current_date)-"
              + "DATE_PART('year'," + FIELD_COMPANY_INSCRIPTION_DATE + ")) * 12 + "
              + "(DATE_PART('month',current_date)-DATE_PART('month',"
              + FIELD_COMPANY_INSCRIPTION_DATE + "))) <= 12");

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        companies.add(populateDto(rs, true));
      }
      rs.close();

    } catch (SQLException exception) {
      Logger.log("ComapanyDaoImpl", "findAllNewCompanies", "The search of all new companies"
          + " failed : " + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }
    return companies;
  }

  @Override
  public Set<CompanyDto> search(String[] criteria) {
    Set<CompanyDto> companies = new HashSet<>();

    try {
      String statement = COMPANY_FIND_ALL_QUERY + " comp, " + AddressSchema.TABLE
          + " add WHERE comp." + FIELD_COMPANY_ADDRESS + "= add."
          + AddressSchema.FIELD_ADDRESS_PK + " AND (";

      List<String> list = new ArrayList<>();
      StringBuilder buf = new StringBuilder();

      for (String data : criteria) {

        if (data.isEmpty()) {
          continue;
        }

        buf.append("(LOWER(comp." + FIELD_COMPANY_NAME + ") LIKE ? OR LOWER(add."
            + AddressSchema.FIELD_ADDRESS_STREET + ") LIKE ? OR LOWER("
            + AddressSchema.FIELD_ADDRESS_COMMUNE + ") LIKE ?) AND ");

        list.add(data.toLowerCase());
        list.add(data.toLowerCase());
        list.add(data.toLowerCase());
      }

      statement += buf.toString().substring(0, (buf.toString().length() - 5)) + ")";

      PreparedStatement stmt = this.dal.prepareStatement(statement);

      int index = 0;
      for (String attr : list) {
        stmt.setString(++index, "%" + attr + "%");
      }

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        companies.add(populateDto(rs, true));
      }

      rs.close();
    } catch (SQLException exception) {
      Logger.log("CompanyDaoImpl", "search", "The search of the company failed : "
          + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }
    return companies;
  }
}