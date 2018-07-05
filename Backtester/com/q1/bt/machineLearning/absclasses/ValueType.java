/*    */ package com.q1.bt.machineLearning.absclasses;
/*    */ 
/*    */ public enum ValueType {
/*  4 */   Numerical(1),  Categorical(2);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private ValueType(int Val) {
/*  9 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 13 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/ValueType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */