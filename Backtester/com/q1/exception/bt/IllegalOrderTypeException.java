/*    */ package com.q1.exception.bt;
/*    */ 
/*    */ public class IllegalOrderTypeException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public IllegalOrderTypeException()
/*    */   {
/* 10 */     super("Order Type should either be 1.0 (Buy) or 2.0 (Sell).");
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/exception/bt/IllegalOrderTypeException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */