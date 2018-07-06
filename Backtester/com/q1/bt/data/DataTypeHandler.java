package com.q1.bt.data;

import com.q1.bt.data.classes.ContractData;
import com.q1.bt.data.classes.FundaData;
import com.q1.bt.data.classes.MetaData;
import com.q1.bt.data.classes.PreProcessData;
import com.q1.bt.data.classes.Scrip;
import com.q1.csv.CSVReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DataTypeHandler
  extends Thread
{
  public HashMap<String, ScripDataHandler> scripDataHandlerMap = new HashMap();
  
  String dataType;
  
  public int scripCount;
  public Long dateTime;
  public boolean eof = false;
  


  public DataTypeHandler(String dataPath, String dataType, ArrayList<Scrip> scripSet, Long startDate, String strategyDataType)
    throws Exception
  {
    this.scripCount = scripSet.size();
    

    this.dataType = dataType;
    

    String dataID = "";
    for (Scrip scrip : scripSet)
    {



      if (dataType.equals("FD"))
      {
        dataID = scrip.segmentName + " " + dataType;
        String dataLocation = dataPath + "/FD/" + dataID + ".csv";
        

        CSVReader dataReader = null;
        dataReader = new CSVReader(dataLocation, ',', 0);
        String[] metaInfoStr = dataReader.getLine();
        Object[] metaInfo = processMetaInfo(metaInfoStr);
        dataReader.close();
        

        dataReader = new CSVReader(dataLocation, ',', startDate.longValue(), 1);
        

        FundaData fD = new FundaData(scrip.segmentName);
        

        ScripDataHandler sDHandler = new ScripDataHandler(dataType, dataReader, fD, metaInfo);
        this.scripDataHandlerMap.put(scrip.scripID, sDHandler);


      }
      else
      {


        dataID = scrip.getDataID(dataType);
        

        if (dataType.equals("MD"))
        {
          String dataLocation = dataPath + "/" + dataType + "/" + dataID + ".csv";
          

          boolean fileExists = true;
          CSVReader dataReader = null;
          try {
            dataReader = new CSVReader(dataLocation, ',', 0);
          }
          catch (IOException e)
          {
            fileExists = false;
          }
          String[] header = dataReader.getLine();
          String[] metaInfoStr = dataReader.getLine();
          Object[] metaInfo = processMetaInfo(metaInfoStr);
          dataReader.close();
          

          dataReader = new CSVReader(dataLocation, ',', 2);
          

          MetaData mD = new MetaData(scrip, header);
          

          ScripDataHandler sDHandler = new ScripDataHandler(dataType, dataReader, mD, fileExists, metaInfo);
          this.scripDataHandlerMap.put(scrip.scripID, sDHandler);


        }
        else if (dataType.equals("PP"))
        {
          String dataLocation = dataPath + "/" + dataType + "/" + dataID + ".csv";
          

          CSVReader dataReader = new CSVReader(dataLocation, ',', 0);
          String[] header = dataReader.getLine();
          String[] metaInfoStr = dataReader.getLine();
          Object[] metaInfo = processMetaInfo(metaInfoStr);
          dataReader.close();
          

          dataReader = new CSVReader(dataLocation, ',', 2);
          

          PreProcessData pD = new PreProcessData(scrip, header);
          

          ScripDataHandler sDHandler = new ScripDataHandler(dataType, dataReader, pD, metaInfo);
          this.scripDataHandlerMap.put(scrip.scripID, sDHandler);

        }
        else
        {

          String dataLocation = dataPath + "/" + scrip.contractType + "/" + dataID + ".csv";
          

          CSVReader dataReader = new CSVReader(dataLocation, ',', 0);
          String[] header = dataReader.getLine();
          String[] metaInfoStr = dataReader.getLine();
          Object[] metaInfo = processMetaInfo(metaInfoStr);
          dataReader.close();
          

          if (dataType.equals(strategyDataType)) {
            dataReader = new CSVReader(dataLocation, ',', startDate.longValue(), 2);
          } else {
            dataReader = new CSVReader(dataLocation, ',', startDate.longValue(), true, 2);
          }
          
          ContractData cD = new ContractData(scrip, header);
          

          ScripDataHandler sDHandler = new ScripDataHandler(dataType, dataReader, cD, metaInfo);
          this.scripDataHandlerMap.put(scrip.scripID, sDHandler);
        }
      }
    }
    

    this.dateTime = Long.valueOf(20910101235900L);
  }
  

  public boolean readData()
    throws Exception
  {
    long curDateTime = 29910101235900L;
    

    this.eof = true;
    

    for (ScripDataHandler sDHandler : this.scripDataHandlerMap.values())
    {




      this.eof &= sDHandler.eof;
      if (!sDHandler.eof)
      {
        long dataDateTime;
        
        long dataDateTime;
        if (sDHandler.noUpdate) {
          dataDateTime = sDHandler.dateTime.longValue();


        }
        else
        {

          String[] dataLine = sDHandler.dataReader.getLine();
          

          if (dataLine == null) {
            sDHandler.eof = true;
            continue;
          }
          



          dataDateTime = getDateTime(dataLine).longValue();
          sDHandler.dateTime = Long.valueOf(dataDateTime);
          sDHandler.dataLine = dataLine;
          

          sDHandler.updateData();
        }
        


        if (dataDateTime < curDateTime) {
          curDateTime = dataDateTime;
        }
      }
    }
    if (this.eof) {
      return false;
    }
    
    this.dateTime = Long.valueOf(curDateTime);
    
    return true;
  }
  

  public void checkUpdateAcrossScrips(long curDateTime)
    throws Exception
  {
    for (ScripDataHandler sDHandler : this.scripDataHandlerMap.values())
    {

      if (sDHandler.dateTime.longValue() > curDateTime) {
        sDHandler.noUpdate = true;

      }
      else
      {
        sDHandler.noUpdate = false;
      }
    }
  }
  
  public void close() throws Exception
  {
    for (ScripDataHandler sDHandler : this.scripDataHandlerMap.values()) {
      sDHandler.close();
    }
  }
  

  public Long getDateTime(String[] dataLine)
  {
    if (this.dataType.equals("MD")) {
      return Long.valueOf(Long.parseLong(dataLine[0] + "000000"));
    }
    

    if (this.dataType.equals("PP")) {
      return Long.valueOf(Long.parseLong(dataLine[0] + "000000"));
    }
    

    if (this.dataType.equals("FD")) {
      return Long.valueOf(Long.parseLong(dataLine[0] + "000000"));
    }
    




    if (this.dataType.contains("M")) {
      return Long.valueOf(Long.parseLong(dataLine[0] + dataLine[1]));
    }
    

    if (this.dataType.contains("D")) {
      return Long.valueOf(Long.parseLong(dataLine[0] + "235900"));
    }
    

    return null;
  }
  
  public Object[] processMetaInfo(String[] metaInfo)
  {
    Long startDate = Long.valueOf(0L);Long endDate = Long.valueOf(0L);
    Integer dataCount = Integer.valueOf(0);
    String[] arrayOfString1;
    int j = (arrayOfString1 = metaInfo).length; for (int i = 0; i < j; i++) { String meta = arrayOfString1[i];
      String[] metaVal = meta.split("\\|");
      String key = metaVal[0];
      String value = metaVal[1];
      

      if (key.equals("Start Date")) {
        startDate = Long.valueOf(Long.parseLong(value));
      }
      else if (key.equals("End Date")) {
        endDate = Long.valueOf(Long.parseLong(value));
      }
      else if (key.equals("Data Count")) {
        dataCount = Integer.valueOf(Integer.parseInt(value));
      }
      else {
        System.out.println("Invalid Meta Information: " + key);
      }
    }
    Object[] metaObj = { startDate, endDate, dataCount };
    
    return metaObj;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/DataTypeHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */