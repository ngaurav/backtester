package com.q1.bt.execution;

public enum RolloverMethod
{
  CloseToClose(1),  ExitAtClose(2);
  
  private final int Val;
  
  private RolloverMethod(int Val) {
    this.Val = Val;
  }
  
  public int getVal() {
    return this.Val;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/RolloverMethod.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */