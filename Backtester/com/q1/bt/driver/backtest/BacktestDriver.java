package com.q1.bt.driver.backtest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.q1.bt.data.DataTypeViewer;
import com.q1.bt.data.classes.Scrip;
import com.q1.bt.execution.Execution;
import com.q1.bt.execution.ScripExecution;
import com.q1.bt.execution.Strategy;
import com.q1.bt.execution.order.Order;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.postprocess.OutputShape;
import com.q1.bt.postprocess.PostProcessData;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;


public class BacktestDriver
{
  BacktesterGlobal btGlobal;
  Backtest btObj;
  String scripListID;
  public TreeMap<Long, Double> dailyMTM = new TreeMap();
  
  public Long startDate;
  
  public Long endDate;
  
  public String orderBookPath;
  public String postProcessPath;
  HashMap<String, DataTypeViewer> dataMap;
  DataTypeViewer mainData;
  DataTypeViewer rolloverData = null;
  Long prevRolloverDate = Long.valueOf(0L);
  boolean rollover = false;
  
  Strategy strategy;
  Execution execution;
  int funCall = 0;
  
  Long prevDateTime;
  
  String backtestKey;
  
  TreeMap<String, TreeMap<String, PostProcessData>> postProcessMap = new TreeMap();
  HashMap<String, CSVWriter> scripTradebookWriterMap = new HashMap();
  HashMap<String, CSVWriter> scripPostProcessWriterMap = new HashMap();
  boolean ppHeaderCheck = false;
  

  ArrayList<Long> dailyDates = new ArrayList();
  ArrayList<Order> orderBook = new ArrayList();
  Long prevOrderTimestamp = Long.valueOf(0L);
  


  public BacktestDriver(String scripListID, ArrayList<Scrip> scripSet, Strategy strategy, HashMap<String, DataTypeViewer> dataMap, BacktesterGlobal btGlobal, Backtest backtest, Long startDate, Long endDate)
    throws IOException
  {
    this.btGlobal = btGlobal;
    this.btObj = backtest;
    

    this.dataMap = dataMap;
    

    String[] strategyVal = strategy.strategyID.split("_");
    String mainDataType = strategyVal[1];
    this.mainData = ((DataTypeViewer)dataMap.get(mainDataType));
    
    if (mainDataType.contains("M")) {
      this.rolloverData = ((DataTypeViewer)dataMap.get("1D"));
    }
    
    this.scripListID = scripListID;
    this.strategy = strategy;
    this.backtestKey = (strategy.strategyID + " " + scripListID);
    this.startDate = startDate;
    this.endDate = endDate;
    

    this.scripTradebookWriterMap = new HashMap();
    
    String tbPath = btGlobal.loginParameter.getOutputPath() + "/" + backtest.timeStamp + "/Trade Data" + "/" + 
      this.backtestKey;
    tbPath = createDataPath(tbPath);
    
    HashMap<String, ScripExecution> executionMap = new HashMap();
    
    for (Scrip scrip : scripSet) {
      try {
        CSVWriter writer = new CSVWriter(tbPath + "/" + scrip.scripID + " Tradebook.csv", false, ",");
        

        ScripExecution scripExec = new ScripExecution(writer, backtest.backtestParameter.getSlippageModel(), 
          backtest.backtestParameter.getRolloverMethod());
        executionMap.put(scrip.scripID, scripExec);
        
        this.scripTradebookWriterMap.put(scrip.scripID, writer);
      } catch (IOException e1) {
        btGlobal.displayMessage("Error creating tradebook writer for: " + this.backtestKey + ", " + scrip.scripID);
        e1.printStackTrace();
      }
    }
    

    this.execution = new Execution(scripListID, btGlobal.loginParameter.getCapital(), this.scripTradebookWriterMap, 
      backtest.backtestParameter.getSlippageModel(), backtest.backtestParameter.getRolloverMethod(), 
      executionMap);
    

    this.prevDateTime = this.mainData.dateTime;
    

    this.postProcessPath = createDataPath(btGlobal.loginParameter.getOutputPath() + "/" + backtest.timeStamp + 
      "/Post Process Data" + "/" + this.backtestKey);
    

    if (backtest.backtestParameter.isGenerateOutputCheck()) {
      for (PostProcessData ppData : strategy.getPostProcessData()) {
        updatePostProcessMap(ppData, ((Scrip)scripSet.get(0)).scripID);
      }
    }
    
    for (Scrip scrip : scripSet) {
      try {
        CSVWriter writer = new CSVWriter(this.postProcessPath + "/" + scrip.scripID + " Post Process.csv", false, 
          ",");
        
        this.scripPostProcessWriterMap.put(scrip.scripID, writer);
      } catch (IOException e1) {
        btGlobal.displayMessage("Error creating output writer for: " + this.backtestKey + ", " + scrip.scripID);
        e1.printStackTrace();
      }
    }
  }
  
  public PostProcessData getPostProcessData(String scripID, String outputName)
  {
    TreeMap<String, PostProcessData> scripPostProcessMap = (TreeMap)this.postProcessMap.get(scripID);
    

    if (scripPostProcessMap == null) {
      return null;
    }
    

    return (PostProcessData)scripPostProcessMap.get(outputName);
  }
  


