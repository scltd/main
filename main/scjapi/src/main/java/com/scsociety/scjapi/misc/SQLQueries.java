package com.scsociety.scjapi.misc;

public final class SQLQueries {
	
	public static String ALL_CONTRACTS = "SELECT * FROM contracts;";
	public static String CONTRACT_BY_UUID = "SELECT * FROM contracts WHERE uuid = ?::uuid";
	public static String CONTRACT_BY_EXCHANGE_ID = "SELECT * FROM contracts WHERE exchange_contract_id LIKE ?";
	public static String INSERT_TRADE = "INSERT into trades VALUES (?,?::uuid,?,?,?,?,?,nextval('tradenumber'),NOW())";
	public static String DELETE_TRADE = "UPDATE trades set type=2 WHERE tradeno=?";
}
