package com.q1.bt.machineLearning.absclasses;

import com.q1.bt.machineLearning.utility.CandleData;

public class Factor
{
  String ClassName;
  String factorName;
  IndVar dv;
  String factorType;
  
  public Factor(String factorName, IndVar dv) {
    this.ClassName = dv.getClass().getSimpleName();
    this.factorName = factorName;
    this.dv = dv;
  }
  
  public void updateInd(CandleData[] dd) {
    if ((this.dv instanceof CandleIndVar)) {
      ((CandleIndVar)this.dv).updateInd(dd);
    }
    else if (((this.dv instanceof DailyIndVar)) && ((dd[0] instanceof com.q1.bt.machineLearning.utility.DailyData))) {
      ((DailyIndVar)this.dv).updateInd((com.q1.bt.machineLearning.utility.DailyData[])dd);

    }
    else
    {

      System.err.println("Indicator type doesn't match with the data provided for " + this.factorName); }
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


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/Factor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */