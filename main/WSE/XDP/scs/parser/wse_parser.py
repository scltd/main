from models import *
from datetime import datetime, timedelta
from consts import *
import logging


class XDPDumpParser:
  def __init__(self, outputGenerator=None):
    self.output = outputGenerator
    self.date = ""
    self.cc = {}
  def handleInstrumentStateChange(self, msg):
    logging.info("[handleInstrumentStateChange] %s", str(msg))
  def handleOrderUpdate(self, msg, recvTime):
    logging.debug("[handleOrderUpdate] %s", str(msg))
    contract = None
    order = Order()
    recvTime = recvTime.split(".")
    recvTime = datetime.strptime("%s %s" % (self.date, recvTime[0]), "%Y%m%d %H:%M:%S") + timedelta(milliseconds=int(recvTime[1])) 
    order.rts = recvTime
    for field in msg:
# 2015-01-29 10:16:16,295:DEBUG:[handleOrderUpdate] ['MsgSize=62', 'MsgType=230', 'SymbolIndex=10616', 'SourceTime=04:00:10.534', 'SourceSeqNum=10838', 'Price=50', 'AggregatedVolume=46881', 'Volume=10000', 'LinkID=0', 'OrderID=17', 'SystemID=11', 'SourceTimeMicroSecs=224', 'NumberOrders=5', 'Side=2', 'OrderType=2', 'ActionType=A', 'PriceScaleCode=2', 'OrderDate=20130415', 'OrderPriorityDate=20130415', 'OrderPriorityTime=40010534', 'OrderPriorityMicroSecs=196', 'OrderOrigin=.', 'Filler=0']
      sF = field.split("=")
      if sF[0] == "SymbolIndex":
        symId = int(sF[1])
        contract = self.cc[symId]
      if sF[0] == "SourceTime":
        ssF = sF[1].split(".")
        order.ts = datetime.strptime("%s %s" % (self.date, ssF[0]), "%Y%m%d %H:%M:%S") + timedelta(milliseconds=int(ssF[1])) 
      if sF[0] == "SourceTimeMicroSecs":
        order.ts += timedelta(microseconds=int(sF[1])) 
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
  def handleTradeFullInformation(self, msg, recvTime):
    logging.debug("[handleTradeFullInformation] %s", str(msg))
    #['MsgSize=66', 
    #'MsgType=240', 
    #'SymbolIndex=11302',
    #'SourceTime=07:44:02.361',
    #'TradeIDNumber=24', 
    #'QuoteLinkID=0', 
    #'SourceSeqNum=129040', 
    #'Price=8900', 
    #'Volume=162', 
    #'CumulativeQuantity=1063', 
    #'HighestPrice=8935', 
    #'LowestPrice=8860', 
    #'VariationLastPrice=45', 
    #'SystemID=11', 
    #'SourceTimeMicroSecs=728', 
    #'Filler=0', 
    #'SmallTradeIndicator=', 
    #'TradCond2=0', 
    #'TradCond3=0', 
    #'TradeOrigin=B', 
    #'TickDirection=0', 
    #'OpeningTradeIndicator=S', 
    #'VariationScaleCode=2', 
    #'PriceScaleCode=2',
    #'SSTrade=N']
    trade = Trade()
    recvTime = recvTime.split(".")
    recvTime = datetime.strptime("%s %s" % (self.date, recvTime[0]), "%Y%m%d %H:%M:%S") + timedelta(milliseconds=int(recvTime[1])) 
    trade.rts = recvTime    
    contract = None
    for field in msg:
      sF = field.split("=")
      if sF[0] == "SymbolIndex":
        symId = int(sF[1])
        contract = self.cc[symId]
        
      if sF[0] == "SourceTime":
        ssF = sF[1].split(".")        
        trade.ts = datetime.strptime("%s %s" % (self.date, ssF[0]), "%Y%m%d %H:%M:%S") + timedelta(milliseconds=int(ssF[1])) 
      if sF[0] == "SourceTimeMicroSecs":
        trade.ts += timedelta(microseconds=int(sF[1]))
      if sF[0] == "Volume":
        trade.volume = int(sF[1])
      if sF[0] == "Price":
        trade.price = int(sF[1])
      if sF[0] == "HighestPrice":
        trade.high = int(sF[1])
      if sF[0] == "LowestPrice":
        trade.low = int(sF[1])
      if sF[0] == "OpeningTradeIndicator":
        trade.indicator = sF[1]
      if sF[0] == "CumulativeQuantity":
        trade.cum_qty = int(sF[1])
    try:
      contract.market.add_trade(trade)
    except KeyError:
      logging.error("ERROR: %s" % contract.name)
      logging.error("ERROR: %s" % (str(trade)))
      
       
  def handlePriceUpdate(self, msg):
    logging.debug("[handlePriceUpdate] %s", str(msg))
  def handleAuctionSummary(self, msg):
    logging.debug("[handleAuctionSummary] %s", str(msg))
  def handleClosingPrice(self, msg,recvTime):
    logging.debug("[handleClosingPrice] %s", str(msg))
    trade = Trade()
    recvTime = recvTime.split(".")
    recvTime = datetime.strptime("%s %s" % (self.date, recvTime[0]), "%Y%m%d %H:%M:%S") + timedelta(milliseconds=int(recvTime[1])) 
    trade.rts = recvTime    
    contract = None
    for field in msg:
      sF = field.split("=")
      if sF[0] == "SymbolIndex":
        symId = int(sF[1])
        contract = self.cc[symId]
      if sF[0] == "ClosingPrice":
        trade.price = int(sF[1])
        trade.indicator = 'C'
      if sF[0] == "QtyShares":
        trade.volume = int(sF[1])

        
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
    expiration = ""
    mnemonic = ""
    for field in msg:
      sF = field.split("=")
      if sF[0] == "SymbolIndex":
        symId = int(sF[1])
      elif sF[0] == "InstrumentCode":
        isin = sF[1]
      elif sF[0] == "TradingCurrency":
        currency = sF[1]
      elif sF[0] == "InstrumentName":
        name = sF[1].strip('.')
      elif sF[0] == "Mnemo":
        mnemonic = sF[1].strip('.')
      elif sF[0] == "ExpiryDate":
        expiration = sF[1]
      elif sF[0] == "Multiplier":
        mplier = float(sF[1])
      elif sF[0] == "LotSize":
        lotsize = int(sF[1])
      elif sF[0] == "EventDate":
        date = sF[1]
        self.date = date
    if mnemonic:name=mnemonic
    self.cc[symId] = WSEContract(symId, isin, currency, name, mplier, lotsize,date,expiration)
    logging.info("Contract: %d - %s [%s]" % (symId, name,self.date))
  def handleShortSaleChange(self, msg):
    logging.debug("[handleShortSaleChange] %s", str(msg))
  def handleSessionSummary(self, msg):
    logging.debug("[handleSessionSummary] %s", str(msg))
    
