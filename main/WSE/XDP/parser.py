from models import *
from datetime import datetime, timedelta
from consts import *
import logging


class XDPDumpParser:
  def __init__(self, outputGenerator=None, date="20130415"):
    self.output = outputGenerator
    self.cc = {}
    self.date = date
  def handleInstrumentStateChange(self, msg):
    logging.info("[handleInstrumentStateChange] %s", str(msg))
  def handleOrderUpdate(self, msg):
    logging.debug("[handleOrderUpdate] %s", str(msg))
    contract = None
    order = Order()
    for field in msg:
# 2015-01-29 10:16:16,295:DEBUG:[handleOrderUpdate] ['MsgSize=62', 'MsgType=230', 'SymbolIndex=10616', 'SourceTime=04:00:10.534', 'SourceSeqNum=10838', 'Price=50', 'AggregatedVolume=46881', 'Volume=10000', 'LinkID=0', 'OrderID=17', 'SystemID=11', 'SourceTimeMicroSecs=224', 'NumberOrders=5', 'Side=2', 'OrderType=2', 'ActionType=A', 'PriceScaleCode=2', 'OrderDate=20130415', 'OrderPriorityDate=20130415', 'OrderPriorityTime=40010534', 'OrderPriorityMicroSecs=196', 'OrderOrigin=.', 'Filler=0']
      sF = field.split("=")
      if sF[0] == "SymbolIndex":
        symId = int(sF[1])
        contract = self.cc[symId]
      if sF[0] == "SourceTime":
        ssF = sF[1].split(".")
        contract.market.exchangeTS = datetime.strptime("%s %s" % (self.date, ssF[0]), "%Y%m%d %H:%M:%S") + timedelta(milliseconds=int(ssF[1])) 
      if sF[0] == "SourceTimeMicroSecs":
        contract.market.exchangeTS += timedelta(microseconds=int(sF[1])) 
#        logging.info(contract.market.exchangeTS) 
      if sF[0] == "Volume":
        order.qty = int(sF[1])
      if sF[0] == "AggregatedVolume":
        order.aggregated_qty = int(sF[1])    
      if sF[0] == "Price":
        order.price = int(sF[1])
      if sF[0] == "Side":
        if sF[1] == OrderUpdate.Side_SELL: order.qty = order.qty * -1
        if sF[1] == OrderUpdate.Side_SHORT_SELL: order.qty = order.qty * -1
        order.side = sF[1]
      if sF[0] == "NumberOrders":
        order.number_of_orders = int(sF[1])
      if sF[0] == "OrderType":
        order.order_type = sF[1]
      if sF[0] == "OrderID":
        order.id = int(sF[1])
      if sF[0] == "ActionType":
        order.action_type = sF[1]
        if sF[1] == OrderUpdate.ActionType_ADD:
          pass
        if sF[1] == OrderUpdate.ActionType_MOD:
          pass
        if sF[1] == OrderUpdate.ActionType_DEL:
          pass
    if order.side in ['.', ' ']:return
    if order.order_type == '1':return 
    if order.order_type == 'P':return 
    try:
      contract.market.add_order(order)
    except KeyError:
      logging.error("ERROR: %s" % contract.name)
      logging.error("ERROR: %s" % (str(order)))
  def handleTradeFullInformation(self, msg):
    logging.debug("[handleTradeFullInformation] %s", str(msg))
  def handlePriceUpdate(self, msg):
    logging.debug("[handlePriceUpdate] %s", str(msg))
  def handleAuctionSummary(self, msg):
    logging.debug("[handleAuctionSummary] %s", str(msg))
  def handleClosingPrice(self, msg):
    logging.debug("[handleClosingPrice] %s", str(msg))
  def handleInstrumentStateChange(self, msg):
    logging.debug("[handleInstrumentStateChange] %s", str(msg))
  def handleIndicativeMatchingPrice(self, msg):
    logging.debug("[handleIndicativeMatchingPrice] %s", str(msg))
  def handleCollars(self, msg):
    logging.debug("[handleCollars] %s", str(msg))
  def handleReferential(self, msg):
    # logging.debug("[handleReferential] %s",str(msg))
    symId = 0
    isin = ""
    currency = "PLN"
    name = ""
    mplier = 0
    lotsize = 0
    for field in msg:
      sF = field.split("=")
      if sF[0] == "SymbolIndex":
        symId = int(sF[1])
      elif sF[0] == "InstrumentCode":
        isin = sF[1]
      elif sF[0] == "TradingCurrency":
        currency = sF[1]
      elif sF[0] == "Mnemo":
        name = sF[1].strip('.')
      elif sF[0] == "Multiplier":
        mplier = float(sF[1])
      elif sF[0] == "LotSize":
        lotsize = int(sF[1])
    self.cc[symId] = WSEContract(symId, isin, currency, name, mplier, lotsize)
    logging.info("Contract: %d - %s" % (symId, name))
  def handleShortSaleChange(self, msg):
    logging.debug("[handleShortSaleChange] %s", str(msg))
  def handleSessionSummary(self, msg):
    logging.debug("[handleSessionSummary] %s", str(msg))
    
