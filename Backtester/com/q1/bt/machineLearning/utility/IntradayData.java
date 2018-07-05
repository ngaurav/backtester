/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ 
/*    */ public class IntradayData
/*    */   extends CandleData
/*    */ {
/*    */   public DailyData previousDayData;
/*    */   public MetaData curMetaData;
/*    */   public Long time;
/*    */   
/*    */   public IntradayData(String assetName, String assetType)
/*    */   {
/* 13 */     super(assetName, assetType);
/*    */   }
/*    */   
/*    */   public void updateData(Double op, Double hi, Double lo, Double cl, Double vol, Long time)
/*    */   {
/* 18 */     this.op = op;
/* 19 */     this.hi = hi;
/* 20 */     this.lo = lo;
/* 21 */     this.cl = cl;
/* 22 */     this.vol = vol;
/* 23 */     this.time = time;
/*    */   }
/*    */   
/*    */ 
/*    */   public void updateData(DailyData previousDayData, MetaData curMetaData, boolean isRoll, Double rollOver)
/*    */   {
/* 29 */     this.rollOver = rollOver;
/* 30 */     this.isRoll = Boolean.valueOf(isRoll);
/* 31 */     this.previousDayData = previousDayData;
/* 32 */     this.curMetaData = curMetaData;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/IntradayData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */