package biz.dto;

/**
 * Address' Data Transfer Objects.
 */
public interface AddressDto extends AbstractDto {

  String getStreet();

  void setStreet(String street);

  int getStreetNumber();

  void setStreetNumber(int streetNumber);

  int getBox();

  void setBox(int box);

  int getZipCode();

  void setZipCode(int zipCode);

  String getCommune();

  void setCommune(String commune);
}
