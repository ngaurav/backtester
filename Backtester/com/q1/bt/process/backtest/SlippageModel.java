/*    */ package com.q1.bt.process.backtest;
/*    */ 
/*    */ public enum SlippageModel
/*    */ {
/*  5 */   AdaptiveModel(1),  AdaptiveModelSigma(2),  AdaptiveModelATR(3),  ConstantSlippage(
/*  6 */     4),  ZeroSlippage(5),  LinearModel(6),  TickSlippage(7),  LinearModelHalfTick(
/*  7 */     8),  KylesLambdaSigma(9),  KylesLambdaATR(10);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private SlippageModel(int Val) {
/* 12 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 16 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/backtest/SlippageModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */