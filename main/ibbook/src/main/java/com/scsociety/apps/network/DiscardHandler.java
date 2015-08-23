package com.scsociety.apps.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class DiscardHandler extends ChannelHandlerAdapter { // (1)
	private ByteBuf buf;

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		buf = ctx.alloc().buffer(4); // (1)
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		buf.release(); // (1)
		buf = null;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
//		OrderEntryProto.Order c = OrderEntryProto.Order.newBuilder()
//				.setUuid("test123").setQty(0)
//				.setTimeInForce(OrderEntryProto.Order.TIF.AUC).build();
//		ByteBuf encoded = ctx.alloc().buffer(c.getSerializedSize());
//		encoded.writeBytes(c.toByteArray());
//		ctx.write(encoded);
//		ctx.flush();
		ByteBuf m = (ByteBuf) msg;
		buf.writeBytes(m);
		m.release();
		long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
		System.out.println(new Date(currentTimeMillis));
		// ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
