package mock.dao;

import biz.dto.AbstractDto;
import biz.dto.CompanyDto;
import biz.objects.Company;
import dal.dao.CompanyDao;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mock.DataBaseMock;

public class CompanyDaoMock implements CompanyDao {

  private DataBaseMock dataBaseMock;
  
  CompanyDaoMock(DataBaseMock dataBaseMock) {
    this.dataBaseMock = dataBaseMock;
  }
  
  @Override
  public CompanyDto create(Company company) {
    return dataBaseMock.addCompany(company);
  }
  
  @Override
  public AbstractDto insert(AbstractDto abstractDto) {
    return create((Company) abstractDto);
  }

  @Override
  public CompanyDto update(Company company) {
    return dataBaseMock.updateCompany(company);
  }
  
  @Override
  public AbstractDto update(AbstractDto abstractDto) {
    return update((Company) abstractDto);
  }

  @Override
  public CompanyDto findByPk(int pk) {
    return dataBaseMock.getCompanyByPk(pk);
  }

  @Override
  public CompanyDto findByName(String name) {
    return dataBaseMock.getCompanyByName(name);
  }

  @Override
  public List<CompanyDto> findAllCompanies() {
    return dataBaseMock.getCompanies();
  }

  @Override
  public List<CompanyDto> findAllNewCompanies() {
    return dataBaseMock.getCompaniesForSelection();
  }

  @Override
  public Set<CompanyDto> search(String[] criteria) {
    return new HashSet<>(dataBaseMock.getCompanies());
  }

  @Override
  public void cacheClean() {}

}
