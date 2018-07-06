package com.q1.bt.process.objects;

import com.q1.bt.process.parameter.MachineLearningParameter;


public class MachineLearning
{
  Backtest backtest;
  MachineLearningParameter mlParameter;
  String timeStamp;
  
  public MachineLearning(MachineLearningParameter mlParameter, Backtest backtest)
  {
    this.mlParameter = mlParameter;
    this.backtest = backtest;
  }
  
  public Backtest getBacktest()
  {
    return this.backtest;
  }
  
  public void setBacktest(Backtest backtest) {
    this.backtest = backtest;
  }
  
  public MachineLearningParameter getMlParameter() {
    return this.mlParameter;
  }
  
  public void setMlParameter(MachineLearningParameter mlParameter) {
    this.mlParameter = mlParameter;
  }
  
  public String getTimeStamp() {
    return this.timeStamp;
  }
  
  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/objects/MachineLearning.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */