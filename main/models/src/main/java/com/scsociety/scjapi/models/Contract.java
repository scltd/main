package com.scsociety.scjapi.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Contract implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4002302066369620882L;
	public void parse(ResultSet r)
	{
		try {
			System.out.println(r.getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Contract()
	{
		
	}
}
