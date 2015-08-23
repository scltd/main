package com.scsociety.scjapi.interfaces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.scsociety.scjapi.models.Contract;
import com.scsociety.scjapi.models.TradeModel;

public class Test {

  public static void main(String[] args) {
    Properties p = new Properties();
    try {
      p.load((new FileInputStream(new File("/tmp/connection.properties"))));
      System.out.println("TEST");
    } catch (InvalidPropertiesFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    IContracts<Contract> ic = new ContractsImpl<Contract>(p, Contract.class);
    List<Contract> zz = ic.getAllContracts();
    Iterator<Contract> i = zz.iterator();
    while (i.hasNext()) {
      Contract ccc = i.next();
      System.out.println(ccc.getUuid());
      System.out.println(ccc.getExpiration());
    }
    ITrades<TradeModel> it = new TradesImpl<TradeModel>(p, TradeModel.class);
    it.insertTrade("PPP", "90001a59-dd5f-48b7-a994-5d0b6e8af723", "SCSTRD01", 10, 1.12, "DDDE", 1,
        true);
    it.deleteTrade(10);
  }

}
