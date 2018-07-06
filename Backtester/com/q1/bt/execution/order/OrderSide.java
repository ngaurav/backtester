package com.q1.bt.execution.order;

public enum OrderSide {
  BUY(1),  SELL(2);
  
  private final int Val;
  
  private OrderSide(int Val) {
    this.Val = Val;
  }
  
  public int getVal() {
    return this.Val;
  }
  
  public String getStringVal() {
    if (this.Val == 1)
      return "BUY";
    if (this.Val == 2) {
      return "SELL";
    }
    return null;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/OrderSide.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */