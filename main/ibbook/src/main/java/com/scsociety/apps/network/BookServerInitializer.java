package com.scsociety.apps.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.scsociety.apps.OrderEntryInterface;

public class BookServerInitializer extends ChannelInitializer<SocketChannel> {
  OrderEntryInterface _orderEntryIF;

  public BookServerInitializer(OrderEntryInterface ev) {
    _orderEntryIF = ev;
  }

  @Override
  public void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline p = ch.pipeline();
    p.addLast(new ProtobufVarint32FrameDecoder());
    p.addLast(new ProtobufDecoder(OrderEntryProto.Order.getDefaultInstance()));
    p.addLast(new ProtobufVarint32LengthFieldPrepender());
    p.addLast(new ProtobufEncoder());
    p.addLast(new BookServerHandler(_orderEntryIF));
  }
}
