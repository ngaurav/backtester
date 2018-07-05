/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ public class CandleData
/*    */ {
/*    */   public Double cl;
/*    */   public Double op;
/*    */   public Double lo;
/*    */   public Double hi;
/*    */   public Double vol;
/*    */   public Long date;
/*    */   public String assetName;
/*    */   public String assetType;
/*    */   public Boolean isRoll;
/*    */   public Double rollOver;
/*    */   
/*    */   public CandleData(String assetName, String assetType) {
/* 17 */     this.assetName = assetName;
/* 18 */     this.assetType = assetType;
/* 19 */     this.cl = null;
/* 20 */     this.op = null;
/* 21 */     this.lo = null;
/* 22 */     this.hi = null;
/* 23 */     this.isRoll = null;
/* 24 */     this.rollOver = null;
/* 25 */     this.date = Long.valueOf(0L);
/*    */   }
/*    */   
/*    */ 
/*    */   public void updateData(Double op, Double hi, Double lo, Double cl, Double vol, Double rollOver, Boolean isroll, Long date)
/*    */   {
/* 31 */     this.cl = cl;
/* 32 */     this.op = op;
/* 33 */     this.lo = lo;
/* 34 */     this.hi = hi;
/* 35 */     this.vol = vol;
/* 36 */     this.rollOver = rollOver;
/* 37 */     this.isRoll = isroll;
/* 38 */     this.date = date;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/CandleData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */