package com.scsociety.scjapi.interfaces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.scsociety.scjapi.models.Contract;

public class ContractsImpl<CONTRACT> implements IContracts<CONTRACT> 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5678342168357808503L;
	private final IBackend backend;
	Class<CONTRACT> reference;
	public ContractsImpl(Properties config,Class<CONTRACT> cr) 
	{
		backend = PostgreSQLBackend.getInstance(config);
		System.out.println(backend);
		reference = cr;
		System.out.println(PostgreSQLBackend.getInstance(config));
	}
	private CONTRACT getContractInstance()
	{
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
	public List<CONTRACT> getAllContracts() 
	{
		PreparedStatement z = backend.getContractQuery();
		List<CONTRACT> a = new ArrayList<CONTRACT>();

		try {
			z.setString(1, "472a8aa2-68ab-4034-9098-336c9bdc140c");
			ResultSet r = backend.query(z);
			while (r.next())
			{
				CONTRACT c = this.getContractInstance();
				Method method = c.getClass().getMethod("parse", ResultSet.class);
				method.invoke(c, r);
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
		
		return a;
	}

	public Contract getContractByExchangeId(String exchangeId) 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
