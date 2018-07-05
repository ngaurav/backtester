package com.q1.bt.machineLearning.absclasses;

import java.util.ArrayList;

public abstract class IntradayMLAlgo
{
  public abstract ArrayList<Double> runModel(ArrayList<ArrayList<Double>> paramArrayList, ArrayList<Double> paramArrayList1, boolean paramBoolean1, boolean paramBoolean2, String paramString)
    throws Exception;
  
  public abstract ArrayList<String[]> getParameterList();
  
  public abstract void initialize(ArrayList<IntradayFactor> paramArrayList, String paramString1, String paramString2);
  
  public abstract IntradayMLAlgo copy();
  
  public abstract void close();
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/IntradayMLAlgo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */