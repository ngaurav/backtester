/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class CandleData
/*    */ {
/*    */   public Double cl;
/*    */   public Double op;
/*    */   public Double lo;
/*    */   public Double hi;
/*    */   public Double vol;
/*    */   public Long date;
/*    */   public Integer expiry;
/*    */   public Integer actualExpiry;
/*    */   public Integer rolloverExpiry;
/*    */   public String assetName;
/*    */   public String assetType;
/*    */   public Boolean isRoll;
/*    */   public Double rollOver;
/*    */   public HashMap<String, String> metaDataMap;
/*    */   
/*    */   public CandleData(String assetName, String assetType) {
/* 23 */     this.assetName = assetName;
/* 24 */     this.assetType = assetType;
/* 25 */     this.cl = null;
/* 26 */     this.op = null;
/* 27 */     this.lo = null;
/* 28 */     this.hi = null;
/* 29 */     this.isRoll = null;
/* 30 */     this.rollOver = null;
/* 31 */     this.date = Long.valueOf(0L);
/* 32 */     this.expiry = null;
/* 33 */     this.actualExpiry = null;
/* 34 */     this.rolloverExpiry = null;
/* 35 */     this.metaDataMap = null;
/*    */   }
/*    */   
/*    */ 
/*    */   public void updateData(Double op, Double hi, Double lo, Double cl, Double vol, Double rollOver, Boolean isroll, Long date, Integer expiry, Integer actualExpiry, Integer rolloverExpiry, HashMap<String, String> metaDataMap)
/*    */   {
/* 41 */     this.cl = cl;
/* 42 */     this.op = op;
/* 43 */     this.lo = lo;
/* 44 */     this.hi = hi;
/* 45 */     this.vol = vol;
/* 46 */     this.rollOver = rollOver;
/* 47 */     this.isRoll = isroll;
/* 48 */     this.date = date;
/* 49 */     this.expiry = expiry;
/* 50 */     this.actualExpiry = actualExpiry;
/* 51 */     this.rolloverExpiry = rolloverExpiry;
/* 52 */     this.metaDataMap = metaDataMap;
/*    */   }
/*    */   
/*    */   public void updateData(Double op, Double hi, Double lo, Double cl, Double vol, Double rollOver, Boolean isroll, Long date, Integer expiry, Integer actualExpiry, Integer rolloverExpiry, Double mtm, HashMap<String, String> metaDataMap) {}
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/CandleData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */