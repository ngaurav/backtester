/*    */ package com.q1.bt.execution.order;
/*    */ import java.io.Serializable;
/*    */ 
/*  4 */ public enum OrderType implements Serializable { STOP(1),  LIMIT(2),  MARKET(3),  EOD(4),  OPEN(5),  OPENWITHSLIP(6),  ROLLOVER(7),  LIMITATPRICE(8),  LIMITWITHSLIP(8);
/*    */   
/*    */   private final int Val;
/*    */   
/*    */   private OrderType(int Val) {
/*  9 */     this.Val = Val;
/*    */   }
/*    */   
/*    */   public int getVal() {
/* 13 */     return this.Val;
/*    */   }
/*    */   
/*    */   public String getStringVal() {
/* 17 */     if (this.Val == 1)
/* 18 */       return "STOP";
/* 19 */     if (this.Val == 2)
/* 20 */       return "LIMIT";
/* 21 */     if (this.Val == 3)
/* 22 */       return "MARKET";
/* 23 */     if (this.Val == 4)
/* 24 */       return "EOD";
/* 25 */     if (this.Val == 5)
/* 26 */       return "OPEN";
/* 27 */     if (this.Val == 6)
/* 28 */       return "OPENWITHSLIP";
/* 29 */     if (this.Val == 7)
/* 30 */       return "ROLLOVER";
/* 31 */     if (this.Val == 8)
/* 32 */       return "LIMITATPRICE";
/* 33 */     if (this.Val == 9) {
/* 34 */       return "LIMITWITHSLIP";
/*    */     }
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/OrderType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */