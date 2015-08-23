package com.scsociety.scjapi.models;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Contract implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 4002302066369620882L;
  private String uuid;
  private String symbol_name;
  private Integer type;
  private Currency currency;
  private Exchange exchange;
  private Double dollars_per_point;
  private Integer deliverable_size;
  private Double strike;
  private String put_call_code;
  private Date expiration;
  private Integer version;
  private String exchange_contract_id;

  public void parse(ResultSet r) {
    try {
      this.setUuid(r.getString("uuid"));
      this.setSymbol_name(r.getString("symbol_name"));
      this.setType(r.getInt("type"));
      this.setCurrency(new Currency(r.getString("currency")));
      this.setExchange(new Exchange(r.getInt("exchange")));
      this.setDollarsPerPoint(r.getDouble("dollarsperpoint"));
      this.setDeliverableSize(r.getInt("deliverablesize"));
      this.setStrike(r.getDouble("strike"));
      this.setPutCallCode(r.getString("pccode"));
      this.setExpiration(r.getDate("expiration"));
      this.setVersion(r.getInt("version"));
      this.setExchangeContractId(r.getString("exchange_contract_id"));;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public Contract() {

  }

  public final String getUuid() {
    return uuid;
  }

  public final void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public final String getSymbol_name() {
    return symbol_name;
  }

  public final void setSymbol_name(String symbol_name) {
    this.symbol_name = symbol_name;
  }

  public final Integer getType() {
    return type;
  }

  public final void setType(Integer type) {
    this.type = type;
  }

  public final Currency getCurrency() {
    return currency;
  }

  public final void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public final Exchange getExchange() {
    return exchange;
  }

  public final void setExchange(Exchange exchange) {
    this.exchange = exchange;
  }

  public final Double getDollarsPerPoint() {
    return dollars_per_point;
  }

  public final void setDollarsPerPoint(Double dollars_per_point) {
    this.dollars_per_point = dollars_per_point;
  }

  public final Integer getDeliverableSize() {
    return deliverable_size;
  }

  public final void setDeliverableSize(Integer deliverable_size) {
    this.deliverable_size = deliverable_size;
  }

  public final Double getStrike() {
    return strike;
  }

  public final void setStrike(Double strike) {
    this.strike = strike;
  }

  public final String getPutCallCode() {
    return put_call_code;
  }

  public final void setPutCallCode(String put_call_code) {
    this.put_call_code = put_call_code;
  }

  public final Date getExpiration() {
    return expiration;
  }

  public final void setExpiration(Date expiration) {
    this.expiration = expiration;
  }

  public final Integer getVersion() {
    return version;
  }

  public final void setVersion(Integer version) {
    this.version = version;
  }

  public final String getExchangeContractId() {
    return exchange_contract_id;
  }

  public final void setExchangeContractId(String exchange_contract_id) {
    this.exchange_contract_id = exchange_contract_id;
  }

}
