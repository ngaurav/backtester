/*    */ package com.q1.bt.machineLearning.absclasses;
/*    */ 
/*    */ public enum FactorType {
/*  4 */   Price(1),  MTM(2),  External(3);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private FactorType(int Val) {
/*  9 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 13 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/FactorType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */