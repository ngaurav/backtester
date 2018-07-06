package com.q1.bt.execution;

import com.q1.bt.data.DataTypeViewer;
import com.q1.bt.data.ScripDataViewer;
import com.q1.bt.data.classes.ContractData;
import com.q1.bt.data.classes.MetaData;
import java.io.Serializable;
import java.util.HashMap;










public class ExecutionData
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public String scripListID;
  public String scripID;
  public Long dateTime;
  public Long date;
  public String dataType;
  public ContractData mainContractData;
  public ContractData rolloverContractData;
  public MetaData metaData;
  public Long prevOrderTimestamp = Long.valueOf(0L);
  
  public ExecutionData(ExecutionData execData)
  {
    this.scripListID = execData.scripListID;
    this.scripID = execData.scripID;
    this.dateTime = execData.dateTime;
    this.date = execData.date;
    this.dataType = execData.dataType;
    this.mainContractData = new ContractData(execData.mainContractData);
    this.rolloverContractData = new ContractData(execData.rolloverContractData);
    this.metaData = new MetaData(execData.metaData);
    this.prevOrderTimestamp = execData.prevOrderTimestamp;
  }
  



  public ExecutionData(String scripListID, String scripID, DataTypeViewer mainData, HashMap<String, DataTypeViewer> dataViewerMap, Long prevOrderTimestamp)
  {
    this.scripListID = scripListID;
    

    this.scripID = scripID;
    

    this.dataType = mainData.dataType;
    

    DataTypeViewer metaDataViewer = (DataTypeViewer)dataViewerMap.get("MD");
    

    this.dateTime = mainData.dateTime;
    this.date = mainData.date;
    

    this.prevOrderTimestamp = prevOrderTimestamp;
    

    this.mainContractData = ((ScripDataViewer)mainData.scripDataViewerMap.get(scripID)).contractData;
    

    if (this.dataType.contains("M")) {
      DataTypeViewer dailyData = (DataTypeViewer)dataViewerMap.get("1D");
      this.rolloverContractData = ((ScripDataViewer)dailyData.scripDataViewerMap.get(scripID)).contractData;
      this.date = ((ScripDataViewer)dailyData.scripDataViewerMap.get(scripID)).date;
    } else if (this.dataType.contains("D")) {
      this.rolloverContractData = this.mainContractData;
    }
    

    this.metaData = ((ScripDataViewer)metaDataViewer.scripDataViewerMap.get(scripID)).metaData;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/ExecutionData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */