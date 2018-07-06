package biz.dto;

import biz.exceptions.BusinessException;
import java.sql.Timestamp;
import java.time.LocalDate;

public interface JeDto extends AbstractDto {

  LocalDate getDayDate();

  void setDayDate(LocalDate date);

  void setDate(Timestamp date);

  void setDate(String date);

  void setDate(LocalDate date);

  int getDayYear();

  void setDayYear(int year);

  void prepareForInsert() throws BusinessException;
}
