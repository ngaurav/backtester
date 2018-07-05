/*    */ package com.q1.bt.data.classes;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FundaData
/*    */ {
/*    */   public String segmentName;
/* 11 */   public HashMap<String, String> dataMap = new HashMap();
/*    */   
/*    */   Long startDate;
/*    */   Long endDate;
/*    */   int dataCount;
/*    */   
/*    */   public FundaData(String segmentName)
/*    */   {
/* 19 */     this.segmentName = segmentName;
/*    */   }
/*    */   
/*    */   public FundaData(FundaData fD)
/*    */   {
/* 24 */     this.segmentName = fD.segmentName;
/* 25 */     this.startDate = fD.startDate;
/* 26 */     this.endDate = fD.endDate;
/* 27 */     this.dataCount = fD.dataCount;
/* 28 */     this.dataMap = new HashMap(fD.dataMap);
/* 29 */     this.startDate = fD.startDate;
/* 30 */     this.endDate = fD.endDate;
/* 31 */     this.dataCount = fD.dataCount;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addData(String[] dataLine)
/*    */   {
/* 38 */     this.dataMap = new HashMap();
/*    */     
/*    */     String[] arrayOfString1;
/* 41 */     int j = (arrayOfString1 = dataLine).length; for (int i = 0; i < j; i++) { String data = arrayOfString1[i];
/* 42 */       String[] dataVal = data.split("\\|");
/* 43 */       if (dataVal.length == 2) {
/* 44 */         this.dataMap.put(dataVal[0], dataVal[1]);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void processMetaInfo(Object[] metaInfo) {
/* 50 */     this.startDate = ((Long)metaInfo[0]);
/* 51 */     this.endDate = ((Long)metaInfo[1]);
/* 52 */     this.dataCount = ((Integer)metaInfo[2]).intValue();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/FundaData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */