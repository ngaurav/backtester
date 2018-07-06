package com.q1.bt.process.objects;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.execution.ParamObject;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.bt.process.parameter.PackageParameter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;






public class Backtest
{
  public HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap;
  public HashMap<String, ArrayList<String[]>> strategyParameterMap;
  public BacktestParameter backtestParameter;
  public String timeStamp;
  public boolean fileBacktest = false;
  
  public String orderBookPath;
  

  public Backtest(BacktestParameter backtestParameter, HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap, HashMap<String, ArrayList<String[]>> strategyParameterMap)
  {
    this.backtestParameter = backtestParameter;
    this.backtestMap = new HashMap(backtestMap);
    this.strategyParameterMap = new HashMap(strategyParameterMap);
  }
  
  public Backtest(BacktestParameter backtestParameter, String orderBookPath) throws Exception {
    this.backtestParameter = backtestParameter;
    this.backtestMap = createBacktestMapFromFolder(orderBookPath);
    this.strategyParameterMap = new HashMap();
    this.orderBookPath = orderBookPath;
    this.fileBacktest = true;
  }
  
  public void setStrategyParametersAsDefault(PackageParameter packageParameter)
  {
    this.strategyParameterMap = new HashMap();
    
    for (String strategyID : this.backtestMap.keySet())
    {

      ArrayList<String[]> defaultParameters = new ArrayList();
      try {
        defaultParameters = ParamObject.getParameterList(packageParameter.getStrategyPackage(), strategyID);
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
      

      this.strategyParameterMap.put(strategyID, defaultParameters);
    }
  }
  

  public HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> createBacktestMapFromFolder(String folderPath)
    throws Exception
  {
    HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = new HashMap();
    
    File folderPathFile = new File(folderPath);
    
    File[] backtestTable = folderPathFile.listFiles();
    int backtestCount = backtestTable.length;
    

    for (int i = 0; i < backtestCount; i++)
    {

      String[] backtestVal = backtestTable[i].getName().split(" ");
      String strategyID = backtestVal[0];
      String scripListID = backtestVal[1];
      
      File[] scripListTable = backtestTable[i].listFiles();
      

      ArrayList<Scrip> scripSet = new ArrayList();
      
      File[] arrayOfFile1;
      int j = (arrayOfFile1 = scripListTable).length; for (int i = 0; i < j; i++) { File scripFile = arrayOfFile1[i];
        String scripID = scripFile.getName().substring(0, scripFile.getName().length() - 14);
        scripSet.add(new Scrip(scripID));
      }
      

      LinkedHashMap<String, ArrayList<Scrip>> scripListMap = (LinkedHashMap)backtestMap.get(strategyID);
      if (scripListMap == null) {
        scripListMap = new LinkedHashMap();
        scripListMap.put(scripListID, scripSet);
        backtestMap.put(strategyID, scripListMap);
      } else {
        scripListMap.put(scripListID, scripSet);
        backtestMap.put(strategyID, scripListMap);
      }
    }
    

    return backtestMap;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/objects/Backtest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */