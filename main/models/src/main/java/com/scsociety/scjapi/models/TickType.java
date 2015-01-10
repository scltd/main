package com.scsociety.scjapi.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TickType implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7205141802002667923L;
	private Integer id;
	private Double min;
	private Double max;
	private Double change;
	public TickType()
	{
		
	}
	public void parse(ResultSet r)
	{
		try {
			this.setId(r.getInt("id"));
			this.setMin(r.getDouble("min"));
			this.setMax(r.getDouble("max"));
			this.setChange(r.getDouble("change"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public final Integer getId() {
		return id;
	}
	public final void setId(Integer id) {
		this.id = id;
	}
	public final Double getMin() {
		return min;
	}
	public final void setMin(Double min) {
		this.min = min;
	}
	public final Double getMax() {
		return max;
	}
	public final void setMax(Double max) {
		this.max = max;
	}
	public final Double getChange() {
		return change;
	}
	public final void setChange(Double change) {
		this.change = change;
	}
	
	
}
