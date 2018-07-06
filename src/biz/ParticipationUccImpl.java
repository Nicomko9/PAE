package biz;

import biz.dto.CompanyDto;
import biz.dto.ContactDto;
import biz.dto.JeDto;
import biz.dto.ParticipationDto;
import biz.dto.PresenceDto;
import biz.exceptions.BusinessErrorException;
import biz.exceptions.BusinessException;
import biz.exceptions.ErrorsCode;
import biz.exceptions.FatalException;
import biz.exceptions.OptimisticLockException;
import biz.objects.Company;
import biz.objects.Contact;
import biz.objects.Je;
import biz.objects.Participation;
import biz.objects.Presence;
import dal.DalFrontEndServices;
import dal.dao.CompanyDao;
import dal.dao.ContactDao;
import dal.dao.ParticipationDao;
import dal.dao.PresenceDao;
import ihm.utils.Logger;
import java.util.ArrayList;
import java.util.List;

class ParticipationUccImpl implements ParticipationUcc {

  private DalFrontEndServices dal;

  private ParticipationDao participationDao;

  private CompanyDao companyDao;

  private UnitOfWork uof;

  private ContactDao contactsDao;

  private PresenceDao presenceDao;

  ParticipationUccImpl(DalFrontEndServices dal, ParticipationDao participationDao,
      CompanyDao companyDao, ContactDao contactDao, PresenceDao presenceDao, UnitOfWork uof) {
    this.dal = dal;
    this.participationDao = participationDao;
    this.companyDao = companyDao;
    this.presenceDao = presenceDao;
    this.contactsDao = contactDao;
    this.uof = uof;
  }

  private ParticipationDto update(ParticipationDto participationDto) {
    Participation participation = (Participation) participationDto;
    participation.checkConstraints();

    try {
      // this.dal.startTransaction();
      this.uof.startTransaction();

      // participationDto = this.participationDao.update(participation);
      this.uof.update(participation);

      // this.dal.commitTransaction();
      this.uof.commitTransaction();

      participationDto = (ParticipationDto) this.uof.getUpdatedObject();

      return participationDto;
    } catch (OptimisticLockException exception) {
      this.uof.rollbackTransaction();
      Logger.log("ParticipationUcc", "Rollback", "Conflit de version", Logger.ERROR);
      throw exception;
    } catch (Exception exception) {
      // this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      Logger.log("ParticipationUcc", "update", "Error : " + exception.getMessage(), Logger.ERROR);
      throw new FatalException();
    }
  }

  @Override
  public ParticipationDto invite(ParticipationDto participationDto) {

    if (!participationDto.getState().equals("invitée")) {
      Logger.log("ParticipationDaoImpl", "invite", "The state of the participation is incorrect",
          Logger.ERROR);
      throw new BusinessErrorException(ErrorsCode.BUSINESS_INVALID_PARTICIPATION_STATE);
    }

    Participation participation = (Participation) participationDto;
    participation.checkConstraints();

    try {
      // this.dal.startTransaction();$
      this.uof.startTransaction();

      // participationDto = this.participationDao.create(participation);
      this.uof.insert(participation);

      // this.dal.commitTransaction();
      this.uof.commitTransaction();

      participationDto = (ParticipationDto) this.uof.getInsertedObject();

      return participationDto;
    } catch (Exception exception) {
      // this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw new FatalException();
    }
  }

  @Override
  public List<ParticipationDto> inviteCompanies(List<ParticipationDto> participationsToInvite) {

    List<ParticipationDto> participationsInvited = new ArrayList<ParticipationDto>();

    for (ParticipationDto participationDto : participationsToInvite) {
      Participation participation = (Participation) participationDto;
      participation.checkConstraints();
    }

    if (participationsToInvite.stream().anyMatch(p -> !p.getState().equals("invitée"))) {
      Logger.log("ParticipationDaoImpl", "inviteCompanies",
          "The state of a participation is incorrect", Logger.ERROR);
      throw new BusinessException(ErrorsCode.BUSINESS_INVALID_PARTICIPATION_STATE);
    }

    try {
      this.uof.startTransaction();

      for (int idx = 0; idx < participationsToInvite.size(); idx++) {
        Participation participation = (Participation) participationsToInvite.get(idx);

        this.uof.insert(participation);
      }

      this.uof.commitTransaction();

      for (int i = 0; i < participationsToInvite.size(); i++) {
        participationsInvited.add((ParticipationDto) this.uof.getInsertedObject());
      }

      return participationsInvited;
    } catch (OptimisticLockException exception) {
      this.uof.rollbackTransaction();
      Logger.log("ParticipationUcc", "Rollback", "Conflit de version", Logger.ERROR);
      throw exception;
    } catch (Exception exception) {
      this.uof.rollbackTransaction();
      throw new FatalException();
    }
  }

  @Override
  public ParticipationDto confirm(ParticipationDto participationDto) {

    Participation participation = (Participation) participationDto;
    participation.checkConstraints();

    if (!participation.confirm()) {
      throw new BusinessErrorException(ErrorsCode.BUSINESS_INVALID_PARTICIPATION_STATE);
    }

    try {
      this.uof.startTransaction();

      this.uof.update(participation);

      Company company = (Company) participationDto.getCompany();
      company.setLastParticipationYear(participationDto.getJe().getDayYear());
      this.uof.update(company);

      this.uof.commitTransaction();

      participationDto = (ParticipationDto) this.uof.getUpdatedObject();

      company = (Company) this.uof.getUpdatedObject();

      participationDto.setCompany(company);

      return participationDto;
    } catch (OptimisticLockException exception) {
      this.uof.rollbackTransaction();
      Logger.log("ParticipationUcc", "Update", "Conflit de version", Logger.ERROR);
      throw exception;
    } catch (Exception exception) {
      Logger.log("ParticipationUcc", "confirm", "Exception " + exception.getMessage(),
          Logger.ERROR);
      // this.dal.rollbackTransaction();
      this.uof.rollbackTransaction();
      throw new FatalException();
    }
  }

  @Override
  public ParticipationDto returnToInvite(ParticipationDto participationDto) {
    Participation participation = (Participation) participationDto;
    if (!participation.refuse()) {
      throw new BusinessErrorException(ErrorsCode.BUSINESS_INVALID_PARTICIPATION_STATE);
    }
    return update(participationDto);
  }

  @Override
  public ParticipationDto refuse(ParticipationDto participationDto) {
    Participation participation = (Participation) participationDto;
    if (!participation.refuse()) {
      throw new BusinessErrorException(ErrorsCode.BUSINESS_INVALID_PARTICIPATION_STATE);
    }
    return update(participationDto);
  }

  @Override
  public ParticipationDto invoice(ParticipationDto participationDto) {
    Participation participation = (Participation) participationDto;
    if (!participation.invoice()) {
      throw new BusinessErrorException(ErrorsCode.BUSINESS_INVALID_PARTICIPATION_STATE);
    }
    return update(participationDto);
  }

  @Override
  public ParticipationDto pay(ParticipationDto participationDto) {
    Participation participation = (Participation) participationDto;
    if (!participation.pay()) {
      throw new BusinessErrorException(ErrorsCode.BUSINESS_INVALID_PARTICIPATION_STATE);
    }
    return update(participationDto);
  }

  @Override
  public ParticipationDto cancel(ParticipationDto participationDto) {
    Participation participation = (Participation) participationDto;
    participation.cancel();
    return update(participationDto);
  }

  @Override
  public List<ParticipationDto> getAllParticipationsOf(JeDto jeDto) {
    Je je = (Je) jeDto;
    je.checkConstraints();

    try {
      this.dal.startTransaction();

      List<ParticipationDto> participationsDto = participationDao.findByYear(je.getDayYear());

      for (ParticipationDto dto : participationsDto) {
        List<ContactDto> list = contactsDao.findContactsForCompany(dto.getCompany().getPk());

        for (ContactDto contact : list) {
          PresenceDto presence =
              this.presenceDao.findForJe(jeDto.getPk(), dto.getCompany().getPk(), contact.getPk());
          if (presence != null) {
            contact.addPresence((Presence) presence, false);
          }
          dto.addContact((Contact) contact);
        }
      }

      this.dal.commitTransaction();

      return participationsDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw exception;
    }
  }

  @Override
  public List<ParticipationDto> getAllParticipationsOf(CompanyDto companyDto) {
    Company company = (Company) companyDto;

    try {
      this.dal.startTransaction();

      List<ParticipationDto> participationsDto =
          participationDao.findByCompanyNumber(company.getPk());

      this.dal.commitTransaction();

      return participationsDto;
    } catch (Exception exception) {
      this.dal.rollbackTransaction();
      throw new FatalException();
    }
  }


  public ParticipationDto rollback(ParticipationDto participationDto) {
    try {
      this.uof.startTransaction();

      Participation participation = (Participation) participationDto;
      participation.rollback();

      this.uof.update(participation);

      this.uof.commitTransaction();

      return (ParticipationDto) this.uof.getUpdatedObject();
    } catch (OptimisticLockException exception) {
      this.uof.rollbackTransaction();
      Logger.log("ParticipationUcc", "Rollback", "Conflit de version", Logger.ERROR);
      throw exception;
    } catch (Exception exception) {
      this.uof.rollbackTransaction();
      Logger.log("ParticipationUcc", "Rollback", "Exception " + exception.getMessage(),
          Logger.ERROR);
      throw new FatalException();
    }
  }

}
