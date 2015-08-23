package com.scsociety.apps;

import com.scsociety.apps.network.OrderEntryProto.Order;

public interface OrderEntryInterface 
{
	void handleOrder(Order order);
}
