package com.scsociety.scjapi;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scsociety.scjapi.interfaces.ContractsImpl;
import com.scsociety.scjapi.interfaces.IContracts;
import com.scsociety.scjapi.models.Contract;

public class ContractsManagement {
	final static Logger log = LoggerFactory
			.getLogger(ContractsManagement.class);
	private IContracts<Contract> contractsInterface;

	public ContractsManagement(Properties config) {
		contractsInterface = new ContractsImpl<Contract>(config, Contract.class);
	}

	List<Contract> loadContracts() {
		List<Contract> c = contractsInterface.getAllContracts();
		return c;
	}
}
