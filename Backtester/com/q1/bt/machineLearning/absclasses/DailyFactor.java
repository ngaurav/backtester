package com.q1.bt.machineLearning.absclasses;

import com.q1.bt.machineLearning.utility.CandleData;

public class DailyFactor {
  String ClassName;
  String factorName;
  CandleIndVar dv;
  
  public DailyFactor(String factorName, CandleIndVar dv) {
    this.ClassName = dv.getClass().getSimpleName();
    this.factorName = factorName;
    this.dv = dv;
  }
  
  public void updateInd(CandleData[] dd) {
    this.dv.updateInd(dd);
  }
  
  public Double[] getValue() {
    return this.dv.getInd();
  }
  
  public String getName() {
    return this.factorName;
  }
  
  public String getfactorType() {
    return this.dv.getClass().getSimpleName();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/DailyFactor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */