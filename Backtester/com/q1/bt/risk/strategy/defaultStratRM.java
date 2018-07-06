package com.q1.bt.risk.strategy;

import com.q1.bt.risk.RMData;
import com.q1.bt.risk.StrategyRM;

public class defaultStratRM extends StrategyRM
{
  public defaultStratRM(String strategyID)
  {
    super(strategyID);
  }
  

  public RMData runRM(RMData rmData)
  {
    return rmData;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/risk/strategy/defaultStratRM.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */