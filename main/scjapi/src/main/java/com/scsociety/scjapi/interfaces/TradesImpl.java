package com.scsociety.scjapi.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class TradesImpl<TRADE> implements ITrades<TRADE> {

  /**
	 * 
	 */
  private static final long serialVersionUID = 7675635261141485227L;
  private final IBackend backend;
  Class<TRADE> reference;

  public TradesImpl(Properties config, Class<TRADE> cr) {
    backend = PostgreSQLBackend.getInstance(config);
    reference = cr;
  }

  private TRADE getTradeInstance() {
    try {
      return reference.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean insertTrade(String account, String contract, String trader, Integer size,
      Double price, String desc, Integer type, boolean commit) {
    PreparedStatement prepQuery = backend.getInsertTradeQuery();
    if (prepQuery != null)
      try {

        prepQuery.setString(1, account);
        prepQuery.setString(2, contract);
        prepQuery.setString(3, trader);
        prepQuery.setInt(4, size);
        prepQuery.setDouble(5, price);
        prepQuery.setString(6, desc);
        prepQuery.setInt(7, type);
        prepQuery.execute();
        if (commit)
          backend.commit();
        return true;
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    return false;
  }

  public boolean updateTrade(String account, String contract, String trader, Integer size,
      Double price, String desc, Integer type, Integer tradeno) {
    // TODO Auto-generated method stub
    return false;

  }

  public boolean deleteTrade(Integer tradeNo) {
    PreparedStatement prepQuery = backend.getDeleteTradeQuery();
    if (prepQuery != null)
      try {
        prepQuery.setInt(1, tradeNo);
        prepQuery.execute();
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

  public void commit() {
    backend.commit();
  }

}
