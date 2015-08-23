package com.scsociety.apps.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.scsociety.apps.OrderEntryInterface;
import com.scsociety.apps.network.OrderEntryProto.Order;

public class BookServerHandler extends SimpleChannelInboundHandler<Order> {
  OrderEntryInterface _orderEntryIF;

  public BookServerHandler(OrderEntryInterface oei) {
    _orderEntryIF = oei;
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {}

  {

  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, Order order) throws Exception {

    long currentTime = System.currentTimeMillis();
    _orderEntryIF.handleOrder(order, ctx);
    // Order.Builder builder = Order.newBuilder();
    // ctx.write(builder.build());
  }
}
