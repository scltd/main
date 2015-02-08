#!/usr/bin/env python

import sys
import re
import parser as pXDP
from messages import *
import logging


logging.basicConfig(format='%(asctime)s:%(levelname)s:%(message)s',level=logging.DEBUG,filename=None)
fh = open('/Users/zummie/2013-04/2013-04-WIG30-101.xdp')

parseRe = re.compile("(\[.*\])$")

d = {}
parse = pXDP.XDPDumpParser()
for i in fh:
  sI = i.split("|")
  recvTime = sI[0]
  msg = sI[2]
  m = parseRe.search(msg)
  if m:
    msg = m.group(0)
    sMsg = msg[1:-2].split()
    msgType = int(sMsg[1].split("=")[1])
    if msgType == MessageTypes.InstrumentStateChange:
      parse.handleInstrumentStateChange(sMsg)
    elif msgType == MessageTypes.OrderUpdate:
      parse.handleOrderUpdate(sMsg)
    elif msgType == MessageTypes.TradeFullInformation:
      parse.handleTradeFullInformation(sMsg)
    elif msgType == MessageTypes.PriceUpdate:
      parse.handlePriceUpdate(sMsg)
    elif msgType == MessageTypes.AuctionSummary:
      parse.handleAuctionSummary(sMsg)
    elif msgType == MessageTypes.ClosingPrice:
      parse.handleClosingPrice(sMsg)
    elif msgType == MessageTypes.InstrumentStateChange:
      parse.handleInstrumentStateChange(sMsg)
    elif msgType == MessageTypes.IndicativeMatchingPrice:
      parse.handleIndicativeMatchingPrice(sMsg)
    elif msgType == MessageTypes.Collars:
      parse.handleCollars(sMsg)
    elif msgType == MessageTypes.Referential:
      parse.handleReferential(sMsg)
    elif msgType == MessageTypes.ShortSaleChange:
      parse.handleShortSaleChange(sMsg)
    elif msgType == MessageTypes.SessionSummary:
      parse.handleSessionSummary(sMsg)
    else:
      print 'Unknown msgType', msgType

if __name__ == "__main__":
  pass