package com.scsociety.scjapi.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class TradeModel implements Serializable
{

	/**
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = -7510112493340576228L;
	private String account;
	private Contract contract;
	private String trader;
	private Integer size;
	private Double price;
	private String description;
	private Integer type;
	private Integer tradeNo;
	private Timestamp timestamp;
	
	TradeModel()
	{
		
	}
	TradeModel(TradeModel t)
	{
		this.setAccount(t.getAccount());
		this.setContract(t.getContract());
		this.setTrader(t.getTrader());
		this.setSize(t.getSize());
		this.setPrice(t.getPrice());
		this.setDescription(t.getDescription());
		this.setType(t.getType());
		this.setTradeNo(t.getTradeNo());
		this.setTimestamp(t.getTimestamp());
	}
	public final String getAccount() {
		return account;
	}
	public final void setAccount(String account) {
		this.account = account;
	}
	public final Contract getContract() {
		return contract;
	}
	public final void setContract(Contract contract) {
		this.contract = contract;
	}
	public final String getTrader() {
		return trader;
	}
	public final void setTrader(String trader) {
		this.trader = trader;
	}
	public final Integer getSize() {
		return size;
	}
	public final void setSize(Integer size) {
		this.size = size;
	}
	public final Double getPrice() {
		return price;
	}
	public final void setPrice(Double price) {
		this.price = price;
	}
	public final String getDescription() {
		return description;
	}
	public final void setDescription(String description) {
		this.description = description;
	}
	public final Integer getType() {
		return type;
	}
	public final void setType(Integer type) {
		this.type = type;
	}
	public final Integer getTradeNo() {
		return tradeNo;
	}
	public final void setTradeNo(Integer tradeNo) {
		this.tradeNo = tradeNo;
	}
	public final Timestamp getTimestamp() {
		return timestamp;
	}
	public final void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
