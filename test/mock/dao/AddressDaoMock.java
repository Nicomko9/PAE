package mock.dao;

import biz.dto.AbstractDto;
import biz.dto.AddressDto;
import biz.objects.Address;
import dal.dao.AddressDao;
import mock.DataBaseMock;

class AddressDaoMock implements AddressDao {

  private DataBaseMock dataBaseMock;

  AddressDaoMock(DataBaseMock dataBaseMock) {
    this.dataBaseMock = dataBaseMock;
  }

  @Override
  public AddressDto create(Address address) {
    return dataBaseMock.addAddress(address);
  }
  
  @Override
  public AddressDto insert(AbstractDto abstractDto) {
    return create((Address) abstractDto);
  }

  @Override
  public AddressDto findAddress(String street, int streetNumber, int zipCode, String commune) {
    return dataBaseMock.getAddressByField(street, streetNumber, zipCode, commune);
  }

  @Override
  public AddressDto findByPk(int pk) {
    return dataBaseMock.getAddressByPk(pk);
  }

  @Override
  public AddressDto update(Address address) {
    return dataBaseMock.updateAddress(address);
  }
  
  @Override
  public AddressDto update(AbstractDto abstractDto) {
    return dataBaseMock.updateAddress((Address) abstractDto);
  }

  @Override
  public void cacheClean() {}
}
