package com.scsociety.scjapi.interfaces;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.scsociety.scjapi.models.Contract;
public class Test {

	public static void main(String[] args) 
	{
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
		IContracts<Contract> ic = new ContractsImpl<Contract>(p,Contract.class);
		ic.getAllContracts();
	}

}
