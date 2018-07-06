package com.q1.bt.data.classes;

import java.util.HashMap;




public class PreProcessData
{
  public String exchangeName;
  public String assetclassName;
  public String segmentName;
  public String scripName;
  String[] header;
  public HashMap<String, String> dataMap = new HashMap();
  
  Long startDate;
  
  Long endDate;
  
  int dataCount;
  
  public PreProcessData(Scrip scrip, String[] header)
  {
    this.exchangeName = scrip.exchangeName;
    this.assetclassName = scrip.assetClassName;
    this.segmentName = scrip.segmentName;
    this.scripName = scrip.scripName;
    

    this.header = header;
  }
  



  public PreProcessData(Scrip scrip)
  {
    this.exchangeName = scrip.exchangeName;
    this.assetclassName = scrip.assetClassName;
    this.segmentName = scrip.segmentName;
    this.scripName = scrip.scripName;
  }
  

  public PreProcessData(PreProcessData preData)
  {
    this.exchangeName = preData.exchangeName;
    this.assetclassName = preData.assetclassName;
    this.segmentName = preData.segmentName;
    this.scripName = preData.scripName;
    this.header = preData.header;
    this.dataMap = new HashMap(preData.dataMap);
  }
  


  public void addData(String[] dataLine)
  {
    this.dataMap = new HashMap();
    

    for (int i = 0; i < dataLine.length; i++) {
      this.dataMap.put(this.header[i], dataLine[i]);
    }
  }
  
  public void processMetaInfo(Object[] metaInfo)
  {
    this.startDate = ((Long)metaInfo[0]);
    this.endDate = ((Long)metaInfo[1]);
    this.dataCount = ((Integer)metaInfo[2]).intValue();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/PreProcessData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */