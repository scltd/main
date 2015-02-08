import logging
import sortedcontainers as sct
from consts import *

class Order(object):
  # 2015-01-29 10:16:16,295:DEBUG:[handleOrderUpdate] ['MsgSize=62', 'MsgType=230', 'SymbolIndex=10616', 'SourceTime=04:00:10.534', 'SourceSeqNum=10838', 'Price=50', 'AggregatedVolume=46881', 'Volume=10000', 'LinkID=0', 'OrderID=17', 'SystemID=11', 'SourceTimeMicroSecs=224', 'NumberOrders=5', 'Side=2', 'OrderType=2', 'ActionType=A', 'PriceScaleCode=2', 'OrderDate=20130415', 'OrderPriorityDate=20130415', 'OrderPriorityTime=40010534', 'OrderPriorityMicroSecs=196', 'OrderOrigin=.', 'Filler=0']

  def __init__(self):
    self.id = None
    self.ts = None
    self.qty = 0
    self.aggregated_qty = 0
    self.action_type = ''
    self.number_of_orders = 0
    self.order_type = 0
    self.price = 0
    self.side = ''
  def __str__(self):
    return "Order [%3s@%.2f] %d|%s|%s" % (self.qty, self.price, self.aggregated_qty, self.order_type, self.action_type)

class Side(object):
  def __init__(self):
    self.price = 0.0
    self.qty = 0
    self.ts = 0
    self.no_contributors = 0
  def __str__(self):
    return "%d@%.3f [%d] - [%s]" % (self.qty, self.price, self.no_contributors,self.ts)
  def __repr__(self):
    return self.__str__()

class Market(object):
  def __init__(self, contract, layers):
    self.contract = contract
    self.exchangeTS = 0L
    self.receiveTS = 0L
    self.buys = sct.SortedDict()
    self.sells = sct.SortedDict()
    
    self.last = Side()
    self.orders = {}
  def dump_market(self):
    b = [Side(),Side(),Side()]
    a = [Side(),Side(),Side()]
    for bid in enumerate(reversed(self.buys.keys())):
      if bid[0] == 3: break
      for z in self.buys[bid[1]].values():
        b[bid[0]].price = z.price
        b[bid[0]].qty += z.qty
        b[bid[0]].no_contributors += 1        
    for ask in enumerate(self.sells.keys()):
      if ask[0] == 3: break
      for z in self.sells[ask[1]].values():
        a[ask[0]].price = z.price
        a[ask[0]].qty += z.qty
        a[ask[0]].no_contributors += 1
    for i in xrange(3):
      if b[0].price >= a[0].price: logging.info("TOP: CROSSED")
      logging.info("TOP[%d] %s | %s - %s" % (i,b[i],a[i],self.exchangeTS))
  def add_order(self, order):
    orderList = None
    price = order.price
    order.price = price / self.contract.multiplier
    if not price:return
    if order.qty > 0:
      if self.buys.has_key(price):
        orderList = self.buys[price]
      else:
        orderList = {}
        self.buys[price] = orderList
    else:
      if self.sells.has_key(price):
        orderList = self.sells[price]
      else:
        orderList = {}
        self.sells[price] = orderList   
    if order.action_type == OrderUpdate.ActionType_DEL:
      if order.id in orderList:
        del(orderList[order.id])
    else:
      orderList[order.id] = order
    if not len(orderList):
      if order.qty > 0: del(self.buys[price])
      else: 
        del(self.sells[price])
    if self.contract.name == "JSW":
      logging.info("ORDER: %s" % (str(order)))
      self.dump_market()
class WSEContract(object):
  def __init__(self, id, isin, currency, name, mplier, lotsize):
    self.isin = isin
    self.currency = currency
    self.name = name
    self.id = id
    self.multiplier = mplier
    self.lotsize = 0
    self.market = Market(self, 3)
