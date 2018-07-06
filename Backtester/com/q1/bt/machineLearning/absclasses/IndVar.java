package com.q1.bt.machineLearning.absclasses;

public abstract class IndVar
{
  public FactorType fType;
  public ValueType vType;
  
  public IndVar(FactorType fType, ValueType vType) {
    this.fType = fType;
    this.vType = vType;
  }
  
  public abstract Double[] getInd();
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/IndVar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */