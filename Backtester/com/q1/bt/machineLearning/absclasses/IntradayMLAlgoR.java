package com.q1.bt.machineLearning.absclasses;

import java.util.ArrayList;

public abstract class IntradayMLAlgoR
{
  public abstract String getModelPackage();
  
  public abstract String getModelName();
  
  public abstract ArrayList<String[]> getParameterList();
  
  public abstract IntradayMLAlgoR copy();
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/IntradayMLAlgoR.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */