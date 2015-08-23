package com.scsociety.scjapi.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AccountsImpl<ACCOUNT> implements IAccounts<ACCOUNT> {

  /**
	 * 
	 */
  private static final long serialVersionUID = -1348079158631528660L;
  private final IBackend backend;

  Class<ACCOUNT> reference;
  private List<ACCOUNT> account_list;

  public AccountsImpl(Properties config, Class<ACCOUNT> cr) {
    backend = PostgreSQLBackend.getInstance(config);
    reference = cr;
    account_list = new ArrayList<ACCOUNT>();
  }

  private ACCOUNT getAccountInstance() {
    try {
      return reference.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<ACCOUNT> loadAccountById(String uuid) {
    PreparedStatement pQuery = backend.getAccountByIdQuery();
    List<ACCOUNT> return_list = new ArrayList<ACCOUNT>();

    try {
      pQuery.setString(1, uuid);
      ResultSet r = backend.query(pQuery);
      while (r.next()) {

        ACCOUNT c = this.getAccountInstance();
        Method method = c.getClass().getMethod("parse", ResultSet.class);
        method.invoke(c, r);
        return_list.add(c);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return return_list;
  }

  public List<ACCOUNT> loadAccountByName(String name) {
    PreparedStatement pQuery = backend.getAccountByNameQuery();
    List<ACCOUNT> return_list = new ArrayList<ACCOUNT>();

    try {
      pQuery.setString(1, name);
      ResultSet r = backend.query(pQuery);
      while (r.next()) {

        ACCOUNT c = this.getAccountInstance();
        Method method = c.getClass().getMethod("parse", ResultSet.class);
        method.invoke(c, r);
        return_list.add(c);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return return_list;
  }

  public List<ACCOUNT> getAllAccounts() {
    PreparedStatement pQuery = backend.getAccountsQuery();
    account_list = new ArrayList<ACCOUNT>();
    try {
      ResultSet r = backend.query(pQuery);
      while (r.next()) {
        ACCOUNT c = this.getAccountInstance();
        Method method = c.getClass().getMethod("parse", ResultSet.class);
        method.invoke(c, r);
        account_list.add(c);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return account_list;
  }

  public boolean updateAccount(String uuid, Integer today, Double equity) {
    PreparedStatement pQuery = backend.getUpdateAccountQuery();
    try {
      pQuery.setInt(1, today);
      pQuery.setDouble(2, equity);
      pQuery.setString(3, uuid);
      pQuery.execute();
      backend.commit();
      return true;

    } catch (SQLException e) {
      e.printStackTrace();

    }
    return false;
  }

  public void rollback() {
    backend.rollback();
  }

}
