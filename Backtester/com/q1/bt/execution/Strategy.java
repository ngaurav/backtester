package com.q1.bt.execution;

import com.q1.bt.data.DataTypeViewer;
import com.q1.bt.execution.order.Order;
import com.q1.bt.postprocess.PostProcessData;
import com.q1.math.MathLib;
import java.util.ArrayList;
import java.util.HashMap;







public abstract class Strategy
{
  public String strategyID;
  
  public abstract ArrayList<Order> processStrategy(HashMap<String, DataTypeViewer> paramHashMap, HashMap<String, Long> paramHashMap1, HashMap<String, Double> paramHashMap2, HashMap<String, Double> paramHashMap3, HashMap<String, ArrayList<String[]>> paramHashMap4, Double paramDouble1, Double paramDouble2);
  
  public abstract ArrayList<String[]> getParameterList();
  
  public abstract ArrayList<PostProcessData> getPostProcessData();
  
  public Long getMaxQuantity(Double price, Double capital, Double lotSize, Integer maxLeverage)
  {
    return Long.valueOf(MathLib.floorTick(capital.doubleValue() * maxLeverage.intValue() / price.doubleValue(), lotSize.doubleValue()));
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/Strategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */