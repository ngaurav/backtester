/*    */ package com.q1.bt.risk;
/*    */ 
/*    */ 
/*    */ public abstract class StrategyRM
/*    */ {
/*    */   String scripID;
/*    */   String strategyID;
/*    */   
/*    */   public StrategyRM(String strategyID)
/*    */   {
/* 11 */     this.strategyID = strategyID;
/*    */   }
/*    */   
/*    */   public abstract RMData runRM(RMData paramRMData);
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/risk/StrategyRM.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */