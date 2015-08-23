package com.scsociety.apps;

public interface EventNotifierInterface {
	public void handleOrderAck(int orderId);
	public void handleOrderFill(int orderId, double avgFillPrice, int fillQty);
	public void handleNextOrderId(int orderId);
}
