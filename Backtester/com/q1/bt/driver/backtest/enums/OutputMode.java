/*    */ package com.q1.bt.driver.backtest.enums;
/*    */ 
/*    */ public enum OutputMode {
/*  4 */   Normal(0),  Existing(1),  Chop(2);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private OutputMode(int Val) {
/*  9 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 13 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/backtest/enums/OutputMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */