package com.q1.bt.machineLearning.absclasses;

import com.q1.bt.machineLearning.utility.DailyData;

public abstract class DailyIndVar extends IndVar
{
  public DailyIndVar(FactorType fType, ValueType vType) {
    super(fType, vType);
  }
  
  public abstract void updateInd(DailyData[] paramArrayOfDailyData);
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/DailyIndVar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */