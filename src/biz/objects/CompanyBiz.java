package biz.objects;

import biz.dto.AddressDto;
import biz.dto.UserDto;
import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import com.owlike.genson.annotation.JsonIgnore;
import ihm.utils.DataValidator;
import java.time.LocalDateTime;

/**
 * Company's business object.
 */
class CompanyBiz extends BizObject implements Company {

  /**
   * Company's name.
   */
  private String companyName;

  /**
   * Company's creator.
   */
  @JsonIgnore
  private User creator;

  /**
   * Company's address.
   */
  private Address address;

  /**
   * Company's inscription date.
   */
  private LocalDateTime inscriptionDate;

  /**
   * Company's last year of participation.
   */
  private int lastParticipationYear;

  CompanyBiz() {}

  /*
   * Getters & setters.
   */

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  @JsonIgnore
  public UserDto getCreator() {
    if (creator == null) {
      return null;
    }
    return (UserDto) creator.clone();
  }

  @JsonIgnore
  public void setCreator(User creator) {
    this.creator = (creator != null) ? (User) creator.clone() : null;
  }

  public AddressDto getAddress() {
    if (this.address == null) {
      return null;
    }
    return (AddressDto) address.clone();
  }

  public void setAddress(Address address) {
    this.address = (address != null) ? (Address) address.clone() : null;
  }

  public LocalDateTime getInscriptionDate() {
    return inscriptionDate;
  }

  public void setInscriptionDate(LocalDateTime inscriptionDate) {
    this.inscriptionDate = inscriptionDate;
  }

  public int getLastParticipationYear() {
    return lastParticipationYear;
  }

  public void setLastParticipationYear(int lastParticipationYear) {
    this.lastParticipationYear = lastParticipationYear;
  }

  /**
   * Prepare the {@link Company} to be insert in the database.
   */
  @Override
  public void prepareForInsert() {
    creator.checkConstraints();
    this.checkConstraints();
  }

  @Override
  public void checkConstraints() {
    if (!DataValidator.validateString(companyName) || address == null) {
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_COMPANY);
    }
    address.checkConstraints();
  }

  @Override
  public CompanyBiz clone() {
    try {
      CompanyBiz clone = (CompanyBiz) super.clone();
      clone.address = (address != null) ? (Address) address.clone() : null;
      clone.creator = (creator != null) ? (User) creator.clone() : null;
      return clone;
    } catch (CloneNotSupportedException exception) {
      throw new InternalError();
    }
  }

  @Override
  public String toString() {
    return "CompanyBiz [primaryKey=" + primaryKey + ", companyName=" + companyName + ", creator="
        + ((creator != null) ? creator.getLogin() : null) + ", address="
        + ((address != null) ? address.getStreet() : null) + ", inscriptionDate=" + inscriptionDate
        + ", lastParticipationYear=" + lastParticipationYear + "]";
  }

}
