package com.q1.bt.machineLearning.absclasses;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class IntradayVarList
{
  public abstract ArrayList<Factor> getFactorList(HashMap<String, CandleIndVar> paramHashMap);
  
  public abstract HashMap<String, CandleIndVar> getNormalizerList();
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/IntradayVarList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */