/*    */ package com.q1.bt.randomParamSearch.paramDist;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ public class Exp extends com.q1.bt.randomParamSearch.absClasses.Parameter
/*    */ {
/*    */   double constant;
/*    */   double step;
/*    */   double scale;
/*    */   
/*    */   public Exp(double min, double max, double step) {
/* 12 */     this.constant = Math.log(min);
/* 13 */     this.step = step;
/* 14 */     this.scale = Math.log(max / min);
/* 15 */     this.distribution_type = com.q1.bt.randomParamSearch.enums.DistributionTypes.EXP;
/*    */   }
/*    */   
/*    */   public double getNext(Random rand)
/*    */   {
/* 20 */     int ret = (int)(Math.exp(rand.nextDouble() * this.scale + this.constant) / this.step);
/* 21 */     return this.step * ret;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/randomParamSearch/paramDist/Exp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */