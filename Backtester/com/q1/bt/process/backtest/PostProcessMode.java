/*    */ package com.q1.bt.process.backtest;
/*    */ 
/*    */ public enum PostProcessMode
/*    */ {
/*  5 */   SingleScrip(1),  Spread(2),  Portfolio(3);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private PostProcessMode(int Val) {
/* 10 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 14 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/backtest/PostProcessMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */