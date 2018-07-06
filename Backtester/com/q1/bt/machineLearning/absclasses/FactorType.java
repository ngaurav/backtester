package com.q1.bt.machineLearning.absclasses;

public enum FactorType {
  Price(1),  MTM(2),  External(3);
  
  private final int Val;
  
  private FactorType(int Val) {
    this.Val = Val;
  }
  
  public int getVal() {
    return this.Val;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/FactorType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */