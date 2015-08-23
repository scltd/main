package com.scsociety.apps;

import io.netty.channel.ChannelHandlerContext;

import com.scsociety.apps.network.OrderEntryProto.Order;

public interface OrderEntryInterface {
  void handleOrder(Order order, ChannelHandlerContext ctx);
}
