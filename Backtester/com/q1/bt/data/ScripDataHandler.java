package com.q1.bt.data;

import com.q1.bt.data.classes.ContractData;
import com.q1.bt.data.classes.FundaData;
import com.q1.bt.data.classes.MetaData;
import com.q1.bt.data.classes.PreProcessData;
import com.q1.csv.CSVReader;
import java.util.HashMap;






public class ScripDataHandler
  extends Thread
{
  CSVReader dataReader;
  String[] dataLine;
  String dataType;
  HashMap<String, Long> dateTimeMap = new HashMap();
  
  public Long date;
  
  public Long time;
  
  public Long dateTime;
  ContractData contractData = null;
  MetaData metaData = null;
  PreProcessData preData = null;
  FundaData fundaData = null;
  
  public Long startDate;
  
  public Long endDate;
  
  public int dataCount;
  public boolean eof = false;
  public boolean noUpdate = false;
  public boolean fileExists = true;
  
  public ScripDataHandler(String dataType, CSVReader dataReader, MetaData metaData, boolean fileExists, Object[] metaInfo)
  {
    this.dataReader = dataReader;
    this.metaData = metaData;
    this.fileExists = fileExists;
    this.dataType = dataType;
    processMetaInfo(metaInfo);
  }
  
  public ScripDataHandler(String dataType, CSVReader dataReader, ContractData contractData, Object[] metaInfo)
  {
    this.dataReader = dataReader;
    this.contractData = contractData;
    this.dataType = dataType;
    processMetaInfo(metaInfo);
  }
  
  public ScripDataHandler(String dataType, CSVReader dataReader, PreProcessData preData, Object[] metaInfo)
  {
    this.dataReader = dataReader;
    this.preData = preData;
    this.dataType = dataType;
    processMetaInfo(metaInfo);
  }
  
  public ScripDataHandler(String dataType, CSVReader dataReader, FundaData fundaData, Object[] metaInfo)
  {
    this.dataReader = dataReader;
    this.fundaData = fundaData;
    this.dataType = dataType;
    processMetaInfo(metaInfo);
  }
  
  public void close() throws Exception
  {
    this.dataReader.close();
  }
  

  public void updateData()
    throws Exception
  {
    if (this.dataType.equals("MD"))
    {

      this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
      

      this.metaData.addData(this.dataLine);


    }
    else if (this.dataType.equals("PP"))
    {

      this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
      

      this.preData.addData(this.dataLine);


    }
    else if (this.dataType.equals("FD"))
    {

      this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
      

      this.fundaData.addData(this.dataLine);


    }
    else
    {

      if (this.dataType.contains("M")) {
        this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
        this.time = Long.valueOf(Long.parseLong(this.dataLine[1]));


      }
      else if (this.dataType.contains("D")) {
        this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
      }
      

      this.contractData.addContractData(this.dataLine);
    }
  }
  

  public Long updateRolloverData(ScripDataHandler sdHandlerM)
  {
    this.dateTime = sdHandlerM.dateTime;
    this.time = sdHandlerM.time;
    this.noUpdate = false;
    
    return this.dateTime;
  }
  


  public Long updateRolloverData(String[] forwardDateTime)
  {
    this.dateTime = Long.valueOf(Long.parseLong(forwardDateTime[0] + forwardDateTime[1]));
    this.time = Long.valueOf(Long.parseLong(forwardDateTime[1]));
    this.noUpdate = false;
    
    return this.dateTime;
  }
  

  public void processMetaInfo(Object[] metaInfo)
  {
    this.startDate = ((Long)metaInfo[0]);
    this.endDate = ((Long)metaInfo[1]);
    this.dataCount = ((Integer)metaInfo[2]).intValue();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/ScripDataHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */