package mock.ucc;

import biz.AddressUcc;
import biz.dto.AddressDto;
import biz.objects.Address;
import dal.dao.AddressDao;

public class AddressUccMock implements AddressUcc {

  private AddressDao addressDao;

  AddressUccMock(AddressDao addressDao) {
    this.addressDao = addressDao;
  }

  @Override
  public AddressDto register(AddressDto biz) {
    return addressDao.create((Address) biz);
  }

  @Override
  public AddressDto getAddress(String street, int streetNumber, int zipCode, String commune) {
    return addressDao.findAddress(street, streetNumber, zipCode, commune);
  }

  @Override
  public AddressDto update(AddressDto addressDto) {
    return addressDao.update((Address) addressDto);
  }

  @Override
  public AddressDto getByPk(int pk) {
    // TODO Auto-generated method stub
    return null;
  }

}
