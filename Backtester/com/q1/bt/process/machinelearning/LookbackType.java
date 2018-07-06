package com.q1.bt.process.machinelearning;

public enum LookbackType
{
  Hinged("Hinged"),  Rolling("Rolling");
  
  private final String Val;
  
  private LookbackType(String Val) {
    this.Val = Val;
  }
  
  public String getVal() {
    return this.Val;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/machinelearning/LookbackType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */