package com.scsociety.scjapi.interfaces;

import java.io.Serializable;

public interface ITrades<TRADE> extends Serializable {
	public boolean deleteTrade(Integer tradeNo);
	public boolean insertTrade(String account, String contract, String trader,
			Integer size, Double price, String desc, Integer type);

	public boolean updateTrade(String account, String contract, String trader,
			Integer size, Double price, String desc, Integer type,
			Integer tradeno);
}
