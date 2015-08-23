package com.scsociety.scjapi.interfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.scsociety.scjapi.misc.SQLQueries;

public class PostgreSQLBackend implements IBackend {
	/**
	 * 
	 */

	private static final long serialVersionUID = 4263230852317084405L;
	private Connection connection;
	private Properties configProps;

	private static PostgreSQLBackend instance = null;

	protected PostgreSQLBackend(Properties config) {
		this.intialize(config);
	}

	public static PostgreSQLBackend getInstance(Properties config) {
		if (instance == null) {
			instance = new PostgreSQLBackend(config);
		}
		return instance;
	}

	public Boolean connect() {
		if (configProps.isEmpty()) {
			System.out
					.println("Backend hasn't been initialized. Please initialize it first");
			return false;
		}
		try {
			String url = "jdbc:postgresql://sql.scsociety.com/scltd";
			String user = "scs";
			String password = "%0l1d%t4t3%0c13ty";

			connection = DriverManager.getConnection(url,user,password);
			connection.setAutoCommit(false);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean ping() {
		try {
			return (this.connection.isClosed() ? false : true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean intialize(Properties config) {
		configProps = config;
		return this.connect();
	}

	public Boolean disconnect() {
		if (this.ping()) {
			try {
				this.connection.close();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return true;
		}
		return false;
	}

	public PreparedStatement getContractsQuery() {
		try {
			if (connection != null)
				return connection.prepareStatement(SQLQueries.ALL_CONTRACTS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public PreparedStatement getContractByIdQuery() {
		try {
			return connection.prepareStatement(SQLQueries.CONTRACT_BY_UUID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public PreparedStatement getContractByEidQuery() {
		try {
			return connection
					.prepareStatement(SQLQueries.CONTRACT_BY_EXCHANGE_ID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public PreparedStatement getTradesQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public PreparedStatement getAccountsQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public PreparedStatement getInsertTradeQuery() {
		try {
			if (connection != null)
				return connection.prepareStatement(SQLQueries.INSERT_TRADE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PreparedStatement getDeleteTradeQuery() {
		try {
			if (connection != null)
				return connection.prepareStatement(SQLQueries.DELETE_TRADE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet query(PreparedStatement p) {

		try {
			if (p != null)
				return p.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void rollback() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public PreparedStatement getAccountByIdQuery() {
		try {
			return connection.prepareStatement(SQLQueries.ACCOUNT_BY_UUID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PreparedStatement getAccountByNameQuery() {
		try {
			return connection.prepareStatement(SQLQueries.ACCOUNT_BY_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PreparedStatement getUpdateAccountQuery() {
		try {
			return connection.prepareStatement(SQLQueries.UPDATE_ACCOUNT);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
