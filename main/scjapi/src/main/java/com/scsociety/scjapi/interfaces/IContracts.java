package com.scsociety.scjapi.interfaces;

import java.io.Serializable;
import java.util.List;

public interface IContracts<CONTRACT> extends Serializable {
  public List<CONTRACT> getAllContracts();

  // public List<CONTRACT> getContractsBySearchCriteria(ContractSearchCriteria csc);
  public CONTRACT getContractById(String exchangeId);

  public CONTRACT getContractByExchangeId(String exchangeId);
}
