/*    */ package com.q1.bt.execution.order;
/*    */ 
/*    */ public enum DataPoint
/*    */ {
/*  5 */   Open(1),  High(2),  Low(3),  Close(4);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private DataPoint(int Val) {
/* 10 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 14 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/DataPoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */