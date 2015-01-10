package com.scsociety.scjapi.interfaces;

import java.io.Serializable;
import java.util.List;

import com.scsociety.scjapi.models.Contract;;

public interface IContracts<CONTRACT> extends Serializable
{
	public List<CONTRACT> getAllContracts();
	public Contract getContractByExchangeId(String exchangeId);
}
