package com.q1.bt.machineLearning.utility;

import com.q1.bt.data.DataDriver;
import com.q1.bt.data.DataTypeViewer;
import com.q1.bt.data.ScripDataViewer;
import com.q1.bt.data.classes.Contract;
import com.q1.bt.data.classes.ContractData;
import com.q1.bt.data.classes.MetaData;
import com.q1.bt.data.classes.Scrip;
import com.q1.bt.process.objects.Backtest;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DailyDataReader
{
  Long currentDate;
  Contract cur1DData;
  MetaData curMDData;
  Contract prev1DData;
  MetaData prevMDData;
  private Long mtmDate;
  private boolean startFileFlag = true;
  
  com.q1.bt.global.BacktesterGlobal btGlobal;
  
  Backtest backtest;
  DataDriver dataDriver;
  Scrip scrip;
  
  public DailyDataReader(Long startDate, com.q1.bt.global.BacktesterGlobal btGlobal, Backtest backtest, Scrip scrip, String scripListName)
    throws java.io.IOException
  {
    this.btGlobal = btGlobal;
    this.backtest = backtest;
    this.scrip = scrip;
    


    ArrayList<Scrip> scripSet = new ArrayList();
    scripSet.add(scrip);
    try
    {
      this.dataDriver = new DataDriver(btGlobal, startDate, backtest, scripListName, scripSet, "1D");
    } catch (Exception e) {
      System.out.println("Error in creation of data driver in ML Stage");
      e.printStackTrace();
    }
    this.currentDate = Long.valueOf(0L);
    this.cur1DData = null;
    this.curMDData = null;
    this.prev1DData = null;
    this.prevMDData = null;
    this.mtmDate = Long.valueOf(0L);
  }
  



  private void processFirstDateData()
    throws Exception
  {
    if ((this.cur1DData == null) && (this.startFileFlag)) {
      this.startFileFlag = false;
      
      this.dataDriver.updateData();
      this.dataDriver.updateDataViewers();
      
      DataTypeViewer dailyDataTypeViewer = (DataTypeViewer)this.dataDriver.dataTypeViewerMap.get("1D");
      ContractData dailyContractData = ((ScripDataViewer)dailyDataTypeViewer.scripDataViewerMap.get(this.scrip.scripID)).contractData;
      this.prev1DData = dailyContractData.contract;
      
      DataTypeViewer metaDtaTypeViewer = (DataTypeViewer)this.dataDriver.dataTypeViewerMap.get("MD");
      this.prevMDData = ((ScripDataViewer)metaDtaTypeViewer.scripDataViewerMap.get(this.scrip.scripID)).metaData;
      
      if (this.prev1DData == null) {
        System.out.println("No data in Daily File");
        return;
      }
      if (this.prevMDData == null) {
        System.out.println("No data in Metadata File");
        return;
      }
    }
  }
  
  private void updateDataDriver()
  {
    this.dataDriver.updateDataViewers();
    DataTypeViewer dailyDataTypeViewer = (DataTypeViewer)this.dataDriver.dataTypeViewerMap.get("1D");
    ContractData dailyContractData = ((ScripDataViewer)dailyDataTypeViewer.scripDataViewerMap.get(this.scrip.scripID)).contractData;
    this.cur1DData = dailyContractData.contract;
    
    DataTypeViewer metaDtaTypeViewer = (DataTypeViewer)this.dataDriver.dataTypeViewerMap.get("MD");
    this.curMDData = ((ScripDataViewer)metaDtaTypeViewer.scripDataViewerMap.get(this.scrip.scripID)).metaData;
    
    this.currentDate = dailyDataTypeViewer.date;
  }
  
  public void process(Long dataDate, CandleData data)
    throws Exception
  {
    process(dataDate, data, null);
  }
  
  public void process(Long dataDate, CandleData data, Double mtm) throws Exception {
    processFirstDateData();
    if (this.currentDate.longValue() > dataDate.longValue())
      return;
    if (this.currentDate.longValue() == dataDate.longValue()) {
      assignData(dataDate, data, mtm);
      if (data.getClass() == CandleData.class) {
        this.mtmDate = dataDate;
        this.prev1DData = this.cur1DData;
        this.prevMDData = this.curMDData;
      }
      return; }
    if (dataDate.longValue() == 99999999L) {
      assignData(dataDate, data, mtm);
      return;
    }
    

    while (this.dataDriver.updateData()) {
      updateDataDriver();
      
      if (this.currentDate.longValue() < dataDate.longValue()) {
        this.prev1DData = this.cur1DData;
        this.prevMDData = this.curMDData;

      }
      else
      {
        if (this.currentDate.longValue() == dataDate.longValue())
        {
          assignData(dataDate, data, mtm);
          if (data.getClass() == CandleData.class)
          {
            this.mtmDate = dataDate;
            this.prev1DData = this.cur1DData;
            this.prevMDData = this.curMDData;
          }
        }
        return;
      }
    }
  }
  
  private void assignData(Long dataDate, CandleData data, Double mtm)
  {
    HashMap<String, String> metaDataMap = new HashMap();
    
    Boolean rollOver = Boolean.valueOf(this.prev1DData.rolloverCl.doubleValue() != -1.0D);
    Double roDiff;
    Double roDiff;
    if (rollOver.booleanValue()) {
      roDiff = Double.valueOf(this.prev1DData.rolloverCl.doubleValue() - this.prev1DData.cl.doubleValue());
    } else {
      roDiff = Double.valueOf(0.0D);
    }
    metaDataMap = this.prevMDData.dataMap;
    
    if (data.getClass().equals(CandleData.class))
    {
      data.updateData(this.prev1DData.op, this.prev1DData.hi, this.prev1DData.lo, this.prev1DData.cl, this.prev1DData.vol, roDiff, rollOver, 
        dataDate, this.prev1DData.exp, this.prev1DData.actualExp, this.prev1DData.rolloverExp, metaDataMap);
    }
    else if (data.getClass().equals(DailyData.class))
    {
      data.updateData(this.prev1DData.op, this.prev1DData.hi, this.prev1DData.lo, this.prev1DData.cl, this.prev1DData.vol, roDiff, rollOver, 
        dataDate, this.prev1DData.exp, this.prev1DData.actualExp, this.prev1DData.rolloverExp, mtm, metaDataMap);
    }
    else
    {
      System.err.println("Incoming class does not match with either DailyData or CandleData"); }
  }
  
  public Long getPrevDate() {
    return this.mtmDate;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/DailyDataReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */