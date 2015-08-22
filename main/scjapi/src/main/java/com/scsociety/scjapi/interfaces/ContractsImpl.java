package com.scsociety.scjapi.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ContractsImpl<CONTRACT> implements IContracts<CONTRACT> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5678342168357808503L;
	private final IBackend backend;

	Class<CONTRACT> reference;
	private List<CONTRACT> contract_list;

	public ContractsImpl(Properties config, Class<CONTRACT> cr) {
		backend = PostgreSQLBackend.getInstance(config);
		reference = cr;
		contract_list = new ArrayList<CONTRACT>();
	}

	private CONTRACT getContractInstance() {
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

	public List<CONTRACT> getAllContracts() {
		contract_list = new ArrayList<CONTRACT>();
		PreparedStatement pQuery = backend.getContractsQuery();
		try {
			ResultSet r = backend.query(pQuery);
			if (r != null)
			while (r.next()) {
				CONTRACT c = this.getContractInstance();
				Method method = c.getClass()
						.getMethod("parse", ResultSet.class);
				method.invoke(c, r);
				contract_list.add(c);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contract_list;
	}

	public CONTRACT getContractByExchangeId(String exchangeId) {
		PreparedStatement pQuery = backend.getContractByEidQuery();
		try {
			pQuery.setString(1, exchangeId);
			ResultSet r = backend.query(pQuery);
			if (r.next()) {
				CONTRACT c = this.getContractInstance();
				Method method = c.getClass()
						.getMethod("parse", ResultSet.class);
				method.invoke(c, r);
				return c;
			}
			return null;
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

	public CONTRACT getContractById(String id) {
		PreparedStatement pQuery = backend.getContractByIdQuery();
		try {
			pQuery.setString(1, id);
			ResultSet r = backend.query(pQuery);
			if (r.next()) {
				CONTRACT c = this.getContractInstance();
				Method method = c.getClass()
						.getMethod("parse", ResultSet.class);
				method.invoke(c, r);
				return c;
			}
			// return null;
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

}
