/*    */ package com.q1.bt.execution.order;
/*    */ 
/*    */ public enum Condition {
/*  4 */   Greater(1),  GreaterOrEqual(2),  Lesser(3),  LesserOrEqual(4),  Equal(5);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private Condition(int Val) {
/*  9 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 13 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/Condition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */