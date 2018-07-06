package biz;

import biz.dto.AbstractDto;
import biz.objects.BusinessFactory;
import dal.DalFrontEndServices;
import dal.dao.AddressDao;
import dal.dao.CompanyDao;
import dal.dao.ContactDao;
import dal.dao.GenericDao;
import dal.dao.JeDao;
import dal.dao.ParticipationDao;
import dal.dao.PresenceDao;
import dal.dao.UserDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

class UnitOfWorkImpl implements UnitOfWork {

  private static final String INSERT = "insert";

  private static final String UPDATE = "update";

  private static final String DELETE = "delete";

  private ThreadLocal<Integer> nbTransaction = new ThreadLocal<Integer>() {

    @Override
    protected Integer initialValue() {
      return 0;
    }

  };

  private ThreadLocal<Map<String, List<AbstractDto>>> transactions = 
      new ThreadLocal<Map<String, List<AbstractDto>>>() {

    @Override
    protected Map<String, List<AbstractDto>> initialValue() {
      Map<String, List<AbstractDto>> transaction = new HashMap<>();
      transaction.put(INSERT, new ArrayList<>());
      transaction.put(UPDATE, new ArrayList<>());
      transaction.put(DELETE, new ArrayList<>());
      return transaction;
    }

  };

  private ThreadLocal<Map<String, Queue<AbstractDto>>> objects = 
      new ThreadLocal<Map<String, Queue<AbstractDto>>>() {

    @Override
    protected Map<String, Queue<AbstractDto>> initialValue() {
      Map<String, Queue<AbstractDto>> objects = new HashMap<>();
      objects.put(INSERT, new LinkedList<>());
      objects.put(UPDATE, new LinkedList<>());
      objects.put(DELETE, new LinkedList<>());
      return objects;
    }

  };

  private DalFrontEndServices dal;

  private Map<Class<?>, GenericDao> daos = new HashMap<>();

  public UnitOfWorkImpl(DalFrontEndServices dal, AddressDao addressDao, CompanyDao companyDao,
      ContactDao contactDao, JeDao jeDao, ParticipationDao participationDao,
      PresenceDao presenceDao, UserDao userDao) {
    this.dal = dal;
    this.daos.put(BusinessFactory.getAddress().getClass(), addressDao);
    this.daos.put(BusinessFactory.getCompany().getClass(), companyDao);
    this.daos.put(BusinessFactory.getContact().getClass(), contactDao);
    this.daos.put(BusinessFactory.getJe().getClass(), jeDao);
    this.daos.put(BusinessFactory.getParticipation().getClass(), participationDao);
    this.daos.put(BusinessFactory.getPresence().getClass(), presenceDao);
    this.daos.put(BusinessFactory.getUser().getClass(), userDao);
  }

  @Override
  public void startTransaction() {
    if (this.nbTransaction.get() == 0) {
      this.objects.remove();
    }
    this.nbTransaction.set(this.nbTransaction.get() + 1);
  }

  @Override
  public void commitTransaction() {
    int nbTransaction = this.nbTransaction.get().intValue();
    if (nbTransaction == 0) {
      throw new UnsupportedOperationException();
    }
    
    this.nbTransaction.set(--nbTransaction);
    
    if (nbTransaction == 0) {

      this.dal.startTransaction();

      for (AbstractDto abstractDto : this.transactions.get().get(INSERT)) {
        this.objects.get().get(INSERT).add(daos.get(abstractDto.getClass()).insert(abstractDto));
      }

      for (AbstractDto abstractDto : this.transactions.get().get(UPDATE)) {
        this.objects.get().get(UPDATE).add(daos.get(abstractDto.getClass()).update(abstractDto));
      }

      //for (AbstractDto abstractDto : this.transactions.get().get(DELETE)) {
      //  daos.get(abstractDto.getClass()).delete(abstractDto);
      //}

      this.dal.commitTransaction();
      
      this.transactions.remove();
      
      for (GenericDao dao : this.daos.values()) {
        dao.cacheClean();
      }
    }
  }

  @Override
  public void rollbackTransaction() {
    this.dal.rollbackTransaction();
    this.transactions.remove();
    this.nbTransaction.remove();
    this.objects.remove();
    for (GenericDao dao : this.daos.values()) {
      dao.cacheClean();
    }
  }

  @Override
  public void insert(AbstractDto abstractDto) {
    this.transactions.get().get(INSERT).add((AbstractDto) abstractDto.clone());
  }

  @Override
  public void update(AbstractDto abstractDto) {
    this.transactions.get().get(UPDATE).add((AbstractDto) abstractDto.clone());
  }

  @Override
  public void delete(AbstractDto abstractDto) {
    this.transactions.get().get(DELETE).add((AbstractDto) abstractDto.clone());
  }

  @Override
  public AbstractDto getInsertedObject() {
    return this.objects.get().get(INSERT).poll();
  }

  @Override
  public AbstractDto getUpdatedObject() {
    return this.objects.get().get(UPDATE).poll();
  }

  @Override
  public AbstractDto getDeletedObject() {
    return this.objects.get().get(DELETE).poll();
  }

}