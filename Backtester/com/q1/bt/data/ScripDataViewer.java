package com.q1.bt.data;

import com.q1.bt.data.classes.ContractData;
import com.q1.bt.data.classes.FundaData;
import com.q1.bt.data.classes.MetaData;
import com.q1.bt.data.classes.PreProcessData;
import java.util.HashMap;




public class ScripDataViewer
  extends Thread
{
  String dataType;
  HashMap<String, Long> dateTimeMap = new HashMap();
  
  public Long date;
  
  public Long time;
  
  public Long dateTime;
  public ContractData contractData = null;
  public MetaData metaData = null;
  PreProcessData preData = null;
  FundaData fundaData = null;
  
  public Long startDate;
  
  public Long endDate;
  
  public int dataCount;
  public boolean eof = false;
  public boolean noUpdate = false;
  public boolean fileExists = true;
  
  public ScripDataViewer(String dataType, MetaData metaData)
  {
    this.metaData = metaData;
    this.dataType = dataType;
  }
  
  public ScripDataViewer(String dataType, ContractData contractData)
  {
    this.contractData = contractData;
    this.dataType = dataType;
  }
  
  public ScripDataViewer(String dataType, PreProcessData preData)
  {
    this.preData = preData;
    this.dataType = dataType;
  }
  
  public ScripDataViewer(String dataType, FundaData fundaData)
  {
    this.fundaData = fundaData;
    this.dataType = dataType;
  }
  


  public void updateData(ScripDataHandler sdHandler)
  {
    this.date = sdHandler.date;
    this.time = sdHandler.time;
    this.dateTime = sdHandler.dateTime;
    

    this.startDate = sdHandler.startDate;
    this.endDate = sdHandler.endDate;
    this.dataCount = sdHandler.dataCount;
    

    if ((this.fileExists) && (this.dataType.equals("MD")))
    {

      this.fileExists = sdHandler.fileExists;
      if (!this.fileExists) {
        return;
      }
      this.metaData.copyMetaData(sdHandler.metaData);


    }
    else if (this.dataType.equals("PP")) {
      this.preData = new PreProcessData(sdHandler.preData);

    }
    else if (this.dataType.equals("FD")) {
      this.fundaData = new FundaData(sdHandler.fundaData);
    }
    else
    {
      this.contractData = new ContractData(sdHandler.contractData);
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/ScripDataViewer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */