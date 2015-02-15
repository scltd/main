

class MessageTypes:
  InstrumentStateChange = 505
  ClassStateChange = 516
  Mail = 523
  IndicativeMatchingPrice = 530
  Collars = 537
  SessionTimetable = 539
  StartReferential = 550
  EndReferential = 551
  Referential = 556
  ShortSaleChange = 560
  SessionSummary = 561
  GenericMessage = 592
  OpenPosition = 595
  #PRICES AND TRADES
  TradeCancellation = 221
  TradeFullInformation = 240
  PriceUpdate = 241
  TradePublication = 243
  AuctionSummary = 245
  ClosingPrice = 247
  #BBO
  QuotesAndBBO5 = 140
  IndicationOfInterest = 142
  #ORDER BOOK
  OrderUpdate = 230
  OrderBookRetrans = 231
  #INDEX
  RealTimeIndex = 546
  IndexSummary = 547
  IndexPortfolio = 548
  #BOND
  AccuredInterest = 601
  InterestForSettlementDay = 602
  YieldToMaturity = 603