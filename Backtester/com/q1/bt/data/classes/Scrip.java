/*    */ package com.q1.bt.data.classes;
/*    */ 
/*    */ 
/*    */ public class Scrip
/*    */ {
/*    */   public String exchangeName;
/*    */   public String assetClassName;
/*    */   public String segmentName;
/*    */   public String scripName;
/*    */   public String contractType;
/*    */   public String scripID;
/*    */   
/*    */   public Scrip(String exchangeName, String assetClassName, String segmentName, String scripName, String contractType)
/*    */   {
/* 15 */     this.exchangeName = exchangeName;
/* 16 */     this.assetClassName = assetClassName;
/* 17 */     this.segmentName = segmentName;
/* 18 */     this.scripName = scripName;
/* 19 */     this.contractType = contractType;
/* 20 */     this.scripID = (exchangeName + " " + assetClassName + " " + segmentName + " " + scripName + " " + contractType);
/*    */   }
/*    */   
/*    */   public Scrip(String scripID) throws Exception
/*    */   {
/* 25 */     String[] scripIDVal = scripID.split(" ");
/*    */     
/* 27 */     if (scripIDVal.length != 5) {
/* 28 */       throw new Exception("Incorrect Scrip ID format: " + scripID);
/*    */     }
/* 30 */     this.exchangeName = scripIDVal[0];
/* 31 */     this.assetClassName = scripIDVal[1];
/* 32 */     this.segmentName = scripIDVal[2];
/* 33 */     this.scripName = scripIDVal[3];
/* 34 */     this.contractType = scripIDVal[4];
/* 35 */     this.scripID = scripID;
/*    */   }
/*    */   
/*    */   public String getDataID(String dataType) {
/* 39 */     return this.exchangeName + " " + this.assetClassName + " " + this.segmentName + " " + this.scripName + " " + dataType;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/Scrip.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */