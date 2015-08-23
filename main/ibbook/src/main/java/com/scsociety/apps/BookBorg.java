package com.scsociety.apps;

import java.util.Properties;

import com.scsociety.apps.network.BookServer;
import com.scsociety.scjapi.ContractsManagement;

public class BookBorg {
	private BookServer _bookServer;
	private ContractsManagement _contracts;
	public BookBorg(Properties p) {
		int port = Integer.parseInt(p.get("ibbook.servicePort").toString());
		_bookServer = new BookServer(port);
		_contracts = new ContractsManagement(p);
	}
}
