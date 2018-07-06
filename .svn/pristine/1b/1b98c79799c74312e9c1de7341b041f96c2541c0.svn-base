package biz;

import biz.dto.AddressDto;
import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import biz.exceptions.FatalException;
import biz.exceptions.OptimisticLockException;
import biz.objects.Address;
import dal.DalFrontEndServices;
import dal.dao.AddressDao;
import ihm.utils.DataValidator;

/**
 * Implementation of the Address' Use Case Controller.
 */
class AddressUccImpl implements AddressUcc {

  private DalFrontEndServices dal;

  private AddressDao addressDao;
  
  private UnitOfWork uof;

  AddressUccImpl(DalFrontEndServices dal, AddressDao addressDao, UnitOfWork uof) {
    this.dal = dal;
    this.addressDao = addressDao;
    this.uof = uof;
  }

  /**
   * Register a new address in the database.
   *
   * @param addressDto {@link AddressDto} to persist
   * @return the addressDto with the information added in the DataBase
   */
  @Override
  public AddressDto register(AddressDto addressDto) {

    Address address = (Address) addressDto;
    address.checkConstraints();

    try {
      //this.dal.startTransaction();
      this.uof.startTransaction();
      
      //addressDto = this.addressDao.create(address);
      this.uof.insert(address);
      
      //this.dal.commitTransaction();
      this.uof.commitTransaction();

      addressDto = (AddressDto) this.uof.getInsertedObject();
      
      return addressDto;
    } catch (Exception exception) {
      //this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw new FatalException();
    }
  }

  /**
   * Get an Address by its street, street number, zip code and commune.
   *
   * @param street of the Address
   * @param streetNumber of the Address
   * @param zipCode of the Address
   * @param commune of the Address
   * @return {@link AddressDto} that fits the good parameters
   */
  @Override
  public AddressDto getAddress(String street, int streetNumber, int zipCode, String commune) {
    if (!DataValidator.validateString(street)) {
      throw new BusinessException(ErrorsCode.HTTP_ERROR_BAD_REQUEST);
    }
    if (!DataValidator.validateInt(streetNumber)) {
      throw new BusinessException(ErrorsCode.HTTP_ERROR_BAD_REQUEST);
    }
    if (!DataValidator.validateInt(zipCode)) {
      throw new BusinessException(ErrorsCode.HTTP_ERROR_BAD_REQUEST);
    }
    if (!DataValidator.validateString(commune)) {
      throw new BusinessException(ErrorsCode.HTTP_ERROR_BAD_REQUEST);
    }

    try {

      this.dal.startTransaction();

      AddressDto addressDto = this.addressDao.findAddress(street, streetNumber, zipCode, commune);

      this.dal.commitTransaction();

      return addressDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new BusinessException(ErrorsCode.INTERNAL_UNKNOWN_ERROR);
    }
  }

  /**
   * Update an address already present in the database.
   *
   * @param addressDto {@link AddressDto} to change
   * @return {@link AddressDto} that has been updated in the database
   */
  @Override
  public AddressDto update(AddressDto addressDto) {

    Address address = (Address) addressDto;
    address.checkConstraints();
    System.out.println(address.getVersion());
    try {
      //this.dal.startTransaction();
      this.uof.startTransaction();

      //addressDto = this.addressDao.update(address);
      this.uof.update(address);
        
      //this.dal.commitTransaction();
      this.uof.commitTransaction();
      
      addressDto = (AddressDto) this.uof.getUpdatedObject();  
      return addressDto;
    } catch (OptimisticLockException exception) {
      //this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw exception;
    } catch (Exception exception) {
      //this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw new FatalException();
    }
  }

  @Override
  public AddressDto getByPk(int pk) {
    try {

      this.dal.startTransaction();

      AddressDto addressDto = this.addressDao.findByPk(pk);

      this.dal.commitTransaction();

      return addressDto;      
    } catch (Exception exception) {
      throw new FatalException();
    }
  }

}
