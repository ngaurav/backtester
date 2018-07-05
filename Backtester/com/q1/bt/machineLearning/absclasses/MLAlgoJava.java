package com.q1.bt.machineLearning.absclasses;

import java.util.ArrayList;

public abstract class MLAlgoJava
  extends MLAlgo
{
  public abstract ArrayList<Double> runModel(ArrayList<ArrayList<Double>> paramArrayList, ArrayList<Double> paramArrayList1, boolean paramBoolean1, boolean paramBoolean2, String paramString)
    throws Exception;
  
  public abstract void initialize(ArrayList<Factor> paramArrayList1, ArrayList<Factor> paramArrayList2, String paramString1, String paramString2);
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/MLAlgoJava.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */