/*    */ package com.q1.bt.optimize.genetic;
/*    */ 
/*    */ public class Parameter
/*    */ {
/*    */   Double startVal;
/*    */   Double step;
/*    */   Double maxVal;
/*    */   int binaryLength;
/*    */   
/*    */   public Parameter(Double startVal, Double step, Double maxVal) {
/* 11 */     this.startVal = startVal;
/* 12 */     this.step = step;
/* 13 */     this.maxVal = maxVal;
/* 14 */     int noOfValues = (int)((maxVal.doubleValue() - startVal.doubleValue()) / step.doubleValue());
/* 15 */     this.binaryLength = Integer.toBinaryString(noOfValues).length();
/*    */   }
/*    */   
/*    */   public byte[] generateValue()
/*    */   {
/* 20 */     byte[] gene = new byte[this.binaryLength];
/* 21 */     for (int i = 0; i < this.binaryLength; i++)
/* 22 */       gene[i] = ((byte)(int)Math.round(Math.random()));
/* 23 */     return gene;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/Parameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */