/*    */ package com.q1.bt.process.objects;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class BatchProcess
/*    */ {
/*    */   Backtest defaultBacktest;
/*    */   HashMap<String, String[]> parameterSet;
/*    */   
/*    */   public BatchProcess(Backtest defaultBacktest, HashMap<String, String[]> parameterSet)
/*    */   {
/* 12 */     this.defaultBacktest = defaultBacktest;
/* 13 */     this.parameterSet = parameterSet;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/objects/BatchProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */