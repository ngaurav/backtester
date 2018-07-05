package com.q1.bt.machineLearning.absclasses;

import java.util.ArrayList;

public abstract class MLAlgo
{
  public abstract MLAlgo copy();
  
  public abstract void close();
  
  public abstract ArrayList<String[]> getParameterList();
  
  public abstract String getModelName();
  
  public abstract String getModelPackage();
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/MLAlgo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */