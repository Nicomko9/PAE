package mock.ucc;

import java.util.List;

import biz.CompanyUcc;
import biz.dto.CompanyDto;
import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import biz.objects.Company;
import dal.dao.CompanyDao;

class CompanyUccMock implements CompanyUcc {

  private CompanyDao companyDao;

  public CompanyUccMock(CompanyDao companyDao) {
    this.companyDao = companyDao;
  }

  @Override
  public CompanyDto register(CompanyDto biz) {
    if(companyDao.findByName(biz.getCompanyName()) != null) {
      throw new BusinessException(ErrorsCode.BUSINESS_NAME_COMPANY_EXISTED);
    }
    return companyDao.create((Company) biz);
  }

  @Override
  public CompanyDto update(CompanyDto biz) {
    return companyDao.update((Company) biz);
  }

  @Override
  public List<CompanyDto> getAllCompanies() {
    return companyDao.findAllCompanies();
  }

  @Override
  public CompanyDto getCompanyByName(String name) {
    return companyDao.findByName(name);
  }

  @Override
  public List<CompanyDto> getAllCompaniesForTheSelection() {
    return companyDao.findAllNewCompanies();
  }

  @Override
  public CompanyDto getCompanyByPk(int pk) {
    return companyDao.findByPk(pk);
  }

}
