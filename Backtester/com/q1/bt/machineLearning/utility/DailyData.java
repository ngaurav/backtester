package com.q1.bt.machineLearning.utility;

import java.util.HashMap;

public class DailyData extends CandleData
{
  String assetName;
  public Double mtm;
  
  public DailyData(String assetName, String assetType)
  {
    super(assetName, assetType);
    this.mtm = null;
    this.assetName = assetName;
  }
  
  public void updateData(Double op, Double hi, Double lo, Double cl, Double vol, Double rollOver, Boolean isroll, Long date, Integer expiry, Integer actualExpiry, Integer rolloverExpiry, Double mtm, HashMap<String, String> metaDataMap)
  {
    updateData(op, hi, lo, cl, vol, rollOver, isroll, date, expiry, actualExpiry, rolloverExpiry, metaDataMap);
    this.mtm = Double.valueOf(mtm == null ? 0.0D : mtm.doubleValue());
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/DailyData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */