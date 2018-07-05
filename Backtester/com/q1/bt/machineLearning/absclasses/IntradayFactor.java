/*    */ package com.q1.bt.machineLearning.absclasses;
/*    */ 
/*    */ import com.q1.bt.machineLearning.utility.IntradayData;
/*    */ 
/*    */ public class IntradayFactor {
/*    */   String ClassName;
/*    */   String factorName;
/*    */   IntradayIndVar dv;
/*    */   
/*    */   public IntradayFactor(String factorName, IntradayIndVar dv) {
/* 11 */     this.ClassName = dv.getClass().getSimpleName();
/* 12 */     this.factorName = factorName;
/* 13 */     this.dv = dv;
/*    */   }
/*    */   
/*    */   public void updateInd(IntradayData dd) {
/* 17 */     this.dv.updateInd(dd);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 25 */     return this.factorName;
/*    */   }
/*    */   
/*    */   public String getfactorType() {
/* 29 */     return this.dv.getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/IntradayFactor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */