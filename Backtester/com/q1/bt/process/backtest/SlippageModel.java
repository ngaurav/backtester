/*    */ package com.q1.bt.process.backtest;
/*    */ 
/*    */ public enum SlippageModel
/*    */ {
/*  5 */   AdaptiveModelSigma(1),  AdaptiveModelATR(2),  ConstantSlippage(3),  ZeroSlippage(4),  LinearModel(5),  TickSlippage(
/*  6 */     6),  LinearModelHalfTick(7),  KylesLambdaSigma(8),  KylesLambdaATR(9);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private SlippageModel(int Val) {
/* 11 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 15 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/backtest/SlippageModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */