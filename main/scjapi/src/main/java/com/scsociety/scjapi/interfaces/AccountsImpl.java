package com.scsociety.scjapi.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AccountsImpl<ACCOUNT> implements IAccounts<ACCOUNT> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1348079158631528660L;
	private final IBackend backend;

	Class<ACCOUNT> reference;
	private List<ACCOUNT> account_list;

	public AccountsImpl(Properties config, Class<ACCOUNT> cr) {
		backend = PostgreSQLBackend.getInstance(config);
		reference = cr;
		account_list = new ArrayList<ACCOUNT>();
	}

	private ACCOUNT getAccountInstance() {
		try {
			return reference.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ACCOUNT loadAccountById(String uuid) {
		PreparedStatement pQuery = backend.getAccountByIdQuery();
		try {
			pQuery.setString(1, uuid);
			ResultSet r = backend.query(pQuery);

			ACCOUNT c = this.getAccountInstance();
			Method method = c.getClass().getMethod("parse", ResultSet.class);
			method.invoke(c, r);
			return c;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ACCOUNT loadAccountByName(String name) {
		PreparedStatement pQuery = backend.getAccountByNameQuery();
		try {
			pQuery.setString(1, name);
			ResultSet r = backend.query(pQuery);

			ACCOUNT c = this.getAccountInstance();
			Method method = c.getClass().getMethod("parse", ResultSet.class);
			method.invoke(c, r);
			return c;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<ACCOUNT> getAllAccounts() {
		PreparedStatement pQuery = backend.getAccountsQuery();
		try {
			ResultSet r = backend.query(pQuery);
			List<ACCOUNT> rv = new ArrayList<ACCOUNT>();
			while (r.next())
			{
			ACCOUNT c = this.getAccountInstance();
			Method method = c.getClass().getMethod("parse", ResultSet.class);
			method.invoke(c, r);
			rv.add(c);
			}
			return rv;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean updateAccount(ACCOUNT account) {
		// TODO Auto-generated method stub
		return false;
	}

	public void rollback() {
		// TODO Auto-generated method stub

	}

}
