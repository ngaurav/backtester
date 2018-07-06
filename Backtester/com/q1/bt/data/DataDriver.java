package com.q1.bt.data;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.csv.CSVReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class DataDriver
{
  public HashMap<String, DataTypeHandler> dataTypeHandlerMap = new HashMap();
  

  public HashMap<String, DataTypeViewer> dataTypeViewerMap = new HashMap();
  
  String scripListID;
  
  ArrayList<Scrip> scripSet;
  String strategyDataType;
  public Long strategyDate = Long.valueOf(-100L);
  

  BacktesterGlobal btGlobal;
  
  Backtest btObj;
  
  public Long curDateTime = Long.valueOf(29910101235900L); public Long prevDateTime = Long.valueOf(29910101235900L);
  public Long curDate = Long.valueOf(29910101L);
  


  public DataDriver(BacktesterGlobal btGlobal, Long startDate, Backtest btObj, String scripListID, ArrayList<Scrip> scripSet, String strategyDataType)
    throws Exception
  {
    this.btGlobal = btGlobal;
    this.btObj = btObj;
    this.scripListID = scripListID;
    this.scripSet = new ArrayList(scripSet);
    this.strategyDataType = strategyDataType;
    

    createDataTypeObjects(startDate);
  }
  

  public void createDataTypeObjects(Long startDate)
    throws Exception
  {
    if (this.strategyDataType.contains("M"))
    {

      createHandlerAndViewer(this.strategyDataType, startDate);
      

      createHandlerAndViewer("1D", startDate);
      

      createHandlerAndViewer("MD", startDate);
      

      createHandlerAndViewer("PP", startDate);
      

      createHandlerAndViewer("FD", startDate);
    }
    else if (this.strategyDataType.contains("D"))
    {

      createHandlerAndViewer(this.strategyDataType, startDate);
      

      createHandlerAndViewer("MD", startDate);
      

      createHandlerAndViewer("PP", startDate);
      

      createHandlerAndViewer("FD", startDate);
    }
  }
  


  public void createHandlerAndViewer(String dataType, Long startDate)
    throws Exception
  {
    try
    {
      DataTypeHandler dataHandler = new DataTypeHandler(this.btGlobal.loginParameter.getDataPath(), dataType, this.scripSet, 
        startDate, this.strategyDataType);
      this.dataTypeHandlerMap.put(dataType, dataHandler);
    }
    catch (Exception e)
    {
      if ((dataType.equals("1M")) || (dataType.equals("1D"))) {
        e.printStackTrace();
      }
    }
    try
    {
      DataTypeViewer dataViewer = new DataTypeViewer(this.btGlobal, dataType, this.scripSet, this.strategyDataType);
      this.dataTypeViewerMap.put(dataType, dataViewer);

    }
    catch (Exception e)
    {
      this.btGlobal.displayMessage("Error Creating Data Type Viewer " + this.scripListID + " " + dataType);
      e.printStackTrace();
    }
  }
  

  public boolean updateData()
    throws Exception
  {
    this.curDateTime = Long.valueOf(29910101235900L);
    
    DataTypeHandler dtHandler;
    for (Map.Entry<String, DataTypeHandler> entry : this.dataTypeHandlerMap.entrySet())
    {

      String dataType = (String)entry.getKey();
      dtHandler = (DataTypeHandler)entry.getValue();
      

      try
      {
        if ((!dataType.equals("MD")) && 
          (dtHandler.eof)) {
          return false;
        }
        

        if (!dtHandler.readData()) {
          ((DataTypeViewer)this.dataTypeViewerMap.get(dataType)).updateEOF(true);



        }
        else if (dtHandler.dateTime.longValue() < this.curDateTime.longValue()) {
          this.curDateTime = dtHandler.dateTime;
        }
      } catch (IOException e) {
        this.btGlobal.displayMessage("Error reading datapoint for: " + this.scripListID + " " + dataType);
        e.printStackTrace();
      }
    }
    


    for (DataTypeHandler dtHandler : this.dataTypeHandlerMap.values()) {
      dtHandler.checkUpdateAcrossScrips(this.curDateTime.longValue());
    }
    

    if (this.strategyDataType.contains("M"))
    {

      DataTypeHandler mHandler = (DataTypeHandler)this.dataTypeHandlerMap.get(this.strategyDataType);
      

      DataTypeHandler dHandler = (DataTypeHandler)this.dataTypeHandlerMap.get("1D");
      

      for (Map.Entry<String, ScripDataHandler> entry : mHandler.scripDataHandlerMap.entrySet())
      {
        String scripID = (String)entry.getKey();
        ScripDataHandler sdHandlerM = (ScripDataHandler)entry.getValue();
        
        String[] forwardDateTime = { "", "" };
        String forwardDate = "";
        try {
          forwardDateTime = sdHandlerM.dataReader.getLastReadDateTime();
          forwardDate = forwardDateTime[0];
        }
        catch (NullPointerException localNullPointerException) {}
        
        String minuteDateStr = sdHandlerM.date.toString();
        

        if ((!forwardDate.equals(minuteDateStr)) && (sdHandlerM.dateTime.equals(this.curDateTime)))
        {

          ScripDataHandler sdhandlerD = (ScripDataHandler)dHandler.scripDataHandlerMap.get(scripID);
          

          if (minuteDateStr.equals(sdhandlerD.date.toString()))
          {

            long dateTime = sdhandlerD.updateRolloverData(sdHandlerM).longValue();
            

            if (dateTime < dHandler.dateTime.longValue()) {
              dHandler.dateTime = Long.valueOf(dateTime);
            }
          } else {
            throw new Exception("Daily Data Date Missing: " + this.scripListID + " " + scripID);
          }
        }
      }
    }
    



    this.curDate = Long.valueOf(this.curDateTime.longValue() / 1000000L);
    

    if (this.curDate.longValue() > this.btObj.backtestParameter.getEndDate()) {
      return false;
    }
    
    if (this.curDateTime.equals(Long.valueOf(29910101235900L))) {
      return false;
    }
    

    this.prevDateTime = this.curDateTime;
    

    this.strategyDate = Long.valueOf(((DataTypeHandler)this.dataTypeHandlerMap.get(this.strategyDataType)).dateTime.longValue() / 1000000L);
    

    return true;
  }
  


  public void updateDataViewers()
  {
    for (DataTypeHandler dtHandler : this.dataTypeHandlerMap.values())
    {

      if (dtHandler.dateTime.equals(this.curDateTime)) {
        ((DataTypeViewer)this.dataTypeViewerMap.get(dtHandler.dataType)).updateData(dtHandler);

      }
      else
      {
        ((DataTypeViewer)this.dataTypeViewerMap.get(dtHandler.dataType)).updateData(this.curDateTime);
      }
    }
  }
  

  public String getDataIDs(String[] scripIdList, String dataType)
  {
    String dataIDs = "";
    String[] arrayOfString; int j = (arrayOfString = scripIdList).length; for (int i = 0; i < j; i++) { String scripID = arrayOfString[i];
      if (dataIDs == "") {
        dataIDs = scripID + " " + dataType;
      } else
        dataIDs = dataIDs + "," + scripID + " " + dataType;
    }
    return dataIDs;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/DataDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */