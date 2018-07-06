package com.q1.bt.machineLearning.utility;

import java.util.HashMap;

public class CandleData
{
  public Double cl;
  public Double op;
  public Double lo;
  public Double hi;
  public Double vol;
  public Long date;
  public Integer expiry;
  public Integer actualExpiry;
  public Integer rolloverExpiry;
  public String assetName;
  public String assetType;
  public Boolean isRoll;
  public Double rollOver;
  public HashMap<String, String> metaDataMap;
  
  public CandleData(String assetName, String assetType) {
    this.assetName = assetName;
    this.assetType = assetType;
    this.cl = null;
    this.op = null;
    this.lo = null;
    this.hi = null;
    this.isRoll = null;
    this.rollOver = null;
    this.date = Long.valueOf(0L);
    this.expiry = null;
    this.actualExpiry = null;
    this.rolloverExpiry = null;
    this.metaDataMap = null;
  }
  

  public void updateData(Double op, Double hi, Double lo, Double cl, Double vol, Double rollOver, Boolean isroll, Long date, Integer expiry, Integer actualExpiry, Integer rolloverExpiry, HashMap<String, String> metaDataMap)
  {
    this.cl = cl;
    this.op = op;
    this.lo = lo;
    this.hi = hi;
    this.vol = vol;
    this.rollOver = rollOver;
    this.isRoll = isroll;
    this.date = date;
    this.expiry = expiry;
    this.actualExpiry = actualExpiry;
    this.rolloverExpiry = rolloverExpiry;
    this.metaDataMap = metaDataMap;
  }
  
  public void updateData(Double op, Double hi, Double lo, Double cl, Double vol, Double rollOver, Boolean isroll, Long date, Integer expiry, Integer actualExpiry, Integer rolloverExpiry, Double mtm, HashMap<String, String> metaDataMap) {}
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/CandleData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */