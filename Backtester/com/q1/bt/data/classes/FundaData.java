package com.q1.bt.data.classes;

import java.util.HashMap;




public class FundaData
{
  public String segmentName;
  public HashMap<String, String> dataMap = new HashMap();
  
  Long startDate;
  Long endDate;
  int dataCount;
  
  public FundaData(String segmentName)
  {
    this.segmentName = segmentName;
  }
  
  public FundaData(FundaData fD)
  {
    this.segmentName = fD.segmentName;
    this.startDate = fD.startDate;
    this.endDate = fD.endDate;
    this.dataCount = fD.dataCount;
    this.dataMap = new HashMap(fD.dataMap);
    this.startDate = fD.startDate;
    this.endDate = fD.endDate;
    this.dataCount = fD.dataCount;
  }
  


  public void addData(String[] dataLine)
  {
    this.dataMap = new HashMap();
    
    String[] arrayOfString1;
    int j = (arrayOfString1 = dataLine).length; for (int i = 0; i < j; i++) { String data = arrayOfString1[i];
      String[] dataVal = data.split("\\|");
      if (dataVal.length == 2) {
        this.dataMap.put(dataVal[0], dataVal[1]);
      }
    }
  }
  
  public void processMetaInfo(Object[] metaInfo) {
    this.startDate = ((Long)metaInfo[0]);
    this.endDate = ((Long)metaInfo[1]);
    this.dataCount = ((Integer)metaInfo[2]).intValue();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/FundaData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */