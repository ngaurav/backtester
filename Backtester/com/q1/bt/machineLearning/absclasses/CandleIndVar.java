package com.q1.bt.machineLearning.absclasses;

import com.q1.bt.machineLearning.utility.CandleData;

public abstract class CandleIndVar extends IndVar {
  public CandleIndVar(FactorType fType, ValueType vType) {
    super(fType, vType);
  }
  
  public abstract void updateInd(CandleData[] paramArrayOfCandleData);
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/CandleIndVar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */