package com.scsociety.apps;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;
import com.scsociety.scjapi.RiskManagement;

public class IBInterface implements EWrapper {
	private RiskManagement _riskMgmt;
	final static Logger log = LoggerFactory.getLogger(IBInterface.class);

	public IBInterface(Properties config) {
		_riskMgmt = new RiskManagement(config);
	}

	public void error(Exception e) {
		log.error("Exception: {}", e.getMessage());
	}

	public void error(String str) {
		log.error("{}", str);
	}

	public void error(int id, int errorCode, String errorMsg) {
		log.error("id: {}, ec: {}, errorMsg: {}", id, errorCode, errorMsg);
	}

	public void connectionClosed() {
		log.info("Connection closed.");

	}

	public void tickPrice(int tickerId, int field, double price,
			int canAutoExecute) {
		log.info("TickPrice: {}", tickerId);
	}

	public void tickSize(int tickerId, int field, int size) {
		log.info("tickSize: {}", tickerId);

	}

	public void tickOptionComputation(int tickerId, int field,
			double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta,
			double undPrice) {
		log.info("tickOptionComputation: {}", tickerId);

	}

	public void tickGeneric(int tickerId, int tickType, double value) {
		log.info("tickGeneric: tickerId: {}, tickType: {}, value: {}",
				tickerId, tickType, value);

	}

	public void tickString(int tickerId, int tickType, String value) {
		log.info("tickString: tickerId: {}, tickType: {}, value: {}", tickerId,
				tickType, value);

	}

	public void tickEFP(int tickerId, int tickType, double basisPoints,
			String formattedBasisPoints, double impliedFuture, int holdDays,
			String futureExpiry, double dividendImpact, double dividendsToExpiry) {
		log.info("tickEFP: tickerId: {}, tickType: {}, basisPoints: {}",
				tickerId, tickType, basisPoints);

	}

	public void orderStatus(int orderId, String status, int filled,
			int remaining, double avgFillPrice, int permId, int parentId,
			double lastFillPrice, int clientId, String whyHeld) {
		log.info(
				"orderStatus: orderId: {}, status: {}, filled: {}, clientId:{}",
				orderId, status, filled, clientId);
	}

	public void openOrder(int orderId, Contract contract, Order order,
			OrderState orderState) {
		log.info(
				"orderStatus: orderId: {}, m_conId: {}, m_action: {}, orderState:{}",
				orderId, contract.m_conId, order.m_action, orderState);

	}

	public void openOrderEnd() {
		log.info("OpenOrderEnd.");

	}

	public void updateAccountValue(String key, String value, String currency,
			String accountName) {
		log.info(
				"updateAccountValue: {}, value:{}, currency:{},accountName:{}",
				key, value, currency, accountName);
	}

	public void updatePortfolio(Contract contract, int position,
			double marketPrice, double marketValue, double averageCost,
			double unrealizedPNL, double realizedPNL, String accountName) {
		log.info("updatePortfolio");

	}

	public void updateAccountTime(String timeStamp) {
		log.info("updateAccountTime");

	}

	public void accountDownloadEnd(String accountName) {
		log.info("accountDownloadEnd");

	}

	public void nextValidId(int orderId) {
		log.info("nextValidId");

	}

	public void contractDetails(int reqId, ContractDetails contractDetails) {
		log.info("contractDetails");

	}

	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		log.info("bondContractDetails");

	}

	public void contractDetailsEnd(int reqId) {
		log.info("contractDetailsEnd");

	}

	public void execDetails(int reqId, Contract contract, Execution execution) {
		if (execution.m_side.equals("SLD")) {
			execution.m_cumQty *= -1;
		}
		log.trace("execDetails [{}],{} - {}@{} {}[{}]", reqId,
				contract.m_localSymbol, execution.m_cumQty, execution.m_price,
				execution.m_clientId, execution.m_execId);
		if (!_riskMgmt.insertPosition(execution.m_clientId,new Integer(contract.m_conId).toString(),
				execution.m_cumQty, execution.m_price)) {
			log.error("Failed to insert trade: {},{},{},{}",
					execution.m_clientId, contract.m_conId, execution.m_cumQty,
					execution.m_price);
		}
		// tradesInterface.updatePosition()
	}

	public void execDetailsEnd(int reqId) {
		log.info("execDetailsEnd");
	}

	public void updateMktDepth(int tickerId, int position, int operation,
			int side, double price, int size) {
		log.info("updateMktDepth");
	}

	public void updateMktDepthL2(int tickerId, int position,
			String marketMaker, int operation, int side, double price, int size) {
		log.info("updateMktDepthL2");

	}

	public void updateNewsBulletin(int msgId, int msgType, String message,
			String origExchange) {
		log.info("updateNewsBulletin");

	}

	public void managedAccounts(String accountsList) {
		log.info("managedAccounts {} ", accountsList);

	}

	public void receiveFA(int faDataType, String xml) {
		log.info("receiveFA");
	}

	public void historicalData(int reqId, String date, double open,
			double high, double low, double close, int volume, int count,
			double WAP, boolean hasGaps) {
		log.info("historicalData");

	}

	public void scannerParameters(String xml) {
		log.info("scannerParameters");

	}

	public void scannerData(int reqId, int rank,
			ContractDetails contractDetails, String distance, String benchmark,
			String projection, String legsStr) {
		log.info("scannerData");

	}

	public void scannerDataEnd(int reqId) {
		log.info("scannerDataEnd");

	}

	public void realtimeBar(int reqId, long time, double open, double high,
			double low, double close, long volume, double wap, int count) {
		log.info("realtimeBar");

	}

	public void currentTime(long time) {
		log.info("currentTime");

	}

	public void fundamentalData(int reqId, String data) {
		log.info("fundamentalData");

	}

	public void deltaNeutralValidation(int reqId, UnderComp underComp) {
		log.info("deltaNeutralValidation");

	}

	public void tickSnapshotEnd(int reqId) {
		log.info("tickSnapshotEnd");

	}

	public void marketDataType(int reqId, int marketDataType) {
		log.info("marketDataType");

	}

	public void commissionReport(CommissionReport commissionReport) {
		log.info("commissionReport");
	}

	public void position(String account, Contract contract, int pos,
			double avgCost) {
		log.info("position");
	}

	public void positionEnd() {
		log.info("positionEnd");

	}

	public void accountSummary(int reqId, String account, String tag,
			String value, String currency) {
		log.trace("accountSummary: {}, {}, {}, {} {}", reqId, account, tag,
				value, currency);

	}

	public void accountSummaryEnd(int reqId) {
		log.info("accountSummaryEnd: {}", reqId);
	}

	public void verifyMessageAPI(String apiData) {
		log.info("verifyMessageAPI");
	}

	public void verifyCompleted(boolean isSuccessful, String errorText) {
		log.info("verifyCompleted");
	}

	public void displayGroupList(int reqId, String groups) {
		log.info("displayGroupList");

	}

	public void displayGroupUpdated(int reqId, String contractInfo) {
		log.info("displayGroupUpdated");
	}

}
