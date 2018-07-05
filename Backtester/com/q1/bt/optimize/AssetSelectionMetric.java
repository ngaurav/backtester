package com.q1.bt.optimize;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AssetSelectionMetric
{
  public abstract HashMap<String, Double> metricCalculate(ArrayList<String> paramArrayList, ArrayList<Long> paramArrayList1, ArrayList<ArrayList<Double>> paramArrayList2, int paramInt);
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/AssetSelectionMetric.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */