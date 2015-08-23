package com.scsociety.scjapi.interfaces;

import java.io.Serializable;
import java.util.List;

public interface IAccounts<ACCOUNT> extends Serializable {
  public List<ACCOUNT> loadAccountById(String uuid);

  public List<ACCOUNT> loadAccountByName(String name);

  public List<ACCOUNT> getAllAccounts();

  public boolean updateAccount(String uuid, Integer today, Double equity);

  public void rollback();
}
