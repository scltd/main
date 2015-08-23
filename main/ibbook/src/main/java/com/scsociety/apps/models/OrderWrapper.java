package com.scsociety.apps.models;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

import com.ib.client.Order;
import com.scsociety.apps.network.OrderEntryProto;
import com.scsociety.scjapi.models.Contract;

public class OrderWrapper implements Serializable {
  public enum State {
    INSERT, INSERT_ACK, MODIFY, MODIFY_ACK, CANCEL, PENDING_MODIFY
  }

  private static final long serialVersionUID = -8014999918702142575L;
  private OrderEntryProto.Order _orderWire;
  private Order _externalOrder;
  private com.ib.client.Contract _externalContract;
  private String _contractId;
  private int _id;
  private State _state;
  private ChannelHandlerContext _context;

  public ChannelHandlerContext get_context() {
    return _context;
  }

  public void set_context(ChannelHandlerContext _context) {
    this._context = _context;
  }

  public OrderWrapper(OrderEntryProto.Order o, ChannelHandlerContext ctx) {
    _state = State.INSERT;
    _orderWire = o;
    _externalOrder = new com.ib.client.Order();
    _externalOrder.m_action = o.getQty() > 0 ? "BUY" : "SELL";
    _externalOrder.m_totalQuantity = Math.abs(o.getQty());
    _externalOrder.m_clientId = 10;
    _externalOrder.m_orderType = "LMT";
    _externalOrder.m_lmtPrice = Double.parseDouble(o.getPrice());
    _externalOrder.m_tif = "DAY";
    _externalOrder.m_transmit = true;
    _context = ctx;
  }

  public OrderEntryProto.Order get_orderWire() {
    return _orderWire;
  }

  public void setAck() {
    if (_state == State.INSERT)
      _state = State.INSERT_ACK;
    else if (_state == State.MODIFY)
      _state = State.MODIFY_ACK;

  }

  public boolean isAcked() {
    return _state == State.INSERT_ACK || _state == State.MODIFY_ACK ? true : false;
  }

  public void set_orderWire(OrderEntryProto.Order _orderWire) {
    this._orderWire = _orderWire;
    _externalOrder.m_action = _orderWire.getQty() > 0 ? "BUY" : "SELL";
    _externalOrder.m_totalQuantity = Math.abs(_orderWire.getQty());
    _externalOrder.m_clientId = 10;
    _externalOrder.m_lmtPrice = Double.parseDouble(_orderWire.getPrice());
  }

  public Order get_externalOrder() {
    return _externalOrder;
  }

  public void set_externalOrder(Order _externalOrder) {
    this._externalOrder = _externalOrder;
  }

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public State get_state() {
    return _state;
  }

  public void set_state(State _state) {
    this._state = _state;
  }

  public com.ib.client.Contract get_externalContract() {
    return _externalContract;
  }

  public String get_contractId() {
    return _contractId;
  }

  public void set_externalContract(Contract contract) {
    _contractId = contract.getUuid();
    _externalContract = new com.ib.client.Contract();
    _externalContract.m_conId = Integer.parseInt(contract.getExchangeContractId());
    _externalContract.m_exchange = "IBIS"; // c.getExchange().getName();
    _externalContract.m_symbol = contract.getSymbol_name();
    _externalContract.m_currency = "EUR";// c.getCurrency();
    _externalContract.m_primaryExch = "IBIS";// c.getExchange().getName();
    _externalContract.m_secType = contract.getType() == 0 ? "STK" : "FUT";
  }
}
