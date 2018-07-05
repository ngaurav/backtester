/*    */ package com.q1.bt.process.objects;
/*    */ 
/*    */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*    */ 
/*    */ 
/*    */ public class MachineLearning
/*    */ {
/*    */   Backtest backtest;
/*    */   MachineLearningParameter mlParameter;
/*    */   String timeStamp;
/*    */   
/*    */   public MachineLearning(MachineLearningParameter mlParameter, Backtest backtest)
/*    */   {
/* 14 */     this.mlParameter = mlParameter;
/* 15 */     this.backtest = backtest;
/*    */   }
/*    */   
/*    */   public Backtest getBacktest()
/*    */   {
/* 20 */     return this.backtest;
/*    */   }
/*    */   
/*    */   public void setBacktest(Backtest backtest) {
/* 24 */     this.backtest = backtest;
/*    */   }
/*    */   
/*    */   public MachineLearningParameter getMlParameter() {
/* 28 */     return this.mlParameter;
/*    */   }
/*    */   
/*    */   public void setMlParameter(MachineLearningParameter mlParameter) {
/* 32 */     this.mlParameter = mlParameter;
/*    */   }
/*    */   
/*    */   public String getTimeStamp() {
/* 36 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public void setTimeStamp(String timeStamp) {
/* 40 */     this.timeStamp = timeStamp;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/objects/MachineLearning.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */