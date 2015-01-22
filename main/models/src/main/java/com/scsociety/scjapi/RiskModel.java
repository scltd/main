package com.scsociety.scjapi;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.scsociety.scjapi.models.AccountModel;
import com.scsociety.scjapi.models.Contract;
import com.scsociety.scjapi.models.TradeModel;

public class RiskModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 783422214244864593L;
	private Map<String, AccountModel> _accountEntries;
	private Map<String, Contract> _contracts;
	private List<TradeModel> _trades;
	private String _name;

	public final String get_name() {
		return _name;
	}

	public final void set_name(String _name) {
		this._name = _name;
	}

	public final Map<String, AccountModel> get_accountEntries() {
		return _accountEntries;
	}

	public final void set_accountEntries(
			Map<String, AccountModel> _accountEntries) {
		this._accountEntries = _accountEntries;
	}

	public final Map<String, Contract> get_contracts() {
		return _contracts;
	}

	public final void set_contracts(Map<String, Contract> _contracts) {
		this._contracts = _contracts;
	}

	public final List<TradeModel> get_trades() {
		return _trades;
	}

	public final void set_trades(List<TradeModel> _trades) {
		this._trades = _trades;
	}

}
