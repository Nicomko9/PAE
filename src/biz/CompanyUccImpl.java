package biz;

import biz.dto.CompanyDto;
import biz.dto.ParticipationDto;
import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import biz.exceptions.FatalException;
import biz.exceptions.OptimisticLockException;
import biz.objects.Company;
import dal.DalFrontEndServices;
import dal.dao.CompanyDao;
import dal.dao.ParticipationDao;
import ihm.utils.DataValidator;
import ihm.utils.Logger;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the Company's Use Case Controller.
 */
class CompanyUccImpl implements CompanyUcc {

  private DalFrontEndServices dal;

  private CompanyDao companyDao;
  
  private ParticipationDao participationDao;
  
  private UnitOfWork uof;

  CompanyUccImpl(DalFrontEndServices dal, CompanyDao companyDao,
      ParticipationDao participationDao, UnitOfWork uof) {
    this.dal = dal;
    this.companyDao = companyDao;
    this.participationDao = participationDao;
    this.uof = uof;
  }

  /**
   * Register a new company in the database.
   *
   * @param companyDto {@link CompanyDto} to persist
   * @return the companyDto with the information added in the Database
   */
  @Override
  public CompanyDto register(CompanyDto companyDto) {
    Company company = (Company) companyDto;
    company.checkConstraints();

    if (null != this.companyDao.findByName(companyDto.getCompanyName())) {
      Logger.log("AddressDaoImpl", "findByPk", "The registration of the company failed : "
          + "This company name also exists", Logger.ERROR);
      throw new BusinessException(ErrorsCode.BUSINESS_NAME_COMPANY_EXISTED);
    }

    company = (Company) companyDto;
    company.setInscriptionDate(LocalDateTime.now());

    try {
      //this.dal.startTransaction();
      this.uof.startTransaction();

      //companyDto = this.companyDao.create(company);
      this.uof.insert(company);

      //this.dal.commitTransaction();
      this.uof.commitTransaction();

      companyDto = (CompanyDto) this.uof.getInsertedObject();
      
      return companyDto;
    } catch (Exception exception) {
      //this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw new FatalException();
    }
  }

  /**
   * Update a company already present in the database.
   *
   * @param companyDto {@link CompanyDto} to persist
   * @return the information of the company updated in the database
   */
  @Override
  public CompanyDto update(CompanyDto companyDto) {

    if (!DataValidator.validateString(companyDto.getCompanyName())) {
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_COMPANY_NAME);
    }

    CompanyDto company = this.companyDao.findByName(companyDto.getCompanyName());
    if (company != null && !companyDto.equals(company)) {
      Logger.log("AddressDaoImpl", "findByPk", "The registration of the company failed : "
          + "This company name also exists", Logger.ERROR);
      throw new BusinessException(ErrorsCode.BUSINESS_NAME_COMPANY_EXISTED);
    }

    try {
      //this.dal.startTransaction();
      this.uof.startTransaction();
      
      //companyDto = this.companyDao.update((Company) companyDto);
      this.uof.update(companyDto);

      //this.dal.commitTransaction();
      this.uof.commitTransaction();
      
      companyDto = (CompanyDto) this.uof.getUpdatedObject();

      return companyDto;
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

  /**
   * Give all the Companies of the database.
   *
   * @return a {@link List} of {@link CompanyDto}
   */
  @Override
  public List<CompanyDto> getAllCompanies() {
    try {

      this.dal.startTransaction();

      List<CompanyDto> companiesDto = this.companyDao.findAllCompanies();

      this.dal.commitTransaction();

      return companiesDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }

  @Override
  public CompanyDto getCompanyByName(String name) {
    try {

      this.dal.startTransaction();

      CompanyDto companyDto = this.companyDao.findByName(name);

      this.dal.commitTransaction();

      return companyDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }

  @Override
  public CompanyDto getCompanyByPk(int pk) {
    try {

      this.dal.startTransaction();

      CompanyDto companyDto = this.companyDao.findByPk(pk);

      this.dal.commitTransaction();

      return companyDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }

  @Override
  public List<CompanyDto> getAllCompaniesForTheSelection() {
    try {
      this.dal.startTransaction();
      
      List<CompanyDto> companiesDto = this.companyDao.findAllNewCompanies();
      List<ParticipationDto> participationsDto = this.participationDao
          .findAllParticipationsForSelection();
      for (ParticipationDto participation : participationsDto) {
        companiesDto.add(participation.getCompany());
      }
      
      this.dal.commitTransaction();
      
      return companiesDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }

}
