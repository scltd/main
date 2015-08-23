package com.scsociety.apps;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.client.AnyWrapper;
import com.ib.client.EClientSocket;
import com.scsociety.apps.models.OrderWrapper;
import com.scsociety.apps.network.BookServer;
import com.scsociety.apps.network.OrderEntryProto;
import com.scsociety.apps.network.OrderEntryProto.Order;
import com.scsociety.scjapi.ContractsManagement;
import com.scsociety.scjapi.exceptions.ContractNotFoundException;
import com.scsociety.scjapi.models.Contract;

public class BookBorg implements EventNotifierInterface, OrderEntryInterface {
  final static Logger log = LoggerFactory.getLogger(BookBorg.class);

  private BookServer _bookServer;
  private ContractsManagement _contractsMgmt;
  private EClientSocket _externalConnection;
  private AnyWrapper _ibWrapper;
  private Map<String, OrderWrapper> _ordersMap;
  private Map<Integer, OrderWrapper> _ordersIdMap;
  private int _order_id;
  private Map<String, Queue<OrderEntryProto.Order>> _queue;


  // Far from perfect, but it should work for now.
  public BookBorg(Properties p) {
    _ibWrapper = new IBInterface(this);
    _externalConnection = new EClientSocket(_ibWrapper);
    _externalConnection.eConnect("localhost", 4001, 10);
    _bookServer = new BookServer(p, this);
    _contractsMgmt = new ContractsManagement(p);
    _ordersMap = new HashMap<String, OrderWrapper>();
    _ordersIdMap = new HashMap<Integer, OrderWrapper>();
    _queue = new HashMap<String, Queue<Order>>();
  }

  public void run() throws Exception {
    _contractsMgmt.loadContracts();
    _externalConnection.reqAccountUpdates(true, "U1029474");
    _bookServer.run();
  }

  public void sendErrorResponse(Order order, ChannelHandlerContext ctx) {
    OrderEntryProto.Order reply = OrderEntryProto.Order.newBuilder(order).setStatus(-1).build();
    ctx.writeAndFlush(reply);
  }

  public void sendReponse(Order order, ChannelHandlerContext ctx) {
    OrderEntryProto.Order reply = OrderEntryProto.Order.newBuilder(order).setStatus(0).build();
    ctx.writeAndFlush(reply);
  }

  public void handleOrder(Order order, ChannelHandlerContext ctx) {
    System.out.println(order.toString());
    try {
      Contract c = _contractsMgmt.getContract(order.getContract());
      log.trace("Order received for {}-{} contract.", c.getUuid(), c.getSymbol_name());
      OrderWrapper ord = _ordersMap.get(c.getUuid());
      if (order.getQty() == 0) {
        log.trace("Handling a delete.");
        if (ord == null) {
          log.warn("Missing order for {}, bailing out.", c.getUuid());
          sendErrorResponse(order, ctx);
        } else {
          if (ord.isAcked()) {
            _externalConnection.cancelOrder(ord.get_id());
            ord.set_state(OrderWrapper.State.CANCEL);
          } else {
            sendErrorResponse(order, ctx);
            log.error("Order is either pending cancel or hasn't been acked.Ignoring this request.");
          }
          // TODO what about different states ?!
        }
      } else {
        log.trace("Handling insert/modify.");
        if (ord == null) {
          log.info("Using orderId: {}", _order_id);
          log.trace("We have no knowledge of any orders for contract: {}. doing insert.",
              c.getUuid());
          ord = new OrderWrapper(order, ctx);
          ord.set_externalContract(c);
          ord.set_id(_order_id);
          _externalConnection.placeOrder(ord.get_id(), ord.get_externalContract(),
              ord.get_externalOrder());
          _ordersIdMap.put(ord.get_id(), ord);
          _ordersMap.put(c.getUuid(), ord);
          _order_id++;
        } else {
          if (ord.isAcked()) {
            log.trace("This normaly would be a modify... but we don't handle it yet...");
            _externalConnection.cancelOrder(ord.get_id());
            ord.set_state(OrderWrapper.State.PENDING_MODIFY);
            Queue<Order> que = _queue.get(ord.get_contractId());
            if (que == null) {
              que = new LinkedList<OrderEntryProto.Order>();
              _queue.put(ord.get_contractId(), que);
            }
            que.add(order);
          } else {
            sendErrorResponse(order, ctx);
            log.warn("Order {} hasn't been acked. Ignoring this update.", ord.get_id());
          }
        }
      }

    } catch (ContractNotFoundException e) {
      sendErrorResponse(order, ctx);
      log.warn("Order received for unknown contract {}. Discarding it.", order.getContract());
    } catch (NumberFormatException e) {
      log.warn("Failed to convert to int.");
    }

  }

  public void handleNextOrderId(int orderId) {
    log.trace("Setting next valid orderId: {}", orderId);
    _order_id = orderId;
  }

  public void handleOrderAck(int orderId) {
    log.trace("Handle OrderAck: {}", orderId);
    OrderWrapper ord = _ordersIdMap.get(orderId);
    if (ord == null)
      log.warn("Somehow we've got confirmation for unknown order id: {}", orderId);
    else {
      switch (ord.get_state()) {
        case INSERT:
        case MODIFY: {
          sendReponse(ord.get_orderWire(), ord.get_context());
          ord.setAck();
          break;
        }
        case CANCEL: {
          sendReponse(ord.get_orderWire(), ord.get_context());
          _ordersIdMap.remove(orderId);
          _ordersMap.remove(ord.get_contractId());

          break;
        }
        case PENDING_MODIFY: {
          _ordersIdMap.remove(orderId);
          _ordersMap.remove(ord.get_contractId());
          Queue<Order> que = _queue.get(ord.get_contractId());
          OrderEntryProto.Order o = que.poll();
          if (o != null) {
            this.handleOrder(o, ord.get_context());
          } else {
            log.error("something messed up with a queue");
          }
          break;

        }
        default: {
          log.warn("Normaly I should be here. Got ack what I already think was acked.");
        }
      }

    }
  }

  public void handleOrderFill(int orderId, double avgFillPrice, int fillQty) {
    log.trace("Handle OrderFill: {} price: {}, shares: {}", orderId, avgFillPrice, fillQty);

  }
}
