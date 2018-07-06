package com.q1.bt.driver.backtest.enums;

public enum AggregationMode {
  Fixed(0),  Active(1);
  
  private final int Val;
  
  private AggregationMode(int Val) {
    this.Val = Val;
  }
  
  public int getVal() {
    return this.Val;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/backtest/enums/AggregationMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */