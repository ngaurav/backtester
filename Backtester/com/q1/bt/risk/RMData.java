package com.q1.bt.risk;

import com.q1.bt.data.DataTypeViewer;
import com.q1.bt.execution.order.Order;
import java.util.ArrayList;
import java.util.HashMap;



public class RMData
{
  public HashMap<String, ArrayList<Order>> orderBookMap;
  public HashMap<String, HashMap<String, DataTypeViewer>> dataMap;
  public HashMap<String, HashMap<Integer, Integer>> positionMap;
  public HashMap<String, ArrayList<String[]>> tradebookMap;
  public HashMap<String, Double> mtmMap;
  public HashMap<String, Double> capitalMap;
  
  public RMData()
  {
    this.orderBookMap = new HashMap();
    this.dataMap = new HashMap();
    this.positionMap = new HashMap();
    this.tradebookMap = new HashMap();
    this.mtmMap = new HashMap();
    this.capitalMap = new HashMap();
  }
  



  public void update(String key, ArrayList<Order> orderBook, HashMap<String, DataTypeViewer> dVMap, HashMap<Integer, Integer> posMap, ArrayList<String[]> tradeBook, Double mtm, Double capital)
  {
    this.orderBookMap.put(key, orderBook);
    this.dataMap.put(key, dVMap);
    this.positionMap.put(key, posMap);
    this.tradebookMap.put(key, tradeBook);
    this.mtmMap.put(key, mtm);
    this.capitalMap.put(key, capital);
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/risk/RMData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */