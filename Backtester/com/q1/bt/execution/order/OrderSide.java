/*    */ package com.q1.bt.execution.order;
/*    */ 
/*    */ public enum OrderSide {
/*  4 */   BUY(1),  SELL(2);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private OrderSide(int Val) {
/*  9 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 13 */     return this.Val;
/*    */   }
/*    */   
/*    */   public String getStringVal() {
/* 17 */     if (this.Val == 1)
/* 18 */       return "BUY";
/* 19 */     if (this.Val == 2) {
/* 20 */       return "SELL";
/*    */     }
/* 22 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/OrderSide.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */