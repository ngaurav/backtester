package com.q1.bt.machineLearning.absclasses;

import com.q1.bt.machineLearning.utility.IntradayData;

public abstract class IntradayIndVar extends IndVar {
  public boolean psuedoDaily;
  
  public IntradayIndVar(FactorType fType, ValueType vType) { super(fType, vType); }
  
  public abstract void updateInd(IntradayData paramIntradayData);
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/IntradayIndVar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */