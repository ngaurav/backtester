/*    */ package com.q1.bt.postprocess;
/*    */ 
/*    */ public enum OutputShape
/*    */ {
/*  5 */   Line(1),  Scatter(2),  Area(3);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private OutputShape(int Val) {
/* 10 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 14 */     return this.Val;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/OutputShape.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */