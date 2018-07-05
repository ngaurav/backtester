/*    */ package com.q1.bt.data.classes;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PreProcessData
/*    */ {
/*    */   public String exchangeName;
/*    */   public String assetclassName;
/*    */   public String segmentName;
/*    */   public String scripName;
/*    */   String[] header;
/* 15 */   public HashMap<String, String> dataMap = new HashMap();
/*    */   
/*    */   Long startDate;
/*    */   
/*    */   Long endDate;
/*    */   
/*    */   int dataCount;
/*    */   
/*    */   public PreProcessData(Scrip scrip, String[] header)
/*    */   {
/* 25 */     this.exchangeName = scrip.exchangeName;
/* 26 */     this.assetclassName = scrip.assetClassName;
/* 27 */     this.segmentName = scrip.segmentName;
/* 28 */     this.scripName = scrip.scripName;
/*    */     
/*    */ 
/* 31 */     this.header = header;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public PreProcessData(Scrip scrip)
/*    */   {
/* 39 */     this.exchangeName = scrip.exchangeName;
/* 40 */     this.assetclassName = scrip.assetClassName;
/* 41 */     this.segmentName = scrip.segmentName;
/* 42 */     this.scripName = scrip.scripName;
/*    */   }
/*    */   
/*    */ 
/*    */   public PreProcessData(PreProcessData preData)
/*    */   {
/* 48 */     this.exchangeName = preData.exchangeName;
/* 49 */     this.assetclassName = preData.assetclassName;
/* 50 */     this.segmentName = preData.segmentName;
/* 51 */     this.scripName = preData.scripName;
/* 52 */     this.header = preData.header;
/* 53 */     this.dataMap = new HashMap(preData.dataMap);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addData(String[] dataLine)
/*    */   {
/* 60 */     this.dataMap = new HashMap();
/*    */     
/*    */ 
/* 63 */     for (int i = 0; i < dataLine.length; i++) {
/* 64 */       this.dataMap.put(this.header[i], dataLine[i]);
/*    */     }
/*    */   }
/*    */   
/*    */   public void processMetaInfo(Object[] metaInfo)
/*    */   {
/* 70 */     this.startDate = ((Long)metaInfo[0]);
/* 71 */     this.endDate = ((Long)metaInfo[1]);
/* 72 */     this.dataCount = ((Integer)metaInfo[2]).intValue();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/PreProcessData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */