package com.scsociety.scjapi.models;

public class Currency {
  public static final String EURO = "EUR";
  public static final String US_DOLLAR = "USD";
  public static final String POLISH_ZLOTY = "PLN";
  private final String val;

  public Currency(String val) {
    this.val = val;
  }
}
