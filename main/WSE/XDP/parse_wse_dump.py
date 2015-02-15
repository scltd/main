#!/usr/bin/env python

import sys
import getopt
import re
import os
import scs.parser.wse_parser as pXDP
from scs.parser.wse.messages import *
import logging


help_message = """
--inputfile,-i\t\t<INPUT FILE> required XDP file
--verbose,-v\t\tTurns on debugging level
--logfile,-l\t\t<LOGFILE> logfile where to log to
"""
class Usage(Exception):
  def __init__(self,msg):
    self.msg = msg

parseRe = re.compile("(\[.*\])$")

def main(argv=sys.argv):
  global parseRe
  if argv is None:
    argv = sys.argv
  try:
    try:
      opts, args = getopt.getopt(argv[1:], "hvl:i:", ["inputfile=","help", "logfile=","verbose"])
    except getopt.error, msg:
      raise Usage(msg)
    logfile = None
    inputFile = None
    verbose = logging.INFO
    # option processing
    for option, value in opts:
      if option in ("-v","--verbose"):
        verbose = logging.DEBUG
      elif option in ("-l","--logfile"):
        logfile = value
      elif option in ("-i","--inputfile"):
        inputFile = value
  
    if not inputFile: raise Usage("Need to specify inputfile")
    if not os.access(inputFile, os.R_OK): raise Usage("Input file is not readable.")
    logging.basicConfig(format='%(asctime)s:%(levelname)s:%(message)s',level=verbose,filename=logfile)
    fh = open(inputFile)
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
          parse.handleOrderUpdate(sMsg,recvTime)
        elif msgType == MessageTypes.TradeFullInformation:
          parse.handleTradeFullInformation(sMsg,recvTime)
        elif msgType == MessageTypes.PriceUpdate:
          parse.handlePriceUpdate(sMsg)
        elif msgType == MessageTypes.AuctionSummary:
          parse.handleAuctionSummary(sMsg)
        elif msgType == MessageTypes.ClosingPrice:
          parse.handleClosingPrice(sMsg,recvTime)
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

  
  except Usage,e:
    sys.stderr.write("ERROR: %s" % (e.msg))
    sys.stderr.write(help_message)
    return 1;

if __name__ == "__main__":
  sys.exit(main())
  pass