  public void updatePostProcessMap(PostProcessData ppData, String scripID)
  {
    if (ppData.scripID == null) {
      ppData.scripID = scripID;
    }
    
    TreeMap<String, PostProcessData> scripPostProcessMap = (TreeMap)this.postProcessMap.get(ppData.scripID);
    

    if (scripPostProcessMap == null) {
      scripPostProcessMap = new TreeMap();
      scripPostProcessMap.put(ppData.getFileHeader(), ppData);
      this.postProcessMap.put(ppData.scripID, scripPostProcessMap);

    }
    else
    {
      scripPostProcessMap.put(ppData.getFileHeader(), ppData);
    }
  }
  
  public void runBacktest()
    throws Exception
  {
    if (((this.mainData.date.longValue() > this.startDate.longValue()) || (this.mainData.date.equals(this.startDate))) && (
      (this.mainData.date.longValue() < this.endDate.longValue()) || (this.mainData.date.equals(this.endDate))))
    {

      if (!this.mainData.skipBacktest)
      {
        this.execution.processOrders(this.mainData, this.dataMap, this.orderBook, this.prevOrderTimestamp);
      }
      

      if (!this.mainData.skipBacktest)
      {

        if (this.btObj.backtestParameter.isGenerateOutputCheck())
        {

          for (Map.Entry<String, ScripExecution> scripEntry : this.execution.executionMap.entrySet())
          {
            String scripID = (String)scripEntry.getKey();
            ScripExecution scripExec = (ScripExecution)scripEntry.getValue();
            

            PostProcessData ppData = getPostProcessData(scripID, "MTM");
            if (ppData == null) {
              ppData = new PostProcessData(scripID, "MTM", OutputShape.Line, 2);
              updatePostProcessMap(ppData, scripID);
            }
            

            Double mtm = Double.valueOf(scripExec.$TradeMTM.doubleValue() / this.btGlobal.loginParameter.getCapital().doubleValue());
            ppData.updateValue(mtm);
          }
          


          if (!this.ppHeaderCheck)
            this.ppHeaderCheck = writePostProcessHeader();
          writePostProcessData(this.mainData.dateTime);
        }
        


        this.orderBook = this.strategy.processStrategy(this.dataMap, this.execution.positionMap, this.execution.mtmMap, 
          this.execution.dayMTMMap, this.execution.scripTradeBookMap, this.btGlobal.loginParameter.getCapital(), 
          this.btGlobal.loginParameter.getRiskPerTrade());
        this.prevOrderTimestamp = this.mainData.dateTime;
      }
    }
    



    this.prevDateTime = this.mainData.dateTime;
  }
  
  public void closeFileStreams()
  {
    try
    {
      for (CSVWriter writer : this.scripPostProcessWriterMap.values())
        writer.close();
      for (CSVWriter writer : this.scripTradebookWriterMap.values())
        writer.close();
    } catch (IOException e) {
      this.btGlobal.displayMessage("Could not close filewriters: " + this.scripListID);
      e.printStackTrace();
    }
  }
  

  public boolean writePostProcessHeader()
  {
    for (Map.Entry<String, CSVWriter> entry : this.scripPostProcessWriterMap.entrySet())
    {
      String scripID = (String)entry.getKey();
      CSVWriter writer = (CSVWriter)entry.getValue();
      TreeMap<String, PostProcessData> scripPostProcessMap = (TreeMap)this.postProcessMap.get(scripID);
      
      ArrayList<String> header = new ArrayList();
      

      header.add("Date");
      header.add("Time");
      
      for (String outputName : scripPostProcessMap.keySet()) {
        header.add(outputName);
      }
      try
      {
        writer.writeLine(header);
      } catch (IOException e1) {
        this.btGlobal.displayMessage("Error writing Post Process Data: " + this.scripListID + ", " + scripID);
        e1.printStackTrace();
      }
    }
    

    return true;
  }
  
  public void writePostProcessData(Long dateTime)
  {
    String dateTimeStr = dateTime.toString();
    String date = dateTimeStr.substring(0, 8);
    String time = dateTimeStr.substring(8);
    

    for (Map.Entry<String, CSVWriter> entry : this.scripPostProcessWriterMap.entrySet())
    {
      String scripID = (String)entry.getKey();
      CSVWriter writer = (CSVWriter)entry.getValue();
      TreeMap<String, PostProcessData> scripPostProcessMap = (TreeMap)this.postProcessMap.get(scripID);
      
      ArrayList<String> dataLine = new ArrayList();
      


      dataLine.add(date);
      dataLine.add(time);
      

      for (PostProcessData ppData : scripPostProcessMap.values()) {
        dataLine.add(ppData.outputValue);
      }
      try
      {
        writer.writeLine(dataLine);
      } catch (IOException e1) {
        this.btGlobal.displayMessage("Error writing Post Process Data: " + this.scripListID + ", " + scripID);
        e1.printStackTrace();
      }
    }
  }
  
  public static String createDataPath(String folder)
  {
    if (!new File(folder).exists())
      new File(folder).mkdirs();
    return folder;
  }
  
  public static void createPath(String folder)
  {
    if (!new File(folder).exists()) {
      new File(folder).mkdirs();
    }
  }
  
  public void serializeObjects() throws IOException
  {
    String obPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.btObj.timeStamp + "/Object Data";
    createPath(obPath);
    

    Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues()
      .excludeFieldsWithModifiers(new int[] {128 }).create();
    FileWriter startWriter = new FileWriter(obPath + "/" + this.backtestKey + " Strategy.json");
    gson.toJson(this.strategy, startWriter);
    startWriter.close();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/backtest/BacktestDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */