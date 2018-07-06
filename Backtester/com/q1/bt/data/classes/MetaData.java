package com.q1.bt.data.classes;

import com.q1.sql.SQLdata;
import java.sql.SQLException;
import java.util.HashMap;







public class MetaData
{
  public String exchangeName;
  public String assetclassName;
  public String segmentName;
  public String scripName;
  public Double slippage;
  public Double openSlippage;
  public Double slippageSlope;
  public Double slippageIntercept;
  public Double lotSize;
  public Double lotFactor;
  public Double tickSize;
  public Integer leverage;
  public Double exchangeFees;
  public Double brokerage;
  String[] header;
  public HashMap<String, String> dataMap = new HashMap();
  


  public MetaData(Scrip scrip, String[] header)
  {
    this.exchangeName = scrip.exchangeName;
    this.assetclassName = scrip.assetClassName;
    this.segmentName = scrip.segmentName;
    this.scripName = scrip.scripName;
    

    this.header = header;
  }
  



  public MetaData(Scrip scrip)
  {
    this.exchangeName = scrip.exchangeName;
    this.assetclassName = scrip.assetClassName;
    this.segmentName = scrip.segmentName;
    this.scripName = scrip.scripName;
  }
  

  public MetaData(MetaData mD)
  {
    this.exchangeName = mD.exchangeName;
    this.assetclassName = mD.assetclassName;
    this.segmentName = mD.segmentName;
    this.scripName = mD.scripName;
    this.header = mD.header;
    this.slippage = mD.slippage;
    this.openSlippage = mD.openSlippage;
    this.slippageSlope = mD.slippageSlope;
    this.slippageIntercept = mD.slippageIntercept;
    this.lotSize = mD.lotSize;
    this.lotFactor = mD.lotFactor;
    this.tickSize = mD.tickSize;
    this.leverage = mD.leverage;
    this.exchangeFees = mD.exchangeFees;
    this.brokerage = mD.brokerage;
    this.dataMap = new HashMap(mD.dataMap);
  }
  
  public void copyMetaData(MetaData mD)
  {
    this.exchangeName = mD.exchangeName;
    this.assetclassName = mD.assetclassName;
    this.segmentName = mD.segmentName;
    this.scripName = mD.scripName;
    this.header = mD.header;
    if (mD.slippage != null)
      this.slippage = mD.slippage;
    if (mD.openSlippage != null)
      this.openSlippage = mD.openSlippage;
    if (mD.slippageSlope != null)
      this.slippageSlope = mD.slippageSlope;
    if (mD.slippageIntercept != null)
      this.slippageIntercept = mD.slippageIntercept;
    if (mD.lotSize != null)
      this.lotSize = mD.lotSize;
    if (mD.lotFactor != null)
      this.lotFactor = mD.lotFactor;
    if (mD.tickSize != null)
      this.tickSize = mD.tickSize;
    if (mD.leverage != null)
      this.leverage = mD.leverage;
    if (mD.exchangeFees != null)
      this.exchangeFees = mD.exchangeFees;
    if (mD.brokerage != null)
      this.brokerage = mD.brokerage;
    this.dataMap = new HashMap(mD.dataMap);
  }
  
  public void readMetaDataFromDatabase(SQLdata sqlObject, String dataType) throws SQLException
  {
    this.tickSize = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
      "Segment", this.segmentName, "Tick Size"));
    
    this.slippage = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
      "Segment", this.segmentName, dataType + " Slippage"));
    
    this.openSlippage = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
      "Segment", this.segmentName, dataType + " Open Slippage"));
    
    this.slippageSlope = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
      "Segment", this.segmentName, "Slippage Slope"));
    
    this.slippageIntercept = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", 
      this.scripName, "Segment", this.segmentName, "Slippage Intercept"));
    
    this.lotSize = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
      "Segment", this.segmentName, "Lot Size"));
    
    this.lotFactor = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
      "Segment", this.segmentName, "Lot Factor"));
    
    this.leverage = Integer.valueOf(sqlObject.getIntValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
      "Segment", this.segmentName, "Leverage"));
    
    this.exchangeFees = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
      "Segment", this.segmentName, "Transaction Charges"));
    
    this.brokerage = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
      "Segment", this.segmentName, "Brokerage"));
    sqlObject.close();
  }
  


  public void addData(String[] dataLine)
  {
    this.dataMap = new HashMap();
    

    for (int i = 0; i < dataLine.length; i++) {
      boolean checkExistingVariable = updateContractVariable(this.header[i], dataLine[i]);
      if (!checkExistingVariable) {
        this.dataMap.put(this.header[i], dataLine[i]);
      }
    }
  }
  

  public boolean updateContractVariable(String header, String value)
  {
    if (header.equals("Slippage")) {
      this.slippage = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Open Slippage")) {
      this.openSlippage = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Slippage Slope")) {
      this.slippageSlope = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Slippage Intercept")) {
      this.slippageIntercept = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Lot Size")) {
      this.lotSize = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Lot Factor")) {
      this.lotFactor = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Tick Size")) {
      this.tickSize = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Leverage")) {
      this.leverage = Integer.valueOf(Integer.parseInt(value));

    }
    else if (header.equals("Exchange Fee")) {
      this.exchangeFees = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Brokerage")) {
      this.brokerage = Double.valueOf(Double.parseDouble(value));
    }
    else {
      return false;
    }
    return true;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/MetaData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */