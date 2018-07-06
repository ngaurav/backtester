package com.q1.bt.data.classes;


public class Scrip
{
  public String exchangeName;
  public String assetClassName;
  public String segmentName;
  public String scripName;
  public String contractType;
  public String scripID;
  
  public Scrip(String exchangeName, String assetClassName, String segmentName, String scripName, String contractType)
  {
    this.exchangeName = exchangeName;
    this.assetClassName = assetClassName;
    this.segmentName = segmentName;
    this.scripName = scripName;
    this.contractType = contractType;
    this.scripID = (exchangeName + " " + assetClassName + " " + segmentName + " " + scripName + " " + contractType);
  }
  
  public Scrip(String scripID) throws Exception
  {
    String[] scripIDVal = scripID.split(" ");
    
    if (scripIDVal.length != 5) {
      throw new Exception("Incorrect Scrip ID format: " + scripID);
    }
    this.exchangeName = scripIDVal[0];
    this.assetClassName = scripIDVal[1];
    this.segmentName = scripIDVal[2];
    this.scripName = scripIDVal[3];
    this.contractType = scripIDVal[4];
    this.scripID = scripID;
  }
  
  public String getDataID(String dataType) {
    return this.exchangeName + " " + this.assetClassName + " " + this.segmentName + " " + this.scripName + " " + dataType;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/Scrip.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */