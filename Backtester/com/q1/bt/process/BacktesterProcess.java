package com.q1.bt.process;

public enum BacktesterProcess
{
  Login(
    "Login"),  Backtest("Backtest"),  Results("Results"),  SensitivityAnalysis("Sensitivity Analysis"),  PostProcess("Post Process"),  MachineLearning(
    "Machine Learning"),  AssetAllocation("Asset Allocation"),  BatchProcess("Batch Process"),  RollingAnalysis("Rolling Analysis"),  IsOs("In Sample OOS"),  ResultGeneration("Result Generation");
  
  private final String Val;
  
  private BacktesterProcess(String Val) {
    this.Val = Val;
  }
  
  public String getVal() {
    return this.Val;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/BacktesterProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */