/*    */ package com.q1.bt.machineLearning.absclasses;
/*    */ 
/*    */ import com.q1.bt.machineLearning.utility.CandleData;
/*    */ 
/*    */ public class DailyFactor {
/*    */   String ClassName;
/*    */   String factorName;
/*    */   CandleIndVar dv;
/*    */   
/*    */   public DailyFactor(String factorName, CandleIndVar dv) {
/* 11 */     this.ClassName = dv.getClass().getSimpleName();
/* 12 */     this.factorName = factorName;
/* 13 */     this.dv = dv;
/*    */   }
/*    */   
/*    */   public void updateInd(CandleData[] dd) {
/* 17 */     this.dv.updateInd(dd);
/*    */   }
/*    */   
/*    */   public Double[] getValue() {
/* 21 */     return this.dv.getInd();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 25 */     return this.factorName;
/*    */   }
/*    */   
/*    */   public String getfactorType() {
/* 29 */     return this.dv.getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/DailyFactor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */