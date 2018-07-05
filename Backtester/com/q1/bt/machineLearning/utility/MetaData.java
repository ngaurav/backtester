/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ 
/*    */ public class MetaData
/*    */ {
/*    */   public Long date;
/*    */   
/*    */   public Double upperCircuit;
/*    */   
/*    */   public Double lowerCircuit;
/*    */   public Long dayStartTime;
/*    */   public Long dayEndTime;
/*    */   public Long s1StartTime;
/*    */   public Long s1EndTime;
/*    */   public Long s2StartTime;
/*    */   public Long s2EndTime;
/*    */   public Long s3StartTime;
/*    */   public Long s3EndTime;
/*    */   
/*    */   public void updateData(Long date, Double upperCircuit, Double lowerCircuit, Long dayStartTime, Long dayEndTime, Long s1StartTime, Long s1EndTime, Long s2StartTime, Long s2EndTime, Long s3StartTime, Long s3EndTime)
/*    */   {
/* 22 */     this.date = date;
/* 23 */     this.upperCircuit = upperCircuit;
/* 24 */     this.lowerCircuit = lowerCircuit;
/* 25 */     this.dayStartTime = dayStartTime;
/* 26 */     this.dayEndTime = dayEndTime;
/* 27 */     this.s1StartTime = s1StartTime;
/* 28 */     this.s1EndTime = s1EndTime;
/* 29 */     this.s2StartTime = s2StartTime;
/* 30 */     this.s2EndTime = s2EndTime;
/* 31 */     this.s3StartTime = s3StartTime;
/* 32 */     this.s3EndTime = s3EndTime;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/MetaData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */