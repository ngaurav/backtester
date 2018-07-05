/*    */ package com.q1.bt.machineLearning.absclasses;
/*    */ 
/*    */ import com.q1.bt.machineLearning.utility.CandleData;
/*    */ 
/*    */ public class Factor
/*    */ {
/*    */   String ClassName;
/*    */   String factorName;
/*    */   IndVar dv;
/*    */   String factorType;
/*    */   
/*    */   public Factor(String factorName, IndVar dv) {
/* 13 */     this.ClassName = dv.getClass().getSimpleName();
/* 14 */     this.factorName = factorName;
/* 15 */     this.dv = dv;
/*    */   }
/*    */   
/*    */   public void updateInd(CandleData[] dd) {
/* 19 */     if ((this.dv instanceof CandleIndVar)) {
/* 20 */       ((CandleIndVar)this.dv).updateInd(dd);
/*    */     }
/* 22 */     else if (((this.dv instanceof DailyIndVar)) && ((dd[0] instanceof com.q1.bt.machineLearning.utility.DailyData))) {
/* 23 */       ((DailyIndVar)this.dv).updateInd((com.q1.bt.machineLearning.utility.DailyData[])dd);
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/*    */ 
/* 29 */       System.err.println("Indicator type doesn't match with the data provided for " + this.factorName); }
/*    */   }
/*    */   
/*    */   public Double[] getValue() {
/* 33 */     return this.dv.getInd();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 37 */     return this.factorName;
/*    */   }
/*    */   
/*    */   public String getfactorType() {
/* 41 */     return this.dv.getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/Factor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */