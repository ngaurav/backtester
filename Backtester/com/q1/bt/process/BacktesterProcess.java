/*    */ package com.q1.bt.process;
/*    */ 
/*    */ public enum BacktesterProcess
/*    */ {
/*  5 */   Login(
/*  6 */     "Login"),  Backtest("Backtest"),  Results("Results"),  SensitivityAnalysis("Sensitivity Analysis"),  PostProcess("Post Process"),  MachineLearning(
/*  7 */     "Machine Learning"),  AssetAllocation("Asset Allocation"),  BatchProcess("Batch Process"),  RollingAnalysis("Rolling Analysis"),  IsOs("In Sample OOS"),  ResultGeneration("Result Generation");
/*    */   
/*    */   private final String Val;
/*    */   
/*    */   private BacktesterProcess(String Val) {
/* 12 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public String getVal() {
/* 16 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/BacktesterProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */