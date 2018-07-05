/*    */ package com.q1.bt.randomParamSearch.paramDist;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ public class Square extends com.q1.bt.randomParamSearch.absClasses.Parameter
/*    */ {
/*    */   double constant;
/*    */   double step;
/*    */   double scale;
/*    */   
/*    */   public Square(double min, double max, double step) {
/* 12 */     this.constant = Math.sqrt(min);
/* 13 */     this.step = step;
/* 14 */     this.scale = (Math.sqrt(max) - Math.sqrt(min));
/* 15 */     this.distribution_type = com.q1.bt.randomParamSearch.enums.DistributionTypes.EXP;
/*    */   }
/*    */   
/*    */   public double getNext(Random rand)
/*    */   {
/* 20 */     int ret = (int)(Math.pow(rand.nextDouble() * this.scale + this.constant, 2.0D) / this.step);
/* 21 */     return this.step * ret;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/randomParamSearch/paramDist/Square.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */