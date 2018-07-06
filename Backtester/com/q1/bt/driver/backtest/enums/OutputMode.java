package com.q1.bt.driver.backtest.enums;

public enum OutputMode {
  Normal(0),  Existing(1),  Chop(2);
  
  private final int Val;
  
  private OutputMode(int Val) {
    this.Val = Val;
  }
  
  public int getVal() {
    return this.Val;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/backtest/enums/OutputMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */