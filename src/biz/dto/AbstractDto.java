package biz.dto;

/**
 * Interface for Data Transfer Objects.
 */
public interface AbstractDto {

  /**
   * Get the primary key of the Object in the database.
   *
   * @return int the value of the primary key
   */
  int getPk();

  /**
   * Set the value of the primary key of a Object.
   *
   * @param primaryKey the new value of the primary key
   */
  void setPk(int primaryKey);

  /**
   * Get the number of version of the Object.
   *
   * @return {@link int} the number of version
   */
  int getVersion();

  /**
   * Update the number of version of the Object.
   *
   * @param version {@link int} the new number of version of the Object in the database
   */
  void setVersion(int version);

  Object clone();

}
