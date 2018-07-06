package biz.objects;

import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JE's business object.
 */
class JeBiz extends BizObject implements Je {

  /**
   * JE's participation list.
   */
  private List<Participation> participations = new ArrayList<>();

  /**
   * JE's Day Date. Format : YYYY-MM-DD
   */
  private LocalDate dayDate;

  /**
   * JE's Day Year.
   */
  private int dayYear;

  JeBiz() {}

  /*
   * Getters & Setters
   */

  @SuppressWarnings("deprecation")
  public void setDate(Timestamp date) {
    this.dayDate = date.toLocalDateTime().toLocalDate();
    this.dayYear = date.getYear() + 1900;

    if (this.dayDate.getMonthValue() > 8) {
      this.dayYear++;
    }
  }

  public void setDate(String date) {
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    try {
      this.setDate(Timestamp.from(format.parse(date).toInstant()));
    } catch (ParseException exc) {
      throw new IllegalArgumentException("le format de la date est incorrect");
    }
  }

  public void setDate(LocalDate date) {
    this.dayDate = date;
    this.dayYear = date.getYear();
  }

  public boolean addParticipation(Participation participation) {
    if (participations.contains(participation)) {
      return false;
    }
    return participations.add((Participation) participation.clone());
  }

  public boolean addParticipation(Company company) {
    Participation participation = new ParticipationBiz();
    participation.setCompany(company);
    participation.setJe(this);
    return addParticipation(participation);
  }

  public List<Participation> getParticipations() {
    List<Participation> clone = new ArrayList<>();
    for (Participation participation : participations) {
      clone.add((Participation) participation.clone());
    }
    return clone;
  }

  public Participation getParticipation(Participation participation) {
    int index = participations.indexOf(participation);
    if (index == -1) {
      return null;
    }
    return (Participation) participations.get(index).clone();
  }

  public Participation getParticipation(Company company) {
    Participation participation = new ParticipationBiz();
    participation.setCompany(company);
    participation.setJe(this);
    return getParticipation(participation);
  }

  public LocalDate getDayDate() {
    return this.dayDate;
  }

  @Override
  public void setDayDate(LocalDate dayDate) {
    this.dayDate = dayDate;
  }

  public int getDayYear() {
    return this.dayYear;
  }

  @Override
  public void setDayYear(int dayYear) {
    this.dayYear = dayYear;
  }

  public int getPk() {
    return this.dayYear;
  }

  @Deprecated
  public void setPk(int pk) {
    dayYear = pk;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + dayYear;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    JeBiz other = (JeBiz) obj;
    return dayYear == other.dayYear;
  }

  /**
   * Prepare the {@link Je} to be insert in the database.
   */
  public void prepareForInsert() throws BusinessException {
    this.checkConstraints();
  }

  /**
   * Check some Constraints.
   *
   * @throws BusinessException if of no respect of the constraints.
   */
  public void checkConstraints() throws BusinessException {
    if (dayDate == null) {
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_COMPANY_DAY);
    }
  }

  @Override
  public JeBiz clone() {
    try {
      JeBiz clone = (JeBiz) super.clone();
      return clone;
    } catch (CloneNotSupportedException exception) {
      throw new InternalError();
    }
  }

  @Override
  public String toString() {
    return "JeBiz [dayDate=" + dayDate + ", dayYear=" + dayYear + "]";
  }

}
