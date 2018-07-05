/*    */ package com.q1.bt.process.machinelearning;
/*    */ 
/*    */ public enum MergeType
/*    */ {
/*  5 */   All("All"),  Strategy("Strategy"),  AssetClass("Asset Class"),  ScripList("ScripList"),  Scrip("Scrip");
/*    */   
/*    */   private final String Val;
/*    */   
/*    */   private MergeType(String Val) {
/* 10 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public String getVal() {
/* 14 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/machinelearning/MergeType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */