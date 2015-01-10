package com.scsociety.scjapi.misc;

public final class SQLQueries {
	
	public static String ALL_CONTRACTS = "SELECT * FROM contracts;";
	public static String ONE_CONTRACT = "SELECT * FROM contracts WHERE uuid = ?::uuid";

}
