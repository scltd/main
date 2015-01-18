package com.scsociety.scjapi.interfaces;

import java.io.Serializable;
import java.util.List;

public interface IAccounts<ACCOUNT> extends Serializable
{
	public ACCOUNT loadAccountById(String uuid);
	public ACCOUNT loadAccountByName(String name);
	public List<ACCOUNT> getAllAccounts();
	public boolean updateAccount(ACCOUNT account);
	public void rollback();
}
