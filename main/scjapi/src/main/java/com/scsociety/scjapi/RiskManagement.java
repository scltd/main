package com.scsociety.scjapi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scsociety.scjapi.interfaces.AccountsImpl;
import com.scsociety.scjapi.interfaces.ContractsImpl;
import com.scsociety.scjapi.interfaces.IAccounts;
import com.scsociety.scjapi.interfaces.IContracts;
import com.scsociety.scjapi.interfaces.ITrades;
import com.scsociety.scjapi.interfaces.TradesImpl;
import com.scsociety.scjapi.models.AccountModel;
import com.scsociety.scjapi.models.Contract;
import com.scsociety.scjapi.models.TradeModel;

public class RiskManagement {
	final static Logger log = LoggerFactory.getLogger(RiskManagement.class);

	private ITrades<TradeModel> tradesInterface;
	private IAccounts<AccountModel> accountsInterface;
	private IContracts<Contract> contractsInterface;
	private RiskModel _risks;

	public RiskManagement(Properties config) {
		tradesInterface = new TradesImpl<TradeModel>(config, TradeModel.class);
		accountsInterface = new AccountsImpl<AccountModel>(config,
				AccountModel.class);
		contractsInterface = new ContractsImpl<Contract>(config, Contract.class);
		_risks = this.loadAccount(config.getProperty("target_account"));
	}

	public RiskModel loadAccount(String name) {
		RiskModel rm = new RiskModel();
		List<AccountModel> account = accountsInterface.loadAccountByName(name);
		if (account.isEmpty()) {
			log.error("Cannot load account {}", name);
			return null;
		}
		rm.set_name(name);

		Iterator<AccountModel> ait = account.iterator();
		Map<String, AccountModel> osmap1 = new HashMap<String, AccountModel>();
		Map<String, Contract> osmap = new HashMap<String, Contract>();
		while (ait.hasNext()) {
			AccountModel am = ait.next();
			Contract contract = contractsInterface.getContractById(am
					.getContract());
			if (contract == null) {
				log.error("Contract id: {} not found. Skipping it.",
						am.getContract());
				continue;
			}
			osmap1.put(contract.getUuid(), am);
			osmap.put(contract.getExchangeContractId(), contract);
		}
		rm.set_accountEntries(osmap1);
		rm.set_contracts(osmap);
		return rm;
	}

	public Boolean insertPosition(Integer client_id, String exchange_symbol_id,
			Integer size, Double price) {
		if (_risks == null) {
			log.error("Failed to initialize RiskModel.");
			return false;
		}
		Contract c = _risks.get_contracts().get(exchange_symbol_id);
		if (c == null) {
			log.error("Cannot find symbol mapped to exchange_id: {}",
					exchange_symbol_id);
			return false;
		}
		if (!tradesInterface.insertTrade(_risks.get_name(), c.getUuid(),
				"SCSTRD01", size, price, "Manualy executed", 0, true)) {
			log.error("Failed to insert trade.");
			return false;
		}
		AccountModel am = _risks.get_accountEntries().get(c.getUuid());
		if (am == null) {
			log.error("Failed to obtain accountModel: {}", c.getUuid());
			return false;
		}
		am.updateToday(size);
		am.updateEquity(size * price * c.getDollarsPerPoint() * -1);
		if (!accountsInterface.updateAccount(am.getUuid(), am.getToday(),
				am.getEquity())) {
			log.info("Failed to update account equity");
			return false;
		}
		return true;
	}

}
