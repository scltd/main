package com.scsociety.scjapi.models;

public enum ContractType {
  STOCK(0), FUTURE(1), OPTION(2);
  private int value;

  private ContractType(int value) {
    this.value = value;
  }
}
