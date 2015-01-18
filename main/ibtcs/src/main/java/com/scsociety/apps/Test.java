package com.scsociety.apps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.client.AnyWrapper;
import com.ib.client.EClientSocket;
import com.ib.client.ExecutionFilter;

public class Test {

	public static void main(String[] args) 
	{
		Logger log = LoggerFactory.getLogger(Test.class);
	    log.info("Hello World");
	    log.trace("TRACE TEST");
	    Properties p = new Properties();
		try {
			p.loadFromXML(new FileInputStream(new File("/tmp/connection.properties.xml")));
			
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AnyWrapper z = new IBInterface(p);
		EClientSocket e = new EClientSocket(z);
		e.eConnect("localhost", 4001, 10);
		e.reqAccountSummary(1,"All","NetLiquidation");
		e.reqExecutions(2, new ExecutionFilter());
	}

}
