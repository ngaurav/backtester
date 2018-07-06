package com.q1.bt.machineLearning.utility;


public class IntradayData
  extends CandleData
{
  public DailyData previousDayData;
  public MetaData curMetaData;
  public Long time;
  
  public IntradayData(String assetName, String assetType)
  {
    super(assetName, assetType);
  }
  
  public void updateData(Double op, Double hi, Double lo, Double cl, Double vol, Long time)
  {
    this.op = op;
    this.hi = hi;
    this.lo = lo;
    this.cl = cl;
    this.vol = vol;
    this.time = time;
  }
  

  public void updateData(DailyData previousDayData, MetaData curMetaData, boolean isRoll, Double rollOver)
  {
    this.rollOver = rollOver;
    this.isRoll = Boolean.valueOf(isRoll);
    this.previousDayData = previousDayData;
    this.curMetaData = curMetaData;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/IntradayData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */