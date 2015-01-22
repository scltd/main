package com.scsociety.scjapi.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3641201361968205986L;
	private String uuid;
	private String name;
	private String contract;
	private Integer today;
	private Integer yesterday;
	private Double equity;

	public void parse(ResultSet r) {
		try {
			this.setUuid(r.getString("uuid"));
			this.setName(r.getString("name"));
			this.setContract(r.getString("contract"));
			this.setToday(r.getInt("today"));
			this.setYesterday(r.getInt("yesterday"));
			this.setEquity(r.getDouble("equity"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public final String getUuid() {
		return uuid;
	}

	public final void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getContract() {
		return contract;
	}

	public final void setContract(String contract) {
		this.contract = contract;
	}

	public final Integer getToday() {
		return today;
	}

	public final void setToday(Integer today) {
		this.today = today;
	}

	public final Integer getYesterday() {
		return yesterday;
	}

	public final void setYesterday(Integer yesterday) {
		this.yesterday = yesterday;
	}

	public final Double getEquity() {
		return equity;
	}

	public final void updateEquity(Double equity) {
		this.equity += equity;
	}

	public final void updateToday(Integer position) {
		this.today += position;
	}

	public final void setEquity(Double equity) {
		this.equity = equity;
	}

}
