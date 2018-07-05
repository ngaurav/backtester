/*    */ package com.q1.bt.execution;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class EMA
/*    */   implements Serializable
/*    */ {
/*    */   private Double ema;
/*    */   private Double alpha;
/*    */   
/*    */   public EMA(int period)
/*    */   {
/* 13 */     this.alpha = Double.valueOf(2.0D / (period + 1.0D));
/* 14 */     this.ema = null;
/*    */   }
/*    */   
/*    */ 
/*    */   public Double calculateEMA(double val)
/*    */   {
/* 20 */     if (this.ema == null) {
/* 21 */       this.ema = Double.valueOf(val);
/*    */     } else {
/* 23 */       this.ema = Double.valueOf(val * this.alpha.doubleValue() + this.ema.doubleValue() * (1.0D - this.alpha.doubleValue()));
/*    */     }
/* 25 */     return this.ema;
/*    */   }
/*    */   
/*    */   public Double getEMA()
/*    */   {
/* 30 */     return this.ema;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/EMA.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */