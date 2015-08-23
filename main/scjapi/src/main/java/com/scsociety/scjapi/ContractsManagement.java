package com.scsociety.scjapi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scsociety.scjapi.exceptions.ContractNotFoundException;
import com.scsociety.scjapi.interfaces.ContractsImpl;
import com.scsociety.scjapi.interfaces.IContracts;
import com.scsociety.scjapi.models.Contract;

public class ContractsManagement {
  final static Logger log = LoggerFactory.getLogger(ContractsManagement.class);

  private IContracts<Contract> _contractsInterface;
  private List<Contract> _contractsList;
  private Map<String, Contract> _contracts;

  public ContractsManagement(Properties config) {
    _contractsInterface = new ContractsImpl<Contract>(config, Contract.class);
    _contracts = new HashMap<String, Contract>();
  }

  public void loadContracts() {
    log.info("Loading contracts from the backend.");
    _contractsList = _contractsInterface.getAllContracts();
    Iterator<Contract> c = _contractsList.iterator();
    while (c.hasNext()) {
      Contract contract = c.next();
      log.info("Found contract: {} - {}", contract.getUuid(), contract.getSymbol_name());
      _contracts.put(contract.getUuid(), contract);
    }
  }

  public Contract getContract(String uuid) throws ContractNotFoundException {
    Contract c = _contracts.get(uuid);
    if (c == null)
      throw new ContractNotFoundException();
    return c;
  }
}
