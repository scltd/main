import logging
import sortedcontainers as sct
from consts import *
import datetime


def totimestamp(dt, epoch=datetime.datetime(1970,1,1)):
    td = dt - epoch
    # return td.total_seconds()
    return (td.microseconds + (td.seconds + td.days * 24 * 3600) * 10**6) / 1e6 

class Trade(object):
  def __init__(self):
    self.id = 0
    self.ts = None
    self.price = 0
    self.volume = 0
    self.cum_qty = 0
    self.high = 0
    self.low = 0
    self.indicator = None
    self.rts = None
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
    self.rts = None
  def __str__(self):
    return "Order [%3s@%.2f] %d|%s|%s" % (self.qty, self.price, self.aggregated_qty, self.order_type, self.action_type)

class Side(object):
  def __init__(self):
    self.price = 0.0
    self.qty = 0
    self.ts = datetime.datetime(1970,1,1)
    self.no_contributors = 0
  def __str__(self):
    return "%d@%.3f [%d] - [%s]" % (self.qty, self.price, self.no_contributors,self.ts)
  def __repr__(self):
    return self.__str__()

class Market(object):
  def __init__(self, contract, layers,date):
    self.contract = contract
    self.exchangeTS = 0L
    self.receiveTS = 0L
    self.buys = sct.SortedDict()
    self.sells = sct.SortedDict()
    self.cumulative_qty = 0
    self.a = [Side(),Side(),Side()]
    self.b = [Side(),Side(),Side()]
    self.last = Side()
    self.orders = {}
    self.open = 0.0
    self.close = 0.0
    self.date = date

  def dump_market(self):
    for bid in enumerate(reversed(self.buys.keys())):
      if bid[0] == 3: break
      self.b[bid[0]].no_contributors = 0
      self.b[bid[0]].qty = 0
      self.b[bid[0]].price = 0.0
      self.b[bid[0]].ts = None
      for z in self.buys[bid[1]].values():
        self.b[bid[0]].price = z.price
        self.b[bid[0]].qty += z.qty
        self.b[bid[0]].no_contributors += 1
        self.b[bid[0]].ts = z.ts       
    for ask in enumerate(self.sells.keys()):
      if ask[0] == 3: break
      self.a[ask[0]].no_contributors = 0
      self.a[ask[0]].qty = 0
      self.a[ask[0]].price = 0
      self.a[ask[0]].ts = None
      for z in self.sells[ask[1]].values():
        self.a[ask[0]].price = z.price
        self.a[ask[0]].qty += abs(z.qty)
        self.a[ask[0]].no_contributors += 1
        self.a[ask[0]].ts = z.ts
    #logging.info("TOP[x] last: %d @ %3.3f | %d | %s" % (self.last.qty,self.last.price,self.cumulative_qty,self.last.ts))
    rtime_t = totimestamp(self.receiveTS)
    rtime = "%.6f" % rtime_t
    rtime = tuple(rtime.split("."))
    csv_line = "%s,%s,%d,%d,%d,0,%s,F,,,,,,A,101,%.3f,,%d,%.3f,%.3f," % (rtime[0],rtime[1],rtime_t*10**6,totimestamp(self.exchangeTS)*10**9,totimestamp(self.last.ts)*10**9,self.contract.name,self.last.price,self.cumulative_qty,self.open,self.close)
    for i in xrange(3):
      csv_line += "%.3f,%d,%d," % (self.b[i].price,self.b[i].qty,self.b[i].no_contributors)
      csv_line += "%.3f,%d,%d," % (self.a[i].price,self.a[i].qty,self.a[i].no_contributors)
    for i in xrange(7):
      csv_line += "0.000,0,0,0.000,0,0,"
    csv_line += "%d," % self.last.qty    
    csv_line += ",,"
    print csv_line    
      #if self.b[0].price >= self.a[0].price: logging.info("TOP: CROSSED - %s" % self.exchangeTS)
      #logging.info("TOP[%d] %s | %s - %s" % (i,self.b[i],self.a[i],self.a[i].ts))
  def add_trade(self,trade):
    self.last.qty = trade.volume
    self.last.price = trade.price / self.contract.multiplier
    self.last.ts = trade.ts
    self.exchangeTS = trade.ts
    self.receiveTS = trade.rts
    if trade.indicator == 'O':self.open = self.last.price
    if trade.indicator == 'C':
      self.cumulative_qty += trade.volume
      self.close = self.last.price
    else: self.cumulative_qty = trade.cum_qty
  def add_order(self, order):
    orderList = None
    price = order.price
    order.price = price / self.contract.multiplier
    self.receiveTS = order.rts
    self.exchangeTS = order.ts

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
    #if self.contract.name == "PKO":
      #logging.info("ORDER: %s" % (str(order)))
    self.dump_market()
class WSEContract(object):
  def __init__(self, id, isin, currency, name, mplier, lotsize,date,expiration):
    self.isin = isin
    self.currency = currency
    self.name = name
    self.id = id
    self.multiplier = mplier
    self.lotsize = 0
    self.expiration = expiration
    self.market = Market(self, 3,date)
