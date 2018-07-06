package com.q1.bt.process.backtest;

public enum SlippageModel
{
  AdaptiveModel(1),  AdaptiveModelSigma(2),  AdaptiveModelATR(3),  ConstantSlippage(
    4),  ZeroSlippage(5),  LinearModel(6),  TickSlippage(7),  LinearModelHalfTick(
    8),  KylesLambdaSigma(9),  KylesLambdaATR(10);
  
  private final int Val;
  
  private SlippageModel(int Val) {
    this.Val = Val;
  }
  
  public int getVal() {
    return this.Val;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/backtest/SlippageModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */