/*    */ package com.q1.bt.machineLearning.absclasses;
/*    */ 
/*    */ 
/*    */ public abstract class EMA
/*    */   extends CandleIndVar
/*    */ {
/*    */   protected Double ema;
/*    */   private Double alpha;
/*    */   
/*    */   public EMA(int period)
/*    */   {
/* 12 */     super(FactorType.Price, ValueType.Numerical);
/* 13 */     this.alpha = Double.valueOf(2.0D / (period + 1.0D));
/* 14 */     this.ema = null;
/*    */   }
/*    */   
/*    */   public void calculate(double cl) {
/* 18 */     if (this.ema == null) {
/* 19 */       this.ema = Double.valueOf(cl);
/*    */     } else {
/* 21 */       this.ema = Double.valueOf(cl * this.alpha.doubleValue() + this.ema.doubleValue() * (1.0D - this.alpha.doubleValue()));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/EMA.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */