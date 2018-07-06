package dal.dao;


import biz.DtoFactory;
import biz.dto.AbstractDto;
import biz.dto.JeDto;
import biz.exceptions.FatalException;
import biz.objects.BizObject;
import biz.objects.Je;
import dal.DalBackEndServices;
import dal.dao.schema.JeSchema;
import ihm.utils.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link JeDao} implementation.
 */
public class JeDaoImpl extends AbstractDao implements JeDao, JeSchema {

  /**
   * Insert JE auto generated query.
   */
  private static final String JE_CREATE_QUERY =
      "INSERT INTO " + TABLE + " (" + String.join(",", FIELDS_MAP) + ") VALUES ("
          + String.join(",", Collections.nCopies(FIELDS_MAP.size(), "?")) + ") returning *";

  private static final String JE_FIND_ALL_QUERY = "SELECT * FROM " + TABLE + " ORDER BY " 
      + FIELD_COMPANY_DAYS_DAY_YEAR;
  /**
   * A compiled expression finding all fields from the table company_days in database.
   */
  private static final String JE_FIND_QUERY = "SELECT * FROM " + TABLE + " WHERE ";

  /**
   * Constructor.
   *
   * @param dal services provider
   * @param factory {@link DtoFactory} object
   */
  JeDaoImpl(DalBackEndServices dal, DtoFactory factory) {
    super(dal, factory);
  }

  /**
   * Create an instance of DTO compay_day based on result set.
   *
   * @param rs {@link ResultSet} coming directly from database
   * @return a {@link Je} object with the information of the {@link ResultSet}
   * @throws SQLException if an error occurs during result set access
   */
  private Je populateDto(ResultSet rs) throws SQLException {
    Je je = (Je) this.factory.getCompanyDay();
    je.setDayDate(rs.getTimestamp(FIELD_COMPANY_DAYS_DAY_DATE).toLocalDateTime().toLocalDate());
    je.setDayYear(rs.getInt(FIELD_COMPANY_DAYS_DAY_YEAR));
    return je;
  }

  /**
   * Search a je for the given year.
   *
   * @param year : the integer that is the year searched
   * @return a {@link Je} object with the information of the {@link ResultSet}
   * @throws SQLException if an error occurs during result set access
   */
  @Override
  public JeDto findByYear(int year) {
    if (this.cacheContains(year)) {
      return (JeDto) this.cacheGet(year);
    }

    return (JeDto) this.cacheStore(year, (BizObject) findBy(FIELD_COMPANY_DAYS_DAY_YEAR, year));
  }

  private JeDto findBy(String key, int value) {
    JeDto je;

    if (!FIELDS_MAP.contains(key)) {
      throw new IllegalArgumentException("You cannot lookup JE based on " + key);
    }
    
    try {
      PreparedStatement ps = this.dal.prepareStatement(JE_FIND_QUERY + key + "= ?");
      ps.setInt(1, value);
      ResultSet rs = ps.executeQuery();
      
      if (!rs.next()) {
        rs.close();
        Logger.log("JeDaoImpl", "findBy", "The JE was not found", Logger.INFORMATION);
        return null;
      }
      
      je = populateDto(rs);
      
      rs.close();
    } catch (SQLException exception) {
      Logger.log("JeDaoImpl", "findBy", "The search of the JE failed : " 
          + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }

    return je;
  }

  /**
   * Persist a je into the table company_days in database.
   *
   * @param je to persist
   * @throws Exception the exception to launch
   */
  @Override
  public JeDto create(Je je) {
    try {
      PreparedStatement stmt = this.dal.prepareStatement(JE_CREATE_QUERY);
      
      stmt.setInt(1, je.getDayYear());
      stmt.setTimestamp(2, Timestamp.valueOf(je.getDayDate().atStartOfDay()));

      System.out.println(stmt.toString());
      
      ResultSet rs = stmt.executeQuery();

      if (!rs.next()) {
        rs.close();
        Logger.log("JeDaoImpl", "create", "The JE creation failed", Logger.ERROR);
        throw new FatalException();
      }

      je = populateDto(rs);

      rs.close();
      return je;
    } catch (SQLException exception) {
      Logger.log("JeDaoImpl", "create", "The JE creation failed : " 
          + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }
  }
  
  @Override
  public AbstractDto insert(AbstractDto abstractDto) {
    try {
      return create((Je) abstractDto);
    } catch (ClassCastException exception) {
      throw new IllegalArgumentException();
    }
  }
  
  @Override
  public AbstractDto update(AbstractDto abstractDto) {
    throw new UnsupportedOperationException();
  }

  /**
   * Get all the company days from the database.
   *
   * @return a {@link JeDto} list of Je data transfer object with the information of the {@link
   * ResultSet}
   * @throws SQLException if an error occurs during result set access
   */
  @Override
  public List<JeDto> findAllJe() {
    List<JeDto> allJe = new ArrayList<JeDto>();
    try {
      PreparedStatement ps = this.dal.prepareStatement(JE_FIND_ALL_QUERY);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        allJe.add(populateDto(rs));
      }

      rs.close();

    } catch (SQLException exception) {
      Logger.log("JeDaoImpl", "findAllJe", "The search of all JE was failed : " 
          + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }
    return allJe;
  }
}
