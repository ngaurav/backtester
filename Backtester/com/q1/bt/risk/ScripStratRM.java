package com.q1.bt.risk;

import com.q1.bt.data.DataTypeViewer;
import com.q1.bt.execution.order.Order;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class ScripStratRM
{
  String scripID;
  String strategyID;
  
  public ScripStratRM(String scripID, String strategyID)
  {
    this.scripID = scripID;
    this.strategyID = strategyID;
  }
  
  public abstract ArrayList<Order> runRM(ArrayList<Order> paramArrayList, HashMap<String, DataTypeViewer> paramHashMap, HashMap<Integer, Integer> paramHashMap1, ArrayList<String[]> paramArrayList1, Double paramDouble1, Double paramDouble2);
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/risk/ScripStratRM.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */