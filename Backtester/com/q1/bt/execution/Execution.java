package com.q1.bt.execution;

import com.q1.bt.data.DataTypeViewer;
import com.q1.bt.execution.order.Order;
import com.q1.bt.process.backtest.SlippageModel;
import com.q1.csv.CSVWriter;
import com.q1.math.MathLib;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;






public class Execution
{
  public Double candleMTM = Double.valueOf(0.0D); public Double cumCandleMTM = Double.valueOf(0.0D); public Double prevCumCandleMTM = Double.valueOf(0.0D);
  public Double buy = Double.valueOf(NaN.0D); public Double sell = Double.valueOf(NaN.0D);
  

  public HashMap<String, Long> positionMap = new HashMap();
  public HashMap<String, Double> mtmMap = new HashMap();
  public HashMap<String, Double> dayMTMMap = new HashMap();
  Double prevCl = null;
  




  HashMap<String, CSVWriter> scripTradebookWriterMap = new HashMap();
  
  public HashMap<String, ArrayList<String[]>> scripTradeBookMap = new HashMap();
  
  public HashMap<String, ScripExecution> executionMap = new HashMap();
  public DataTypeViewer mainData;
  public DataTypeViewer metaData;
  Double capital;
  
  public Execution(String scripListID, Double capital, HashMap<String, CSVWriter> scripTradebookWriterMap, SlippageModel slippageModel, RolloverMethod rolloverMethod, HashMap<String, ScripExecution> executionMap) { this.capital = capital;
    this.scripTradebookWriterMap = scripTradebookWriterMap;
    this.slippageModel = slippageModel;
    this.rolloverMethod = rolloverMethod;
    this.scripListID = scripListID;
    this.executionMap = executionMap;
  }
  
  public void updateWriter(HashMap<String, CSVWriter> scripTradebookWriterMap)
  {
    this.scripTradebookWriterMap = scripTradebookWriterMap;
  }
  
  SlippageModel slippageModel;
  RolloverMethod rolloverMethod;
  String scripListID;
  public void processOrders(DataTypeViewer mainData, HashMap<String, DataTypeViewer> dataMap, ArrayList<Order> orderBook, Long prevOrderTimestamp) throws Exception
  {
    this.mainData = mainData;
    


    String scripID = null;
    CSVWriter tbWriter; for (String curScripID : mainData.scripDataViewerMap.keySet())
    {

      scripID = curScripID;
      

      tbWriter = (CSVWriter)this.scripTradebookWriterMap.get(scripID);
      

      if (!this.executionMap.containsKey(scripID)) {
        ScripExecution scripExec = new ScripExecution(tbWriter, this.slippageModel, this.rolloverMethod);
        this.executionMap.put(scripID, scripExec);
      }
    }
    


    HashMap<String, ArrayList<Order>> orderBookMap = new HashMap();
    for (Order order : orderBook)
    {

      if (mainData.scripCount == 1) {
        order.scripID = scripID;
      }
      
      ArrayList<Order> curOrderList = new ArrayList();
      if (orderBookMap.containsKey(order.scripID)) {
        curOrderList = (ArrayList)orderBookMap.get(order.scripID);
      }
      
      curOrderList.add(order);
      orderBookMap.put(order.scripID, curOrderList);
    }
    

    Double candle$MTM = Double.valueOf(0.0D);
    this.buy = (this.sell = Double.valueOf(NaN.0D));
    Double usedCapital = Double.valueOf(0.0D);
    for (Map.Entry<String, ScripExecution> entry : this.executionMap.entrySet())
    {

      scripID = (String)entry.getKey();
      ScripExecution scripExec = (ScripExecution)entry.getValue();
      

      ArrayList<Order> scripIDOrderBook = (ArrayList)orderBookMap.get(scripID);
      

      if (scripIDOrderBook == null) {
        scripIDOrderBook = new ArrayList();
      }
      
      ExecutionData execData = new ExecutionData(this.scripListID, scripID, mainData, dataMap, prevOrderTimestamp);
      

      scripExec.processOrders(execData, scripIDOrderBook, this.capital);
      

      candle$MTM = Double.valueOf(candle$MTM.doubleValue() + scripExec.$TradeMTM.doubleValue());
      

      this.positionMap.put(scripID, scripExec.position);
      

      this.mtmMap.put(scripID, Double.valueOf(scripExec.$TradeMTM.doubleValue() / this.capital.doubleValue()));
      

      this.dayMTMMap.put(scripID, Double.valueOf((scripExec.$CumMTM.doubleValue() - scripExec.$PrevDayCumMTM.doubleValue()) / this.capital.doubleValue()));
      

      this.scripTradeBookMap.put(scripID, scripExec.tradeBook);
      

      usedCapital = Double.valueOf(usedCapital.doubleValue() + scripExec.usedCapital.doubleValue());
      

      if (MathLib.doubleCompare(usedCapital, this.capital).intValue() > 0.0D) {
        throw new Exception(this.scripListID + ": Capital has been exceeded, Please check your orders!");
      }
    }
    

    this.candleMTM = Double.valueOf(candle$MTM.doubleValue() / this.capital.doubleValue());
    this.cumCandleMTM = Double.valueOf(this.cumCandleMTM.doubleValue() + this.candleMTM.doubleValue());
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/Execution.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */