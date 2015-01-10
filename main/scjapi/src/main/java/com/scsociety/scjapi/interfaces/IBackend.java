package com.scsociety.scjapi.interfaces;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public interface IBackend extends Serializable
{
	public Boolean connect();
	public Boolean ping();
	public Boolean intialize(Properties config);
	public Boolean disconnect();
	public void commit();
	public PreparedStatement getContractsQuery();
	public PreparedStatement getContractByIdQuery();
	public PreparedStatement getContractByEidQuery();
	public PreparedStatement getTradesQuery();
	public PreparedStatement getAccountsQuery();
	public PreparedStatement getInsertTradeQuery();
	public PreparedStatement getDeleteTradeQuery();
	public ResultSet query(PreparedStatement p);	
}
